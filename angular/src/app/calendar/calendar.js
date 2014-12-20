angular.module('calendar', ['security.authorization'])
    .config(['$routeProvider', 'securityAuthorizationProvider', function ($routeProvider, securityAuthorizationProvider) {
        $routeProvider.when('/calendar', {
            templateUrl: 'calendar/calendar.tpl.html',
            controller: 'CalendarCtrl',
            resolve: {
                currentUser: securityAuthorizationProvider.requireAuthenticatedUser
            }
        });
    }]);

angular.module('calendar').controller('CalendarCtrl', ['$scope', '$location', 'security', function($scope, $location, security){
    if (security.isAdmin()) {
        $location.path('/admin/calendar');
    }
}]);