//
//  ActivityToSave.m
//  Accelo
//
//  Created by Kuryliak Maksym on 10.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import "ActivityToSave.h"

@implementation ActivityToSave

+ (NSString *)primaryKey {
    return @"activityNumber";
}

- (id)initWithSubject:(NSString *)subjectText andBody:(NSString *)bodyText {
    self = [super init];
    if(self) {
        self.againstId = @"47";
        self.againstType = @"task";
        self.medium = @"note";
        self.ownerType = @"staff";
        self.visibility = @"all";
        self.subject = subjectText;
        self.body = bodyText;
    }
    return self;
}

- (NSDictionary *)toDictionary {
    NSDictionary *dictionary = [[NSDictionary alloc] initWithObjectsAndKeys:
                                self.againstId, @"against_id",
                                self.againstType, @"against_type",
                                self.medium, @"medium",
                                self.ownerType, @"owner_type",
                                self.visibility, @"visibility",
                                self.subject, @"subject",
                                self.body, @"body", nil];
    return dictionary;
}

@end
