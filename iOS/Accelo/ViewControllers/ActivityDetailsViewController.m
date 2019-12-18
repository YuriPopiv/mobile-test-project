//
//  ActivityDetailsViewController.m
//  Accelo
//
//  Created by Kuryliak Maksym on 09.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import "ActivityDetailsViewController.h"

@interface ActivityDetailsViewController ()

@end

@implementation ActivityDetailsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setupTextView];
    self.activityIndicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleLarge];
    self.activityIndicator.frame = CGRectMake(self.view.center.x, self.view.center.y, 0, 0);
    [self getActivityDetails:self.activity.activityID];
}

- (void)setupTextView {
    self.activityBodyTextView.userInteractionEnabled = NO;
    [self.activityBodyTextView.layer setBorderColor:[[[UIColor lightGrayColor] colorWithAlphaComponent:0.2] CGColor]];
    [self.activityBodyTextView.layer setBorderWidth:1.0];
    self.activityBodyTextView.layer.cornerRadius = 5;
    self.activityBodyTextView.clipsToBounds = YES;
}

- (void)getActivityDetails:(NSString *)activityID {
    [self.activityIndicator startAnimating];
    [self.view addSubview:self.activityIndicator];
    self.authorLabel.hidden = YES;
    self.subjectLabel.hidden = YES;
    self.dateLabel.hidden = YES;
    self.activityBodyTextView.hidden = YES;
    [[APIManager sharedManager] GET:[NSString stringWithFormat:@"%@%@?_fields=id,html_body,interacts", kActivityDetails, activityID]
                         parameters:nil progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
                            [self.activityIndicator stopAnimating];
                            [self.activityIndicator removeFromSuperview];
                            NSString *htmlBody = [[responseObject objectForKey:@"response"] objectForKey:@"html_body"];
                            self.authorLabel.hidden = NO;
                            self.subjectLabel.hidden = NO;
                            self.dateLabel.hidden = NO;
                            self.activityBodyTextView.hidden = NO;
                            [self setupData:htmlBody];
                            /// if API request to accelo is successful - try to upload activities if any
                            UploadManager *uploadManager = [[UploadManager alloc] init];
                            [uploadManager uploadActivity];
                        } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
                            NSLog(@"%@", error);
                            [self.activityIndicator stopAnimating];
                            [self.activityIndicator removeFromSuperview];
                        }];
}

- (void)setupData:(NSString *)htmlBody {
    Interact *fromInteract;
    for (int i = 0; i < self.activity.interacts.count; i++) {
        Interact *interact = self.activity.interacts[i];
        if ([interact.type isEqualToString:@"from"]) {
            fromInteract = interact;
        }
    }
    self.authorLabel.text = fromInteract.ownerName;
    self.subjectLabel.text = self.activity.subject;
    ConvertTime *convertTime = [[ConvertTime alloc] init];
    self.dateLabel.text = [convertTime convertTimestampToString:[self.activity.dateLogged doubleValue]];
    NSString *htmlString = htmlBody;
    NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc]
                                                   initWithData:[htmlString dataUsingEncoding:NSUnicodeStringEncoding]
                                                   options:@{ NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType }
                                                   documentAttributes:nil error:nil];
    self.activityBodyTextView.text = @"";
    self.activityBodyTextView.attributedText = attributedString;
    self.activityBodyTextView.textAlignment = NSTextAlignmentJustified;
}

@end
