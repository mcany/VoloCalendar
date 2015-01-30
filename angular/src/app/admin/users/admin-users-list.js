angular.module('admin-users-list', [
    'services.crud',
    'services.i18nNotifications',
    'services.utilMethods'
])

    .controller('UsersListCtrl', ['$scope', 'crudListMethods', 'i18nNotifications', '$http', 'mongolabResource', 'utilMethods'
        , function ($scope, crudListMethods, i18nNotifications, $http, mongolabResource, utilMethods) {
            angular.extend($scope, crudListMethods('/admin/users'));

            $scope.changePageVolume = function () {
                $scope.itemsPerPage = parseInt($scope.itemsPerPageStr, 10);
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
                utilMethods.save('pagingData', $scope);
                $scope.edit(id);
            };

            $scope.pageChanged = function () {
                $http.post('/admin/users/pagination'
                    , {sortingField: $scope.sortingField, reverse: $scope.reverse, currentPage: $scope.currentPage, itemsPerPage: $scope.itemsPerPage, keyword: $scope.keyword
                    }).
                    success(function (data, status, headers, config) {
                        result = [];
                        for (var i = 0; i < data.items.length; i++) {
                            var myResource = mongolabResource('users');
                            result.push(new myResource(data.items[i]));
                        }
                        $scope.users = result;
                        $scope.totalItems = data.allCount;
                    }).
                    error(function (data, status, headers, config) {
                        $scope.users = null;
                        $scope.totalItems = 0;
                    });
            };

            $scope.sort = function (sortingField) {
                if ($scope.sortingField != sortingField) {
                    $scope.sortingField = sortingField;
                    $scope.reverse = false;
                } else {
                    $scope.reverse = !$scope.reverse;
                }
                $scope.currentPage = 1;
                $scope.pageChanged();
            };

            $scope.search = function () {
                if ($scope.canSearch()) {
                    $scope.currentPage = 1;
                    $scope.pageChanged();
                }
            };

            $scope.canSearch = function () {
                return $scope.keyword;
            };

            $scope.maxSize = 5;
            var cache = utilMethods.get('pagingData');

            if (cache == null) {
                $scope.currentPage = 1;
                $scope.sortingField = 'deleted';
                $scope.reverse = false;
                $scope.itemsPerPage = 5;
                $scope.users = null;
                $scope.keyword = null;
            } else {
                $scope.currentPage = cache.currentPage;
                $scope.sortingField = cache.sortingField;
                $scope.reverse = cache.reverse;
                $scope.itemsPerPage = cache.itemsPerPage;
                $scope.keyword = cache.keyword;
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
