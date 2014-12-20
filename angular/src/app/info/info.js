angular.module('info', [])
    .config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/info', {
        templateUrl: 'info/info.tpl.html',
        controller: 'InfoCtrl'
    });
}]);

angular.module('info').controller('InfoCtrl', ['$scope', function($scope){
}]);