//
//  ActivityViewController.m
//  Accelo
//
//  Created by Kuryliak Maksym on 06.12.2019.
//  Copyright © 2019 Wise-Engineering. All rights reserved.
//

#import "ActivityViewController.h"

@interface ActivityViewController () <UITableViewDelegate, UITableViewDataSource, UISearchBarDelegate>

@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UISearchBar *searchBar;

@end

@implementation ActivityViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.threads = [[NSMutableArray alloc] init];
    NSString *authToken = [[NSUserDefaults standardUserDefaults] stringForKey:@"token"];
    self.token = authToken;
    [self setupNavigationBar];
    [self setupTableView];
    [self getActivities];
}

- (void)setupNavigationBar {
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    self.navigationItem.hidesBackButton = YES;
    self.navigationItem.title = @"Activities";
    UIBarButtonItem *anotherButton = [[UIBarButtonItem alloc] initWithTitle:@"Add" style:UIBarButtonItemStylePlain target:nil action:nil];
    anotherButton.rac_command = [[RACCommand alloc] initWithSignalBlock:^(id _) {
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        CreateActivityViewController *createActivityViewController = (CreateActivityViewController *)
        [storyboard instantiateViewControllerWithIdentifier:@"CreateActivityViewController"];
        [self.navigationController pushViewController:createActivityViewController animated:NO];
        return [RACSignal empty];
    }];
    self.navigationItem.rightBarButtonItem = anotherButton;
}

- (void)setupTableView {
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    self.searchBar.delegate = self;
    _activityIndicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleLarge];
    _activityIndicator.frame = CGRectMake(self.view.center.x, self.view.center.y, 0, 0);
    
    currentPage = 0;
    
    [self.tableView registerNib:[UINib nibWithNibName:@"ActivityTableViewCell" bundle:nil] forCellReuseIdentifier:@"ActivityTableViewCell"];
    self.refreshController = [[UIRefreshControl alloc] init];
    [self.refreshController addTarget:self action:@selector(handleRefresh:) forControlEvents:UIControlEventValueChanged];
    [self.tableView addSubview:self.refreshController];
}

- (void)getActivities {
    [self.activityIndicator startAnimating];
    [self.view addSubview:self.activityIndicator];
    [[APIManager sharedManager] setAuthorizationToken:self.token];
    [[APIManager sharedManager] GET:[NSString stringWithFormat:@"%@&_page=%d&_limit=20", kActivityUrl, currentPage] parameters:nil progress:nil
    success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSLog(@"%@", responseObject);
        Request *request = [MTLJSONAdapter modelOfClass:Request.class fromJSONDictionary:responseObject error:nil];
        [self.activityIndicator stopAnimating];
        [self.activityIndicator removeFromSuperview];
        if ([request.response.threads count]) {
            [self.threads addObjectsFromArray:request.response.threads];
            [self.tableView reloadData];
        }
        self->isNewDataLoading = NO;
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSLog(@"%@", error);
        [self.activityIndicator stopAnimating];
        [self.activityIndicator removeFromSuperview];
    }];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return self.threads.count;
}

- (NSInteger)tableView:(nonnull UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    NSArray *activities = [self.threads[section] activities];
    return activities.count;
}

- (nonnull UITableViewCell *)tableView:(nonnull UITableView *)tableView cellForRowAtIndexPath:(nonnull NSIndexPath *)indexPath {
    ActivityTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"ActivityTableViewCell" forIndexPath:indexPath];
    Activity *activity = [self.threads[indexPath.section] activities][indexPath.row];
    [cell configure:activity];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    Activity *activity = [self.threads[indexPath.section] activities][indexPath.row];
    if ([activity.confidential intValue] == 1) {
        UIAlertController* alert = [UIAlertController alertControllerWithTitle:@"Alert"
                                   message:@"This activity is confidential."
                                   preferredStyle:UIAlertControllerStyleAlert];

        UIAlertAction* defaultAction = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault
                                       handler:^(UIAlertAction * action) {}];

        [alert addAction:defaultAction];
        [self presentViewController:alert animated:YES completion:nil];
    } else {
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        ActivityDetailsViewController *activityDetailsViewController = (ActivityDetailsViewController *)
        [storyboard instantiateViewControllerWithIdentifier:@"ActivityDetailsViewController"];
        activityDetailsViewController.activity = activity;
        [self.navigationController pushViewController:activityDetailsViewController animated:NO];
    }
}

- (void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath {
    if(indexPath.section + 1 == self.threads.count && self.threads.count >= 20) {
        /// reach bottom of table view and should upload new data if any
        if (!isNewDataLoading) {
            self->isNewDataLoading = YES;
            self->currentPage ++;
            [self getActivities];
        }
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 220;
}

#pragma mark - SearchBar

- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText {
    [NSObject cancelPreviousPerformRequestsWithTarget: self];
    [self performSelector:@selector(searchActivityWithText:) withObject:searchText afterDelay:0.5];
}

- (void)searchActivityWithText:(NSString *)searchText {
    if (searchText.length > 0) {
        NSString *holisticSearchText = [searchText stringByReplacingOccurrencesOfString:@" " withString:@"%20"];
        [[APIManager sharedManager] GET:[NSString stringWithFormat:@"%@%@", kSearchUrl, holisticSearchText] parameters:nil progress:nil
        success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
            NSLog(@"%@", responseObject);
            Request *requestTest = [MTLJSONAdapter modelOfClass:Request.class fromJSONDictionary:responseObject error:nil];
            if ([requestTest.response.threads count]) {
                self.threads = requestTest.response.threads;
                [self.tableView reloadData];
            }
        } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
            NSLog(@"%@", error);
        }];
    } else {
        /// if search text is empty, remove searched activites from array, resign search bar and get default activities list
        [self.threads removeAllObjects];
        [self.searchBar resignFirstResponder];
        [self getActivities];
    }
}

#pragma mark - Handle Refresh Method

- (void)handleRefresh : (id)sender {
    self->currentPage = 0;
    [self.threads removeAllObjects];
    [self.tableView reloadData];
    [self.refreshController endRefreshing];
    [self getActivities];
}

@end
