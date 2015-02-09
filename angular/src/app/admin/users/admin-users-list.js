angular.module('admin-users-list', [
    'services.crud',
    'services.i18nNotifications',
    'services.utilMethods'
])

    .controller('UsersListCtrl', ['$scope', 'crudListMethods', 'i18nNotifications', '$http', 'mongolabResource', 'utilMethods'
        , function ($scope, crudListMethods, i18nNotifications, $http, mongolabResource, utilMethods) {
            angular.extend($scope, crudListMethods('/admin/users'));

            $scope.changePageVolume = function () {
                $scope.params.itemsPerPage = parseInt($scope.itemsPerPageStr, 10);
                $scope.pageChanged();
            }

            $scope.remove = function (user, $index, $event) {
                // Don't let the click bubble up to the ng-click on the enclosing div, which will try to trigger
                // an edit of this item.
                $event.stopPropagation();
                user.deleted = true;
                user.$saveOrUpdate(null, function () {
                    //i18nNotifications.pushForCurrentRoute('crud.user.remove.success', 'success', {id: user.$id()});
                }, null, function () {
                    i18nNotifications.pushForCurrentRoute('crud.user.remove.error', 'error', {id: user.$id()});
                });
            };

            $scope.select = function (user, $index, $event) {
                // Don't let the click bubble up to the ng-click on the enclosing div, which will try to trigger
                // an edit of this item.
                $event.stopPropagation();
                var userListDialog = utilMethods.get('userListDialog');
                userListDialog.close(user.$id());
            };

            $scope.restore = function (user, $index, $event) {
                // Don't let the click bubble up to the ng-click on the enclosing div, which will try to trigger
                // an edit of this item.
                $event.stopPropagation();
                user.deleted = false;
                user.$saveOrUpdate(null, function () {
                    //i18nNotifications.pushForCurrentRoute('crud.user.restore.success', 'success', {id: user.$id()});
                }, null, function () {
                    i18nNotifications.pushForCurrentRoute('crud.user.restore.error', 'error', {id: user.$id()});
                });
            };

            $scope.editUser = function (id) {
                utilMethods.save('pagingData', $scope.params);
                $scope.edit(id);
            };

            $scope.addUser = function (id) {
                utilMethods.save('pagingData', $scope.params);
                $scope.new();
            };

            $scope.pageChanged = function () {
                $http.post('/admin/users/pagination'
                    , {sortingField: $scope.params.sortingField, reverse: $scope.params.reverse
                        , currentPage: $scope.params.currentPage, itemsPerPage: $scope.params.itemsPerPage
                        , keyword: $scope.params.keyword
                    }).
                    success(function (data, status, headers, config) {
                        var result = [];
                        for (var i = 0; i < data.items.length; i++) {
                            var myResource = mongolabResource('users');
                            result.push(new myResource(data.items[i]));
                        }
                        $scope.params.users = result;
                        $scope.params.totalItems = data.allCount;
                    }).
                    error(function (data, status, headers, config) {
                        $scope.params.users = null;
                        $scope.params.totalItems = 0;
                    });
            };

            $scope.sort = function (sortingField) {
                if ($scope.params.sortingField != sortingField) {
                    $scope.params.sortingField = sortingField;
                    $scope.params.reverse = false;
                } else {
                    $scope.params.reverse = !$scope.params.reverse;
                }
                $scope.params.currentPage = 1;
                $scope.pageChanged();
            };

            $scope.search = function () {
                $scope.params.currentPage = 1;
                $scope.pageChanged();
            };

            $scope.params = {};
            $scope.params.maxSize = 5;
            var cache = utilMethods.get('pagingData');

            if (cache == null) {
                $scope.params.totalItems = 0;
                $scope.params.currentPage = 1;
                $scope.params.sortingField = 'deleted';
                $scope.params.reverse = false;
                $scope.params.itemsPerPage = 5;
                $scope.params.keyword = null;
                $scope.params.users = null;
            } else {
                $scope.params.totalItems = cache.totalItems;
                $scope.params.currentPage = cache.currentPage;
                $scope.params.sortingField = cache.sortingField;
                $scope.params.reverse = cache.reverse;
                $scope.params.itemsPerPage = cache.itemsPerPage;
                $scope.params.keyword = cache.keyword;
                $scope.params.users = null;
            }
            $scope.pageChanged();
        }])
    .directive('columnHeader', function () {
        return {
            restrict: 'A',
            templateUrl: 'admin/users/column-header.tpl.html',
            scope: {
                sortingField: '@',
                reverse: '@',
                sort: '&'
            },
            link: function (scope, elm, attrs) {
                scope.columnField = attrs.columnField;
                scope.columnHeader = attrs.columnHeader;
                scope.headerClick = function () {
                    scope.sort({field: scope.columnField});
                };
            }
        };
    });
