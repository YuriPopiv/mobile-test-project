//
//  ActivityTableViewCell.h
//  Accelo
//
//  Created by Kuryliak Maksym on 06.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Activity.h"

@interface ActivityTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UILabel *sender;
@property (weak, nonatomic) IBOutlet UILabel *dateLogged;
@property (weak, nonatomic) IBOutlet UILabel *subject;
@property (weak, nonatomic) IBOutlet UILabel *body;
@property (weak, nonatomic) IBOutlet UILabel *people;

-(void)configure:(Activity *)activity;

@end
