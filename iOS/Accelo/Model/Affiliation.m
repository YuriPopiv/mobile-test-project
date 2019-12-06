//
//  Affiliation.m
//  Accelo
//
//  Created by Kuryliak Maksym on 06.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import "Affiliation.h"

@implementation Affiliation

+ (NSDictionary *)JSONKeyPathsByPropertyKey {
    return @{
        @"affiliationsID": @"id",
        @"email": @"email",
        @"mobile": @"mobile"
    };
}

@end
