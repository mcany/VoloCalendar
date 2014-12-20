angular.module('admin-calendar', ['security.authorization'])
    .config(['$routeProvider', 'securityAuthorizationProvider', function ($routeProvider, securityAuthorizationProvider) {
        $routeProvider.when('/admin/calendar', {
            templateUrl: 'admin/calendar/admin-calendar.tpl.html',
            controller: 'AdminCalendarCtrl',
            resolve: {
                currentUser: securityAuthorizationProvider.requireAdminUser
            }
        });
    }]);

angular.module('admin-calendar').controller('AdminCalendarCtrl', ['$scope', function($scope){
}]);