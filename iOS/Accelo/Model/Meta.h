//
//  Meta.h
//  Accelo
//
//  Created by Kuryliak Maksym on 06.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Mantle.h"

@interface Meta : MTLModel <MTLJSONSerializing>

@property (nonatomic, copy, readonly) NSString *message;
@property (nonatomic, copy, readonly) NSString *moreInfo;
@property (nonatomic, copy, readonly) NSString *status;

@end
