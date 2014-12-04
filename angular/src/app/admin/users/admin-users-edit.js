angular.module('admin-users-edit',[
  'services.crud',
  'services.i18nNotifications',
  'admin-users-edit-uniqueEmail',
  'admin-users-edit-validateEquals',
  'services.utilMethods'
])

.controller('UsersEditCtrl', ['$scope', '$location', 'i18nNotifications', 'user', 'utilMethods', function ($scope, $location, i18nNotifications, user, utilMethods) {

  $scope.user = user;
  $scope.password = user.password;

  $scope.fileChanged = utilMethods.fileInputOfUserViewChanged($scope);

  $scope.onSave = function (user) {
    i18nNotifications.pushForNextRoute('crud.user.save.success', 'success', {id : user.$id()});
    $location.path('/admin/users');
  };

  $scope.onError = function() {
    i18nNotifications.pushForCurrentRoute('crud.user.save.error', 'error');
  };

  $scope.onRemove = function(user) {
    i18nNotifications.pushForNextRoute('crud.user.remove.success', 'success', {id : user.$id()});
    $location.path('/admin/users');
  };

}]);