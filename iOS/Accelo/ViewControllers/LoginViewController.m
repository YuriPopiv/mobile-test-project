//
//  ViewController.m
//  Accelo
//
//  Created by Kuryliak Maksym on 04.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import "LoginViewController.h"

@interface LoginViewController ()

@property (weak, nonatomic) IBOutlet UITextField *deploymentTextField;
@property (weak, nonatomic) IBOutlet UIButton *loginButton;

@end

@implementation LoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    NSString *deploymentName = self.deploymentTextField.text;
    self.authURL = [NSString stringWithFormat:@"%@%@", deploymentName, kAuthUrl];
    [self login];
}

-(void)login {
    self.loginButton.rac_command = [[RACCommand alloc] initWithSignalBlock:^(id _) {
        NSURL *url = [NSURL URLWithString:self.authURL];
        NSURLRequest *request = [NSURLRequest requestWithURL:url];
        
        self.webView = [[WKWebView alloc] initWithFrame:self.view.frame];
        [self.webView loadRequest:request];
        self.webView.frame = CGRectMake(self.view.frame.origin.x, self.view.frame.origin.y, self.view.frame.size.width, self.view.frame.size.height);
        self.webView.navigationDelegate = self;
        [self.view addSubview:self.webView];
        return [RACSignal empty];
    }];
}

- (void)webView:(WKWebView *)webView decidePolicyForNavigationAction:(WKNavigationAction *)navigationAction
                                        decisionHandler:(void (^)(WKNavigationActionPolicy))decisionHandler {
    if (navigationAction.request.URL) {
        /// check if callback URL contains code and if yes get it and pass to the getToken method
        if (![navigationAction.request.URL.resourceSpecifier containsString:@"?code="]) {
            decisionHandler(WKNavigationActionPolicyAllow);
        } else {
            decisionHandler(WKNavigationActionPolicyAllow);
            NSString *code = [navigationAction.request.URL.resourceSpecifier substringFromIndex:6];
            ///encode client id and client secret in base64
            NSData *credentialsInData = [[NSString stringWithFormat:@"%@:%@", kClientId, kClientSecret] dataUsingEncoding:NSUTF8StringEncoding];
            NSString *encodedCredentials = [credentialsInData base64EncodedStringWithOptions:0];
            [self getToken:code encodedCredentials:encodedCredentials];
        }
    }
}

- (void)getToken:(NSString *)code encodedCredentials:(NSString *)encodedCredentials {
    [[APIManager sharedManager] setCredentialsToHeader: encodedCredentials];
    [[APIManager sharedManager] POST:[NSString stringWithFormat:@"%@%@", kTokenUrl, code] parameters:nil progress:nil
    success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSString *token = [responseObject valueForKeyPath:@"access_token"];
        if (token) {
            /// save auth token to userdefaults for access to it all over the app (later should use keychain for security)
            [[NSUserDefaults standardUserDefaults] setObject: token forKey:@"token"];
            [self.webView removeFromSuperview];
            UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
            ActivityViewController *activityViewController = (ActivityViewController *)
            [storyboard instantiateViewControllerWithIdentifier:@"ActivityViewController"];
            [self.navigationController pushViewController:activityViewController animated:NO];
        }
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSLog(@"%@", error.description);
    }];
}


@end
