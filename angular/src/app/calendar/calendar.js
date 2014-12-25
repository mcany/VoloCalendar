angular.module('calendar', ['security.authorization'])
    .config(['$routeProvider', 'securityAuthorizationProvider', function ($routeProvider, securityAuthorizationProvider) {
        $routeProvider.when('/calendar', {
            templateUrl: 'calendar/calendar.tpl.html',
            controller: 'CalendarCtrl',
            resolve: {
                calendarViewModel: ['security', '$http', 'securityAuthorization', function (security, $http, securityAuthorization) {
                    return securityAuthorization.requireAuthenticatedUser().then(
                        function (value) {
                            return $http.get('/driver/calendar/' + security.currentUser.id).then(
                                function (result) {
                                    return result.data;
                                });
                        });
                }]
            }
        });
    }]);

angular.module('calendar').controller('CalendarCtrl', ['$scope', '$location', 'security', 'calendarViewModel', '$http', 'utilMethods',
    function ($scope, $location, security, calendarViewModel, $http, utilMethods) {
        if (security.isAdmin()) {
            $location.path('/admin/calendar');
        }
        $scope.calendarViewModel = calendarViewModel;

        $scope.monthSelected = function (month) {
            $scope.selectedMonthLight = month;
            var url = '/driver/month/' + security.currentUser.id + '/' + month.beginDate[0] + '-' + month.beginDate[1] + '-' + month.beginDate[2];
            $http.get(url).then(
                function (result) {
                    $scope.selectedMonth = result.data;
                    if ($scope.selectedMonthLight.selectedWeekLight) {
                        $scope.weekSelected($scope.selectedMonthLight.selectedWeekLight);
                    } else {
                        $scope.selectedWeek = null;
                    }
                });
        };

        $scope.setNextWeek = function () {
            var url = '/driver/setNextWeek/' + security.currentUser.id + '/' + $scope.selectedWeek.beginDate[0] + '-' + $scope.selectedWeek.beginDate[1] + '-' + $scope.selectedWeek.beginDate[2];
            $http.get(url).then(function (value) {
                var urlForMonthStatistics = '/driver/month/' + security.currentUser.id + '/'
                    + $scope.selectedMonth.beginDate[0] + '-' + $scope.selectedMonth.beginDate[1] + '-' + $scope.selectedMonth.beginDate[2];
                return $http.get(urlForMonthStatistics).then(
                    function (result) {
                        $scope.selectedMonth = result.data;
                    });
            });
        };

        $scope.setMonth = function () {
            var url = '/driver/setMonth/' + security.currentUser.id + '/' + $scope.selectedWeek.beginDate[0] + '-' + $scope.selectedWeek.beginDate[1] + '-' + $scope.selectedWeek.beginDate[2];
            $http.get(url).then(function (value) {
                var urlForMonthStatistics = '/driver/month/' + security.currentUser.id + '/'
                    + $scope.selectedMonth.beginDate[0] + '-' + $scope.selectedMonth.beginDate[1] + '-' + $scope.selectedMonth.beginDate[2];
                return $http.get(urlForMonthStatistics).then(
                    function (result) {
                        $scope.selectedMonth = result.data;
                    });
            });
        };

        $scope.setYear = function () {
            var url = '/driver/setYear/' + security.currentUser.id + '/' + $scope.selectedWeek.beginDate[0] + '-' + $scope.selectedWeek.beginDate[1] + '-' + $scope.selectedWeek.beginDate[2];
            $http.get(url).then(function (value) {
                var urlForMonthStatistics = '/driver/month/' + security.currentUser.id + '/'
                    + $scope.selectedMonth.beginDate[0] + '-' + $scope.selectedMonth.beginDate[1] + '-' + $scope.selectedMonth.beginDate[2];
                return $http.get(urlForMonthStatistics).then(
                    function (result) {
                        $scope.selectedMonth = result.data;
                    });
            });
        };

        $scope.save = function () {
            var url = '/driver/week/' + security.currentUser.id;
            $http.post(url, $scope.selectedWeek);
            $scope.original = angular.copy($scope.selectedWeek);
        };
        $scope.revertChanges = function () {
            $route.reload();
        };
        $scope.canSave = function () {
            return !angular.equals($scope.selectedWeek, $scope.original);
        };
        $scope.canRevert = function () {
            return !angular.equals($scope.selectedWeek, $scope.original);
        };

        $scope.weekSelected = function (week) {
            $scope.selectedMonthLight.selectedWeekLight = week;
            var url = '/driver/week/' + security.currentUser.id + '/' + week.beginDate[0] + '-' + week.beginDate[1] + '-' + week.beginDate[2];
            $http.get(url).then(
                function (result) {
                    $scope.selectedWeek = result.data;
                    $scope.original = angular.copy($scope.selectedWeek);
                });
        };

        $scope.setColor = function (element, hourStatistics, greenColorShade) {
            if (hourStatistics.selected) {
                element.css('backgroundColor', 'blue');
            } else {
                element.css('backgroundColor', greenColorShade);
            }

            if (!hourStatistics.enabled) {
                element.css('opacity', 0.5);
            }
        };

        $scope.isSelected = function (week) {
            return $scope.selectedWeek && angular.equals(week.beginDate, $scope.selectedWeek.beginDate);
        };

        $scope.isAnyWeekSelected = function () {
            return $scope.selectedWeek;
        };

        $scope.getMonthName = function (month) {
            return utilMethods.get('monthNames')[month];
        };

        $scope.getWeekDayName = function (date) {
            var dateObj = new Date(date[0] + '/' + date[1] + '/' + date[2]);
            var dayOfWeek = dateObj.getDay();
            return utilMethods.get('weekDayNames')[dayOfWeek];
        };
    }])
    .directive('selectHour', [ 'utilMethods', '$parse', function (utilMethods, $parse) {
        return {
            scope: false,
            link: function (scope, element, attrs) {
                var modelGetter = $parse(attrs.selectHour);
                var hourStatistics = modelGetter(scope);
                if (hourStatistics.enabled) {
                    element.removeAttr('disabled');
                } else {
                    element.attr('disabled', 'disabled');
                }
                var greenColorShade = utilMethods.getGreenColorShade(hourStatistics.requiredDriverCount);
                scope.setColor(element, hourStatistics, greenColorShade);

                var modelSetter = modelGetter.assign;
                element.bind('click', function (event) {
                    scope.$apply(function () {
                        event.preventDefault();

                        var newValue = modelGetter(scope);
                        if (!newValue.enabled) {
                            return;
                        }
                        if (newValue.selected) {
                            scope.selectedMonth.doneHours--;
                            if (scope.selectedMonth.plannedHours > 0) {
                                scope.selectedMonth.diffHours++;
                            }
                            scope.selectedWeek.selectedHoursCount--;
                        } else {
                            scope.selectedMonth.doneHours++;
                            if (scope.selectedMonth.plannedHours > 0) {
                                scope.selectedMonth.diffHours--;
                            }
                            scope.selectedWeek.selectedHoursCount++;
                        }
                        newValue.selected = !newValue.selected;
                        modelSetter(scope, newValue);
                        scope.setColor(element, newValue, greenColorShade);
                    });
                });
            }
        };
    }]);