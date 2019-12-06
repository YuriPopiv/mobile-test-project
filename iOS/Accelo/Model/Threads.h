//
//  Threads.h
//  Accelo
//
//  Created by Kuryliak Maksym on 06.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Activity.h"
#import "Mantle.h"

@interface Threads : MTLModel <MTLJSONSerializing>

@property (nonatomic) NSArray *activities;
@property (nonatomic, copy) NSString *eventText;
@property (nonatomic, copy) NSString *threadID;
@property (nonatomic) NSArray *interacts;
@property (nonatomic, copy) NSString *totalActivities;

@end
