angular.module('home', [])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/', {
            template: " ",
            controller: 'HomeCtrl'
        });
    }]);

angular.module('home').controller('HomeCtrl', ['$scope', '$location', 'security', function ($scope, $location, security) {
    security.requestCurrentUser().then(function (value) {
        if (security.isAdmin()) {
            $location.path('/admin/calendar');
        } else {
            if (security.isAuthenticated() && !security.isAdmin()) {
                $location.path('/calendar');
            } else {
                $location.path('/info');
            }
        }
    });
}]);