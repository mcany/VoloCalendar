angular.module('app', [
    'ngRoute',
    'calendar',
    'info',
    'admin',
    'home',
    'services.utilMethods',
    'services.breadcrumbs',
    'services.i18nNotifications',
    'services.httpRequestTracker',
    'security',
    'directives.crud',
    'templates.app',
    'templates.common',
    'ui.bootstrap']);

//TODO: move those messages to a separate module
angular.module('app').constant('I18N.MESSAGES', {
    'errors.route.changeError': 'Route change error',
    'crud.user.save.success': "A user with id '{{id}}' was saved successfully.",
    'crud.user.remove.success': "A user with id '{{id}}' was removed successfully.",
    'crud.user.remove.error': "Something went wrong when removing user with id '{{id}}'.",
    'crud.user.restore.success': "A user with id '{{id}}' was restored successfully.",
    'crud.user.restore.error': "Something went wrong when restoring user with id '{{id}}'.",
    'crud.user.save.error': "Something went wrong when saving a user...",
    'crud.project.save.success': "A project with id '{{id}}' was saved successfully.",
    'crud.project.remove.success': "A project with id '{{id}}' was removed successfully.",
    'crud.project.save.error': "Something went wrong when saving a project...",
    'login.reason.notAuthorized': "You do not have the necessary access permissions.  Do you want to login as someone else?",
    'login.reason.notAuthenticated': "You must be logged in to access this part of the application.",
    'login.error.invalidCredentials': "Login failed.  Please check your credentials and try again.",
    'login.error.serverError': "There was a problem with authenticating: {{exception}}."
});

angular.module('app').config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.html5Mode(true);
    $routeProvider.otherwise({redirectTo: '/'});
}]);

angular.module('app').run(['security', '$http', function (security, $http) {
    // Get the current user when the application starts
    // (in case they are still logged in from a previous session)
    $http.defaults.headers.common.Accept = 'application/json';
    security.requestCurrentUser();
}]);

angular.module('app').controller('AppCtrl', ['$scope', 'i18nNotifications', 'localizedMessages', function ($scope, i18nNotifications, localizedMessages) {

    $scope.notifications = i18nNotifications;

    $scope.removeNotification = function (notification) {
        i18nNotifications.remove(notification);
    };

    $scope.$on('$routeChangeError', function (event, current, previous, rejection) {
        i18nNotifications.pushForCurrentRoute('errors.route.changeError', 'error', {}, {rejection: rejection});
    });
}]);

angular.module('app').controller('HeaderCtrl', ['$scope', '$location', '$route', 'security', 'breadcrumbs', 'notifications', 'httpRequestTracker',
    function ($scope, $location, $route, security, breadcrumbs, notifications, httpRequestTracker) {
        $scope.location = $location;
        $scope.breadcrumbs = breadcrumbs;

        $scope.isAuthenticated = security.isAuthenticated;
        $scope.isAdmin = security.isAdmin;
        $scope.isDriver = function () {
            return security.isAuthenticated() && !security.isAdmin();
        };

        $scope.isNavbarActive = function (navBarPaths) {
            for (index = 0; index < navBarPaths.length; ++index) {
                if (navBarPaths[index] === $location.path()) {
                    return true;
                }
            }
            return false;
        };

        $scope.hasPendingRequests = function () {
            return httpRequestTracker.hasPendingRequests();
        };
    }]);
