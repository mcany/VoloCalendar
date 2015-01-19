angular.module('reporting', [
    'security.authorization'
])
    .config(['$routeProvider', 'securityAuthorizationProvider', function ($routeProvider, securityAuthorizationProvider) {
        $routeProvider.when('/admin/reporting', {
            templateUrl: 'admin/reporting/reporting.tpl.html',
            controller: 'ReportingCtrl',
            resolve: {
                currentUser: securityAuthorizationProvider.requireAdminUser
            }
        });
    }])
    .controller('ReportingCtrl', ['$http', '$scope', '$route', 'i18nNotifications', 'utilMethods',
        function ($http, $scope, $route, i18nNotifications, utilMethods) {
            var now = new Date();
            $scope.model = {'year':now.getUTCFullYear(), 'month':(now.getUTCMonth() + 1)};

            $scope.monthlyReport = function () {
                var url = '/admin/reporting/monthly/' + $scope.model.year + '/' + $scope.model.month;
                $http.get(url);
            };

            $scope.overviewReport = function () {
                var url = '/admin/reporting/overview';
                $http.get(url);
            };

            $scope.showError = function (fieldName, error) {
                return $scope.form[fieldName].$error[error];
            };

            $scope.canSubmit = function () {
                return $scope.form.$valid;
            };
        }]);