//
//  ActivityViewController.h
//  Accelo
//
//  Created by Kuryliak Maksym on 06.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Activity.h"
#import "Threads.h"
#import "Constants.h"
#import "APIManager.h"
#import "Request.h"
#import "ActivityTableViewCell.h"

@interface ActivityViewController : UIViewController {
    int currentPage;
    BOOL isNewDataLoading;
}

@property(strong, nonatomic) NSString *token;
@property(strong, nonatomic) NSMutableArray *threads;
@property(strong, nonatomic) UIRefreshControl *refreshController;
@property(nonatomic, strong) UIActivityIndicatorView *activityIndicator;

@end
