//
//  Staff.h
//  Accelo
//
//  Created by Kuryliak Maksym on 06.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Mantle.h"

@interface Staff : MTLModel <MTLJSONSerializing>

@property (nonatomic, copy, readonly) NSString *staffID;
@property (nonatomic, copy, readonly) NSString *firstName;
@property (nonatomic, copy, readonly) NSString *surName;

@end
