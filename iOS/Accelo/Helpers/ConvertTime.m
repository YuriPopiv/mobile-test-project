//
//  ConvertTime.m
//  Accelo
//
//  Created by Kuryliak Maksym on 06.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import "ConvertTime.h"

@implementation ConvertTime

- (NSString *)convertTimestampToString:(double)timestamp {
    NSDate *date = [NSDate dateWithTimeIntervalSince1970: timestamp];
    NSDateFormatter *dateformatter = [[NSDateFormatter alloc] init];
    [dateformatter setDateFormat: @"dd MMM yyyy"];
    NSString *dateString = [dateformatter stringFromDate: date];
    return dateString;
}

@end
