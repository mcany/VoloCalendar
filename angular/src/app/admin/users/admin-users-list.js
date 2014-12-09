angular.module('admin-users-list', [
    'services.crud',
    'services.i18nNotifications',
    'services.utilMethods'
])

    .controller('UsersListCtrl', ['$scope', 'crudListMethods', 'i18nNotifications', '$http', 'mongolabResource', 'utilMethods'
        , function ($scope, crudListMethods, i18nNotifications, $http, mongolabResource, utilMethods) {
            angular.extend($scope, crudListMethods('/admin/users'));

            $scope.changePageVolume = function(){
                $scope.itemsPerPage = parseInt($scope.itemsPerPageStr, 10);
                $scope.pageChanged();
            }

            $scope.remove = function (user, $index, $event) {
                // Don't let the click bubble up to the ng-click on the enclosing div, which will try to trigger
                // an edit of this item.
                $event.stopPropagation();

                // Remove this user
                user.$remove(function () {
                    // It is gone from the DB so we can remove it from the local list too
                    $scope.users.splice($index, 1);
                    $scope.totalItems = $scope.totalItems - 1;
                    i18nNotifications.pushForCurrentRoute('crud.user.remove.success', 'success', {id: user.$id()});
                }, function () {
                    i18nNotifications.pushForCurrentRoute('crud.user.remove.error', 'error', {id: user.$id()});
                });
            };

            $scope.editUser = function (id) {
                utilMethods.save('pagingData', $scope);
                $scope.edit(id);
            };

            $scope.pageChanged = function () {
                $http.post('/databases/scrumdb/collections/users/pagination'
                    , {sortingField: $scope.sortingField
                        , reverse: $scope.reverse
                        , beginIndex: (($scope.currentPage - 1) * $scope.itemsPerPage + 1)
                        , maxNumber: $scope.itemsPerPage
                        , keyword: $scope.keyword
                    }).
                    success(function (data, status, headers, config) {
                        result = [];
                        for (var i = 0; i < data.length; i++) {
                            var myResource = mongolabResource('users');
                            result.push(new myResource(data[i]));
                        }
                        $scope.users = result;
                    }).
                    error(function (data, status, headers, config) {
                        $scope.users = null;
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
                $scope.currentPage = 1;
                $scope.pageChanged();
            };

            $scope.canSearch = function () {
                return $scope.keyword != null;
            };

            var cache = utilMethods.get('pagingData');

            $scope.maxSize = 5;
            if (cache == null) {
                $http.get('/databases/scrumdb/collections/users/count').
                    success(function (data, status, headers, config) {
                        $scope.totalItems = data;
                    }).
                    error(function (data, status, headers, config) {
                        $scope.totalItems = 0;
                    });
                $scope.currentPage = 1;
                $scope.sortingField = null;
                $scope.reverse = false;
                $scope.itemsPerPage = 5;
                $scope.users = null;
                $scope.keyword = null;
            } else {
                $scope.totalItems = cache.totalItems;
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
