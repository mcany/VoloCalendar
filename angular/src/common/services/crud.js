angular.module('services.crud', ['services.crudRouteProvider']);

angular.module('services.crud').factory('crudListMethods', ['$location', function ($location) {

    return function (pathPrefix) {

        var mixin = {};

        mixin['new'] = function () {
            $location.path(pathPrefix + '/new');
        };

        mixin['edit'] = function (itemId) {
            $location.path(pathPrefix + '/' + itemId);
        };

        return mixin;
    };
}]);