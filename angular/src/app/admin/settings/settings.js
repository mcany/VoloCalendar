angular.module('settings', [
    'security.authorization'
])

    .config(['$routeProvider', 'securityAuthorizationProvider', function ($routeProvider, securityAuthorizationProvider) {
        $routeProvider.when('/admin/settings', {
            templateUrl: 'admin/settings/manual-forecasting.tpl.html',
            controller: 'ManualForecastingCtrl',
            resolve: {
                currentUser: securityAuthorizationProvider.requireAdminUser
            }
        });
    }])
    .controller('ManualForecastingCtrl', ['$scope', '$location', 'i18nNotifications', function ($scope, $location, i18nNotifications) {

    }]);
