angular.module('calendar', ['security.authorization'])
    .config(['$routeProvider', 'securityAuthorizationProvider', function ($routeProvider, securityAuthorizationProvider) {
        $routeProvider.when('/calendar', {
            templateUrl: 'calendar/calendar.tpl.html',
            controller: 'CalendarCtrl',
            resolve: {
                currentUser: securityAuthorizationProvider.requireAuthenticatedUser,
                driverCalendarViewModel: ['security', '$http', 'securityAuthorization', function (security, $http, securityAuthorization) {
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

angular.module('calendar').controller('CalendarCtrl', ['$scope', '$location', 'security', 'driverCalendarViewModel', '$http',
    function ($scope, $location, security, driverCalendarViewModel, $http) {
        /*if (security.isAdmin()) {
         $location.path('/admin/calendar');
         }*/
        $scope.maxAllowed = 15;
        $scope.driverCalendarViewModel = driverCalendarViewModel;

        $scope.monthSelected = function(month){
            var url = '/driver/month/' + security.currentUser.id + '/' + month.monthBeginDate.year + '-' + month.monthBeginDate.monthValue + '-' + month.monthBeginDate.dayOfMonth;
            $http.get(url).then(
                function (result) {
                    $scope.selectedMonth = result.data;
                });
        };

        $scope.save = function () {
            var url = '/driver/week/' + security.currentUser.id + '/' + $scope.selectedWeek.weekBeginDate.year + '-' + $scope.selectedWeek.weekBeginDate.monthValue + '-' + $scope.selectedWeek.weekBeginDate.dayOfMonth;
            $http.post(url, $scope.selectedWeek);
            $scope.original = angular.copy($scope.selectedWeek);
        };
        $scope.revertChanges = function () {
            $route.reload();
        };
        $scope.canSave = function() {
            return !angular.equals($scope.selectedWeek, $scope.original);
        };
        $scope.canRevert = function() {
            return !angular.equals($scope.selectedWeek, $scope.original);
        };

        $scope.weekSelected = function(week){
            var url = '/driver/week/' + security.currentUser.id + '/' + week.weekBeginDate.year + '-' + week.weekBeginDate.monthValue + '-' + week.weekBeginDate.dayOfMonth;
            $http.get(url).then(
                function (result) {
                    $scope.selectedWeek = result.data;
                    $scope.original = angular.copy($scope.selectedWeek);
                });
        };

        $scope.getGreenColorShade = function(hourStatistics){
            var result;
            if (!hourStatistics.enabled) {
                result = 'white';
            } else {
                var r = Math.floor(124 * (1 - hourStatistics.requiredDriverCount / (2 * $scope.maxAllowed)));
                var g = Math.floor(252 * (1 - hourStatistics.requiredDriverCount / (2 * $scope.maxAllowed)));
                var b = Math.floor(0 * (1 - hourStatistics.requiredDriverCount / (2 * $scope.maxAllowed)));
                result = 'rgb(' + r + ',' + g + ',' + b + ')';
            }
            return result;
        };

        $scope.setColor = function(element, hourStatistics, greenColorShade){
            if (hourStatistics.selected){
                element.css('backgroundColor', 'blue');
            }else {
                element.css('backgroundColor', greenColorShade);
            }
        };
    }])
    .directive('selectHour', function ($parse) {
        return {
            scope:false,
            link:function(scope, element, attrs) {
                var modelGetter = $parse(attrs.selectHour);
                var hourStatistics = modelGetter(scope);
                if (hourStatistics.enabled){
                    element.removeAttr('disabled');
                }else{
                    element.attr('disabled', 'disabled');
                }
                var greenColorShade = scope.getGreenColorShade(hourStatistics);
                scope.setColor(element, hourStatistics, greenColorShade);

                var modelSetter = modelGetter.assign;
                element.bind('click', function (event) {
                    scope.$apply(function () {
                        event.preventDefault();

                        var newValue = modelGetter(scope);
                        if (!newValue.enabled){
                            return;
                        }
                        if (newValue.selected){
                            scope.selectedMonth.doneHours--;
                            scope.selectedMonth.diffHours++;
                        }else{
                            scope.selectedMonth.doneHours++;
                            scope.selectedMonth.diffHours--;
                        }
                        newValue.selected = !newValue.selected;
                        modelSetter(scope, newValue);
                        scope.setColor(element, newValue, greenColorShade);
                    });
                });
            }
        };
    });