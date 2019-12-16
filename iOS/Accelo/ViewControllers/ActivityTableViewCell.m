//
//  ActivityTableViewCell.m
//  Accelo
//
//  Created by Kuryliak Maksym on 06.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import "ActivityTableViewCell.h"
#import "ConvertTime.h"

@implementation ActivityTableViewCell

const int secondsInMinute = 60;
const int secondsInDay = 86400;
const int minutesInDay = 1440;

- (void)awakeFromNib {
    [super awakeFromNib];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
}

- (void)configure:(Activity *)activity {
    Interact *fromInteract;
    NSMutableArray *peoples = [[NSMutableArray alloc]init];
    for (int i = 0; i < activity.interacts.count; i++) {
        Interact *interact = activity.interacts[i];
        if ([interact.type isEqualToString:@"from"]) {
            fromInteract = interact;
        }
        if ([interact.type isEqualToString:@"to"] || [interact.type isEqualToString:@"cc"]) {
            [peoples addObject:interact.ownerName];
        }
    }
    [self calculateDate: activity];
    self.sender.text = fromInteract.ownerName;
    self.people.text = [peoples componentsJoinedByString:@", "];
    self.subject.text = activity.subject;
    self.body.text = activity.previewBody;
    if ([activity.confidential intValue] == 1) {
        self.subject.text = @"Confidential";
        self.people.hidden = YES;
        self.body.hidden = YES;
    }
}

- (void)calculateDate:(Activity *)activity {
    NSDate *nowDate = [NSDate date];
    NSDate *date = [NSDate dateWithTimeIntervalSince1970:[activity.dateLogged doubleValue]];
    NSTimeInterval secondsBetween = [nowDate timeIntervalSinceDate:date];
    int numberOfMinutes = secondsBetween / secondsInMinute;
    int numberOfDays = secondsBetween / secondsInDay;
    if (numberOfMinutes <= 30) {
        self.dateLogged.text = @"Now";
    }
    if (numberOfMinutes > 30 && numberOfMinutes < minutesInDay) {
        self.dateLogged.text = @"Today";
    }
    if (numberOfDays >= 1) {
        ConvertTime *convertTime = [[ConvertTime alloc] init];
        self.dateLogged.text = [convertTime convertTimestampToString:[activity.dateLogged doubleValue]];
    }
}


@end
