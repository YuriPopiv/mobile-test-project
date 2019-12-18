//
//  SavingManager.m
//  Accelo
//
//  Created by Kuryliak Maksym on 16.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import "UploadManager.h"
#import "APIManager.h"
#import "Constants.h"
#import "Reachability.h"
#import "Request.h"
#import "ActivityToSave.h"

@implementation UploadManager

- (void)uploadActivity {
    RLMRealm *realm = [RLMRealm defaultRealm];
    
    /// create semaphore with starting state as 0, semaphore_wait makes state as -1, so the for statement is waiting for semaphore_signal, which will make state as 0 again, to run further
    dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        dispatch_semaphore_t semaphore = dispatch_semaphore_create(0);
        
        /// sort all activites by "activityNumber" to keep activities order
        RLMResults<ActivityToSave *> *activities = [[ActivityToSave allObjects] sortedResultsUsingKeyPath:@"activityNumber"
                                                                                            ascending:YES];
        for (ActivityToSave *activity in activities) {
            NSString *key = activity.activityNumber;
            NSDictionary *params = [activity toDictionary];
            [[APIManager sharedManager] POST:kCreateActivity parameters:params progress:nil
                                     success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
                Request *request = [MTLJSONAdapter modelOfClass:Request.class fromJSONDictionary:responseObject error:nil];
                if ([request.meta.status isEqualToString:@"ok"]) {
                    /// after uploading the activity delete it from realm
                    ActivityToSave *uploadedActivity = [ActivityToSave objectForPrimaryKey:key];
                    [realm transactionWithBlock:^{
                        [realm deleteObject:uploadedActivity];
                    }];
                    dispatch_semaphore_signal(semaphore);
                }
            } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
                dispatch_semaphore_signal(semaphore);
                NSLog(@"%@", error);
            }];
            dispatch_semaphore_wait(semaphore, DISPATCH_TIME_FOREVER);
            NSLog(@"AcTivity %@", activity);
        }
    });
}

@end
