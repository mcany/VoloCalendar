angular.module('admin-calendar', ['security.authorization'])
    .config(['$routeProvider', 'securityAuthorizationProvider', function ($routeProvider, securityAuthorizationProvider) {
        $routeProvider.when('/admin/calendar', {
            templateUrl: 'admin/calendar/admin-calendar.tpl.html',
            controller: 'AdminCalendarCtrl',
            resolve: {
                calendarViewModel: ['security', '$http', 'securityAuthorization', function (security, $http, securityAuthorization) {
                    return securityAuthorization.requireAdminUser().then(
                        function (value) {
                            return $http.get('/admin/schedule/calendar').then(
                                function (result) {
                                    return result.data;
                                });
                        });
                }]
            }
        });
    }]);

angular.module('admin-calendar').controller('AdminCalendarCtrl'
    , ['$scope', '$route', '$location', 'security', 'calendarViewModel', '$http', 'utilMethods', '$modal',
    function ($scope, $route, $location, security, calendarViewModel, $http, utilMethods, $modal) {
        $scope.calendarViewModel = calendarViewModel;

        $scope.monthSelected = function (month) {
            var url = '/admin/schedule/month/' + month.beginDate[0] + '-' + month.beginDate[1] + '-' + month.beginDate[2];
            $http.get(url).then(
                function (result) {
                    $scope.selectedMonth = result.data;
                    $scope.selectedWeek = null;
                    $scope.setNullSelectedDay();
                });
        };

        $scope.save = function () {
            var url = '/admin/schedule/detailedAdminDay';
            $http.post(url, $scope.selectedDay);
            $scope.originalSelectedDay = angular.copy($scope.selectedDay);
            $scope.originalSelectedDayLight = angular.copy($scope.selectedDayLight);
        };
        $scope.revertChanges = function () {
            $route.reload();
        };
        $scope.canSave = function () {
            return !angular.equals($scope.selectedDay, $scope.originalSelectedDay);
        };
        $scope.canRevert = function () {
            return !angular.equals($scope.selectedDay, $scope.originalSelectedDay);
        };

        $scope.weekSelected = function (week) {
            var url = '/admin/schedule/week/' + week.beginDate[0] + '-' + week.beginDate[1] + '-' + week.beginDate[2];
            $http.get(url).then(
                function (result) {
                    $scope.selectedWeek = result.data;
                    $scope.setNullSelectedDay();
                });
        };

        $scope.daySelected = function (day) {
            $scope.setNullSelectedDay();
            $scope.selectedDayLight = day;
            $scope.originalSelectedDayLight = angular.copy(day);
            var url = '/admin/schedule/day/' + day.date[0] + '-' + day.date[1] + '-' + day.date[2];
            $http.get(url).then(
                function (result) {
                    $scope.selectedDay = result.data;
                    $scope.selectedDay.plannedHours = day.plannedHours;
                    $scope.selectedDay.doneHours = day.doneHours;
                    $scope.originalSelectedDay = angular.copy($scope.selectedDay);
                });
        };

        $scope.setColor = function (element, hourStatistics, greenColorShade) {
            element.css('backgroundColor', greenColorShade);

            if (!hourStatistics.enabled) {
                element.css('opacity', 0.5);
            }
        };

        $scope.getBackgroundColorStyle = function (hourStatistics) {
            var greenColorShade = utilMethods.getGreenColorShadeWithMaxDefined(hourStatistics.doneHours, hourStatistics.plannedHours);
            var style = {'background-color' : greenColorShade};

            if (!hourStatistics.enabled) {
                angular.extend(style, {'opacity' : '0.5'});
            }
            return style;
        };

        $scope.setColorSimple = function (element, hourStatistics) {
            if (hourStatistics.selected) {
                element.css('backgroundColor', 'blue');
            } else {
                element.css('backgroundColor', 'white');
            }

            if (!hourStatistics.enabled) {
                element.css('opacity', 0.5);
            }
        };

        $scope.isSelectedMonth = function (month) {
            return $scope.selectedMonth && angular.equals(month.beginDate, $scope.selectedMonth.beginDate);
        };

        $scope.isSelectedWeek = function (week) {
            return $scope.selectedWeek && angular.equals(week.beginDate, $scope.selectedWeek.beginDate);
        };

        $scope.isSelectedDay = function (day) {
            return $scope.selectedDay && angular.equals(day.date, $scope.selectedDay.date);
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

        $scope.deleteDriverDay = function (driverDay) {
            var url = '/admin/schedule/day/' + driverDay.userId + '/' + driverDay.date[0] + '-' + driverDay.date[1] + '-' + driverDay.date[2];
            $http.delete(url).then(
                function (result) {
                    var idx = $scope.selectedDay.detailedDriverDayStatisticsArray.indexOf(driverDay);
                    if(idx >= 0) {
                        $scope.selectedDay.detailedDriverDayStatisticsArray.splice(idx, 1);
                        for(var i = 0; i < driverDay.hourStatisticsArray.length; i++){
                            if (driverDay.hourStatisticsArray[i].selected){
                                $scope.selectedDayLight.adminHourStatisticsArray[i].doneHours--;
                                $scope.selectedMonth.doneHours--;
                                $scope.selectedMonth.diffHours++;
                                $scope.selectedWeek.doneHours--;
                                $scope.selectedDay.doneHours--;
                            }
                        }
                    }
                    $scope.originalSelectedDay = angular.copy($scope.selectedDay);
                    $scope.originalSelectedDayLight = angular.copy($scope.selectedDayLight);
                });
        };

        $scope.addDriverDay = function(){
            var userListDialog = $modal.open({
                templateUrl: 'admin/users/users-list-mini.tpl.html',
                controller: 'UsersListCtrl',
                size: 'lg'});
            utilMethods.save('userListDialog', userListDialog);
            userListDialog.result.then(function(userId) {
                var url = '/admin/schedule/day/' + userId + '/' + $scope.selectedDay.date[0] + '-' + $scope.selectedDay.date[1] + '-' + $scope.selectedDay.date[2];

                $http.post(url).then(
                    function (result) {
                        var driverDay = result.data;
                        if (driverDay) {
                            $scope.selectedDay.detailedDriverDayStatisticsArray.push(driverDay);
                        }
                    });
            });
        };

        $scope.setNullSelectedDay = function(){
            $scope.selectedDay = null;
            $scope.originalSelectedDay = null;
            if ($scope.selectedDayLight && $scope.originalSelectedDayLight) {
                $scope.selectedDayLight.date = $scope.originalSelectedDayLight.date;
                $scope.selectedDayLight.adminHourStatisticsArray = $scope.originalSelectedDayLight.adminHourStatisticsArray;
            }
            $scope.originalSelectedDayLight = null;
            $scope.selectedDayLight = null;
        };

    }])
    .directive('infoHour', [ 'utilMethods', '$parse', function (utilMethods, $parse) {
        return {
            scope: false,
            link: function (scope, element, attrs) {
                var modelGetter = $parse(attrs.infoHour);
                var hourStatistics = modelGetter(scope);
                var greenColorShade = utilMethods.getGreenColorShadeWithMaxDefined(hourStatistics.doneHours, hourStatistics.plannedHours);
                scope.setColor(element, hourStatistics, greenColorShade);
                element.css('color', 'brown');
                element.text(hourStatistics.doneHours + '/' + hourStatistics.plannedHours);
            }
        };
    }])
    .directive('selectHourSimple', [ '$parse', function ($parse) {
        return {
            scope: false,
            link: function (scope, element, attrs) {
                var modelGetter = $parse(attrs.selectHourSimple);
                var hourStatistics = modelGetter(scope);
                element.css('color', 'brown');
                if (hourStatistics.enabled) {
                    element.removeAttr('disabled');
                } else {
                    element.attr('disabled', 'disabled');
                }
                scope.setColorSimple(element, hourStatistics);

                var modelSetter = modelGetter.assign;
                element.bind('click', function (event) {
                    scope.$apply(function () {
                        event.preventDefault();

                        var newValue = modelGetter(scope);
                        var adminHourStatistics = scope.selectedDayLight.adminHourStatisticsArray[hourStatistics.index];
                        if (newValue.selected) {
                            adminHourStatistics.doneHours--;
                            scope.selectedMonth.doneHours--;
                            scope.selectedMonth.diffHours++;
                            scope.selectedWeek.doneHours--;
                            scope.selectedDay.doneHours--;
                        } else {
                            if (adminHourStatistics.plannedHours - adminHourStatistics.doneHours <= 0) {
                                return;
                            }
                            adminHourStatistics.doneHours++;
                            scope.selectedMonth.doneHours++;
                            scope.selectedMonth.diffHours--;
                            scope.selectedWeek.doneHours++;
                            scope.selectedDay.doneHours++;
                        }
                        newValue.selected = !newValue.selected;
                        modelSetter(scope, newValue);
                        scope.setColorSimple(element, newValue);
                    });
                });
            }
        };
    }]);