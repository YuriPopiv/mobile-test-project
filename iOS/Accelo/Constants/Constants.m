//
//  Constants.m
//  Accelo
//
//  Created by Kuryliak Maksym on 06.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import "Constants.h"

@implementation Constants

/// BaseURL
NSString *kBaseUrl = @"https://nk-company-2019.api.accelo.com";

/// AuthURLs
NSString *kAuthUrl = @"oauth2/v0/authorize?response_type=code&client_id=d30447b72b@nk-company-2019.accelo.com&scope=write(all)";
NSString *kTokenUrl = @"/oauth2/v0/token?grant_type=authorization_code&code=";

/// SearchURL
NSString *kSearchUrl = @"/api/v0/activities/threads?_fields=interacts,date_logged,preview_body&q=";

/// ActivityURLs
NSString *kActivityUrl = @"/api/v0/activities/threads?_fields=interacts,date_logged,preview_body";
NSString *kActivityDetails = @"api/v0/activities/";
NSString *kCreateActivity = @"/api/v0/activities";

/// clientData
NSString *kClientId = @"d30447b72b@nk-company-2019.accelo.com";
NSString *kClientSecret = @"qH9q~noBJRegpEtKHjF2S8Czw98OJDYx";

@end
