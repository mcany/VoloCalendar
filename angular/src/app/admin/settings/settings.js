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
                var r = Math.floor(124 * (1 - hourCount / 30));
                var g = Math.floor(252 * (1 - hourCount / 30));
                var b = Math.floor(0 * (1 - hourCount / 30));
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
                        if (newValue > 15) {
                            newValue = 15;
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