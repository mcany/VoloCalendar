angular.module('calendar', [], ['$routeProvider', function($routeProvider){

  $routeProvider.when('/calendar', {
    templateUrl:'calendar/list.tpl.html',
    controller:'CalendarListCtrl'
  });
}]);

angular.module('calendar').controller('CalendarListCtrl', ['$scope', function($scope, projects){
}]);