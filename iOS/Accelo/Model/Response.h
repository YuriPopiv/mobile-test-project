//
//  Response.h
//  Accelo
//
//  Created by Kuryliak Maksym on 06.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Mantle.h"
#import "Affiliation.h"
#import "Staff.h"
#import "Threads.h"

@interface Response : MTLModel <MTLJSONSerializing>

@property (nonatomic, copy, readonly) NSArray<Affiliation *> *affiliations;
@property (nonatomic, copy, readonly) NSArray<Staff *> *staff;
@property (nonatomic, copy, readonly) NSMutableArray<Threads *> *threads;

@end
