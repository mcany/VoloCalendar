angular.module('security.login.toolbar', [])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/profile', {
            templateUrl: 'security/login/profile-edit.tpl.html',
            controller: 'EditProfileCtrl',
            resolve: {
                currentUser: ['securityAuthorization', function (securityAuthorization) {
                    return securityAuthorization.requireAuthenticatedUser();
                }]
            }
        });
    }])
    .controller('EditProfileCtrl', ['$scope', '$location', '$http', 'security', 'utilMethods', function ($scope, $location, $http, security, utilMethods) {
        $scope.user = angular.copy(security.currentUser);
        $scope.password = $scope.user.password;
        $scope.original = angular.copy($scope.user);

        $scope.fileChanged = utilMethods.fileInputOfUserViewChanged($scope);

        $scope.save = function (user) {
            $http.post('/updateProfile', $scope.user).then(function(user){
                security.currentUser = user.data;
                $location.path('/');
            });
        };
        $scope.revertChanges = function () {
            $scope.user = angular.copy($scope.original);
        };
        $scope.canSave = function () {
            return $scope.form.$valid && !angular.equals($scope.user, $scope.original);
        };
        $scope.canRevert = function () {
            return !angular.equals($scope.user, $scope.original);
        };

        $scope.showError = function (fieldName, error) {
            return $scope.form[fieldName].$error[error];
        };
    }])
// The loginToolbar directive is a reusable widget that can show login or logout buttons
// and information the current authenticated user
    .directive('loginToolbar', ['security', function (security) {
        var directive = {
            templateUrl: 'security/login/toolbar.tpl.html',
            restrict: 'E',
            replace: true,
            scope: true,
            link: function ($scope, $element, $attrs, $controller) {
                $scope.isAuthenticated = security.isAuthenticated;
                $scope.login = security.showLogin;
                $scope.logout = security.logout;
                $scope.$watch(function () {
                    return security.currentUser;
                }, function (currentUser) {
                    $scope.currentUser = currentUser;
                });
            }
        };
        return directive;
    }]);