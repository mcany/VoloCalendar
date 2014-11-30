angular.module('security.login.toolbar', [], ['$routeProvider', function($routeProvider){

  $routeProvider.when('/profile', {
    templateUrl:'security/login/profile-edit.tpl.html',
    controller:'EditProfileCtrl',
	resolve:{		
		user:['Users', 'security', function (Users, security) {
			return security.requestCurrentUser().then(function(value){return Users.getById(security.currentUser.id);});
		}]
	}
  });}])
.controller('EditProfileCtrl', ['$scope', '$location', 'i18nNotifications', 'user', 'security', function ($scope, $location, i18nNotifications, user, security) {

  $scope.user = user;
  $scope.password = user.password;

  $scope.onSave = function (user) {
    i18nNotifications.pushForNextRoute('crud.user.save.success', 'success', {id : user.$id()});
    $location.path('/');
  };

  $scope.onError = function() {
    i18nNotifications.pushForCurrentRoute('crud.user.save.error', 'error');
  };

  $scope.onRemove = function(user) {
    //unreachable function
  };

}])
// The loginToolbar directive is a reusable widget that can show login or logout buttons
// and information the current authenticated user
.directive('loginToolbar', ['security', function(security) {
  var directive = {
    templateUrl: 'security/login/toolbar.tpl.html',
    restrict: 'E',
    replace: true,
    scope: true,
    link: function($scope, $element, $attrs, $controller) {
      $scope.isAuthenticated = security.isAuthenticated;
      $scope.login = security.showLogin;
      $scope.logout = security.logout;
	  $scope.$watch(function() {
        return security.currentUser;
      }, function(currentUser) {
        $scope.currentUser = currentUser;
      });
    }
  };
  return directive;
}]);