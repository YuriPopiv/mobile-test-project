//
//  Activity.h
//  Accelo
//
//  Created by Kuryliak Maksym on 06.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Mantle.h"
#import "Interact.h"

@interface Activity : MTLModel <MTLJSONSerializing>

@property (nonatomic) NSNumber *confidential;
@property (nonatomic) NSString *dateLogged;
@property (nonatomic) NSArray *interacts;
@property (nonatomic) NSString *activityID;

@property (nonatomic) NSString *previewBody;
@property (nonatomic) NSString *subject;
@property (nonatomic) NSString *eventText;

@end
