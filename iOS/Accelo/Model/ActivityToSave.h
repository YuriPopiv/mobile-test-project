//
//  ActivityToSave.h
//  Accelo
//
//  Created by Kuryliak Maksym on 10.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import <Realm/Realm.h>

@interface ActivityToSave : RLMObject

@property NSString *againstId;
@property NSString *againstType;
@property NSString *medium;
@property NSString *ownerType;
@property NSString *visibility;
@property NSString *subject;
@property NSString *body;
@property NSString *activityNumber;

- (id)initWithSubject:(NSString *)subjectText andBody:(NSString *)bodyText;
- (NSDictionary *)toDictionary;

@end
