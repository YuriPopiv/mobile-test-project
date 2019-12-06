//
//  Threads.m
//  Accelo
//
//  Created by Kuryliak Maksym on 06.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import "Threads.h"

@implementation Threads

+ (NSDictionary *)JSONKeyPathsByPropertyKey {
    return @{
        @"activities": @"activities",
        @"eventText":@"event_text",
        @"threadID":@"id",
        @"interacts":@"interacts",
        @"totalActivities":@"total_activities"
    };
}

+ (NSValueTransformer *)activitiesJSONTransformer {
    return [MTLJSONAdapter arrayTransformerWithModelClass:[Activity class]];
}

+ (NSValueTransformer *)interactsJSONTransformer {
    return [MTLJSONAdapter arrayTransformerWithModelClass:[Interact class]];
}

@end
