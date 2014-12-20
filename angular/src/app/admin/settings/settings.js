angular.module('settings', [
    'security.authorization'
])

    .config(['$routeProvider', 'securityAuthorizationProvider', function ($routeProvider, securityAuthorizationProvider) {
        $routeProvider.when('/admin/settings', {
            templateUrl: 'admin/settings/manual-forecasting.tpl.html',
            controller: 'ManualForecastingCtrl',
            resolve: {
                currentUser: securityAuthorizationProvider.requireAdminUser,
                manualForecasting: ['$http', function ($http) {
                    return $http.get('/admin/settings/manualForecasting').then(function (result) {
                        return result.data;
                    });
                }]
            }
        });
    }])
    .controller('ManualForecastingCtrl', ['$http', 'manualForecasting', '$scope', '$route', 'i18nNotifications', function ($http, manualForecasting, $scope, $route, i18nNotifications) {
        $scope.maxAllowed = 15;
        $scope.original = angular.copy(manualForecasting);
        $scope.manualForecasting = manualForecasting;
        $scope.save = function () {
            $http.post('/admin/settings/manualForecasting', $scope.manualForecasting);
            $scope.original = angular.copy($scope.manualForecasting);
        };
        $scope.revertChanges = function () {
            $route.reload();
        };
        $scope.canSave = function() {
            return !angular.equals($scope.manualForecasting, $scope.original);
        };
        $scope.canRevert = function() {
            return !angular.equals($scope.manualForecasting, $scope.original);
        };

        $scope.setColor = function(element, hourCount){
            if (hourCount == 0){
                element.css('backgroundColor', 'white');
            }else {
                var r = Math.floor(124 * (1 - hourCount / (2*$scope.maxAllowed)));
                var g = Math.floor(252 * (1 - hourCount / (2*$scope.maxAllowed)));
                var b = Math.floor(0 * (1 - hourCount / (2*$scope.maxAllowed)));
                element.css('backgroundColor', 'rgb(' + r + ',' + g + ',' + b + ')');
            }
        };
    }])
    .directive('voloCounter', function ($parse) {
        return {
            scope:false,
            link:function(scope, element, attrs) {
                var modelGetter = $parse(attrs.voloCounter);
                var hourCount = modelGetter(scope);
                element.text(hourCount);
                scope.setColor(element, hourCount);

                var modelSetter = modelGetter.assign;
                element.bind('click', function (event) {
                    scope.$apply(function () {
                        event.preventDefault();
                        var newValue = modelGetter(scope) + 1;
                        if (newValue > scope.maxAllowed) {
                            newValue = scope.maxAllowed;
                        }
                        modelSetter(scope, newValue);
                        element.text(newValue);
                        scope.setColor(element, newValue);
                    });
                });
                element.bind('click', function (event) {
                    scope.$apply(function () {
                        event.preventDefault();
                        var newValue = modelGetter(scope) + 1;
                        if (newValue > scope.maxAllowed) {
                            newValue = scope.maxAllowed;
                        }
                        modelSetter(scope, newValue);
                        element.text(newValue);
                        scope.setColor(element, newValue);
                    });
                });
                element.bind('keydown', function (event) {
                    scope.$apply(function () {
                        event.preventDefault();

                        var newValue;
                        var oldValue = modelGetter(scope);
                        if (event.keyCode == 32){
                            newValue = 0;
                        }else
                        if (event.keyCode == 8){
                            newValue = Math.floor(oldValue / 10);
                        }else
                        if (48 < event.keyCode && event.keyCode < 58) {
                            newValue = parseInt('' + oldValue + (event.keyCode - 48))
                            if (newValue > scope.maxAllowed){
                                newValue = scope.maxAllowed;
                            }
                        }else
                        if (96 < event.keyCode && event.keyCode < 106) {
                            newValue = parseInt('' + oldValue + (event.keyCode - 96))
                            if (newValue > scope.maxAllowed){
                                newValue = scope.maxAllowed;
                            }
                        }else{
                            return;
                        }
                        modelSetter(scope, newValue);
                        element.text(newValue);
                        scope.setColor(element, newValue);
                    });
                });
                element.bind('contextmenu', function (event) {
                    scope.$apply(function () {
                        event.preventDefault();
                        var newValue = modelGetter(scope) - 1;
                        if (newValue < 0) {
                            newValue = 0;
                        }
                        modelSetter(scope, newValue);
                        element.text(newValue);
                        scope.setColor(element, newValue);
                    });
                });
            }
        }
        ;
    });