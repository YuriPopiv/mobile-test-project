//
//  Response.m
//  Accelo
//
//  Created by Kuryliak Maksym on 06.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import "Response.h"

@implementation Response

+ (NSDictionary *)JSONKeyPathsByPropertyKey {
    return @{
        @"affiliations": @"affiliations",
        @"staff": @"staff",
        @"threads": @"threads"
    };
}

+ (NSValueTransformer *)threadsJSONTransformer {
    return [MTLJSONAdapter arrayTransformerWithModelClass:[Threads class]];
}

@end
