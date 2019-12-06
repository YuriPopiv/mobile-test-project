//
//  APIManager.m
//  Accelo
//
//  Created by Kuryliak Maksym on 06.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import "APIManager.h"
#import "AFNetworkActivityIndicatorManager.h"
#import "Constants.h"

@implementation APIManager

#pragma mark - Methods

- (void)setCredentialsToHeader:(id)credentials {
    [self.requestSerializer clearAuthorizationHeader];
    [self.requestSerializer setValue:[NSString stringWithFormat:@"Basic %@", credentials] forHTTPHeaderField:@"Authorization"];
}

- (void)setAuthorizationToken:(id)token {
    [self.requestSerializer clearAuthorizationHeader];
    [self.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token] forHTTPHeaderField:@"Authorization"];
}

#pragma mark - Initialization

- (id)initWithBaseURL:(NSURL *)url
{
    self = [super initWithBaseURL:url];
    if(!self)
        return nil;

    self.requestSerializer = [AFJSONRequestSerializer serializer];

    [[AFNetworkActivityIndicatorManager sharedManager] setEnabled:YES];

    return self;
}

#pragma mark - Singleton Methods

+ (APIManager *)sharedManager
{
    static dispatch_once_t pred;
    static APIManager *_sharedManager = nil;

    dispatch_once(&pred, ^{ _sharedManager = [[self alloc] initWithBaseURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@", kBaseUrl]]]; });
    return _sharedManager;
}

@end
