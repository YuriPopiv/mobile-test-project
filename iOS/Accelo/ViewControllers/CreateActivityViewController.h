//
//  CreateActivityViewController.h
//  Accelo
//
//  Created by Kuryliak Maksym on 09.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <ReactiveObjC/ReactiveObjC.h>
#import <SystemConfiguration/SystemConfiguration.h>
#import <Realm/Realm.h>
#import "APIManager.h"
#import "Constants.h"
#import "Request.h"
#import "Reachability.h"
#import "ActivityToSave.h"
#import "UploadManager.h"

@interface CreateActivityViewController : UIViewController

@property (weak, nonatomic) IBOutlet UIButton *sendButton;
@property (weak, nonatomic) IBOutlet UITextField *subjectTextField;
@property (weak, nonatomic) IBOutlet UITextView *bodyTextView;

@property(nonatomic, strong) UIActivityIndicatorView *activityIndicator;

@end
