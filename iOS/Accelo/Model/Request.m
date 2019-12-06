//
//  Request.m
//  Accelo
//
//  Created by Kuryliak Maksym on 06.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import "Request.h"

@implementation Request

+ (NSDictionary *)JSONKeyPathsByPropertyKey {
    return @{
        @"meta": @"meta",
        @"response": @"response"
    };
}

@end
