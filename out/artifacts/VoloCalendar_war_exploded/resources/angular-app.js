/*! angular-app - v0.0.1-SNAPSHOT - 2014-12-07
 * https://github.com/angular-app/angular-app
 * Copyright (c) 2014 Pawel Kozlowski & Peter Bacon Darwin;
 * Licensed MIT
 */
angular.module('admin', ['admin-users']);

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
angular.module('admin-users-list', [
    'services.crud',
    'services.i18nNotifications',
    'services.utilMethods'
])

    .controller('UsersListCtrl', ['$scope', 'crudListMethods', 'i18nNotifications', '$http', 'mongolabResource', 'utilMethods'
        , function ($scope, crudListMethods, i18nNotifications, $http, mongolabResource, utilMethods) {
            angular.extend($scope, crudListMethods('/admin/users'));

            $scope.changePageVolume = function(){
                $scope.itemsPerPage = parseInt($scope.itemsPerPageStr, 10);
                $scope.pageChanged();
            }

            $scope.remove = function (user, $index, $event) {
                // Don't let the click bubble up to the ng-click on the enclosing div, which will try to trigger
                // an edit of this item.
                $event.stopPropagation();

                // Remove this user
                user.$remove(function () {
                    // It is gone from the DB so we can remove it from the local list too
                    $scope.users.splice($index, 1);
                    $scope.totalItems = $scope.totalItems - 1;
                    i18nNotifications.pushForCurrentRoute('crud.user.remove.success', 'success', {id: user.$id()});
                }, function () {
                    i18nNotifications.pushForCurrentRoute('crud.user.remove.error', 'error', {id: user.$id()});
                });
            };

            $scope.editUser = function (id) {
                utilMethods.save('pagingData', $scope);
                $scope.edit(id);
            };

            $scope.pageChanged = function () {
                $http.post('/databases/scrumdb/collections/users/pagination'
                    , {sortingField: $scope.sortingField, reverse: $scope.reverse, beginIndex: (($scope.currentPage - 1) * $scope.itemsPerPage + 1), maxNumber: $scope.itemsPerPage}).
                    success(function (data, status, headers, config) {
                        result = [];
                        for (var i = 0; i < data.length; i++) {
                            var myResource = mongolabResource('users');
                            result.push(new myResource(data[i]));
                        }
                        $scope.users = result;
                    }).
                    error(function (data, status, headers, config) {
                        $scope.users = null;
                    });
            };
            $scope.sort = function (sortingField) {
                if ($scope.sortingField != sortingField) {
                    $scope.sortingField = sortingField;
                    $scope.reverse = false;
                } else {
                    $scope.reverse = !$scope.reverse;
                }
                $scope.currentPage = 1;
                $scope.pageChanged();
            };
            var cache = utilMethods.get('pagingData');

            $scope.maxSize = 5;
            if (cache == null) {
                $http.get('/databases/scrumdb/collections/users/count').
                    success(function (data, status, headers, config) {
                        $scope.totalItems = data;
                    }).
                    error(function (data, status, headers, config) {
                        $scope.totalItems = 0;
                    });
                $scope.currentPage = 1;
                $scope.sortingField = null;
                $scope.reverse = false;
                $scope.itemsPerPage = 5;
                $scope.users = null;
                $scope.pageChanged();
            } else {
                $scope.totalItems = cache.totalItems;
                $scope.currentPage = cache.currentPage;
                $scope.sortingField = cache.sortingField;
                $scope.reverse = cache.reverse;
                $scope.itemsPerPage = cache.itemsPerPage;
                $scope.users = cache.users;
            }

        }])
    .directive('columnHeader', function () {
        return {
            restrict: 'A',
            templateUrl: 'admin/users/column-header.tpl.html',
            scope: {
                sortingField: '@',
                reverse: '@',
                sort: '&'
            },
            link: function (scope, elm, attrs) {
                scope.columnField = attrs.columnField;
                scope.columnHeader = attrs.columnHeader;
                scope.headerClick = function () {
                    scope.sort({field: scope.columnField});
                };
            }
        };
    });

angular.module('admin-users', [
  'admin-users-list',
  'admin-users-edit',  
  'services.crud',
  'security.authorization',
  'directives.gravatar'
])

.config(['crudRouteProvider', 'securityAuthorizationProvider', function (crudRouteProvider, securityAuthorizationProvider) {

  crudRouteProvider.routesFor('Users', 'admin')
    .whenList({
      currentUser: securityAuthorizationProvider.requireAdminUser
    })
    .whenNew({
      user: ['Users', function(Users) { return new Users({admin:false, base64Image:'data:image/jpg;base64,/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAkGBwgHBgkIBwgKCgkLDRYPDQwMDRsUFRAWIB0iIiAdHx8kKDQsJCYxJx8fLT0tMTU3Ojo6Iys/RD84QzQ5Ojf/2wBDAQoKCg0MDRoPDxo3JR8lNzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzf/wAARCABBADIDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDAqVIi3ODTreLdgmrm1YwMcmvLZ9FYrLb57UjW+Oxq1vOaN57ikOxnuhU9DTK0ZIw6kiqEibXxTE0Noo/Gj8adhGpCoVc8Uh55IqUD92PrQg3OqkcEipcrK5pG7dhixO4ykbH3AppQg4IIPvXQ2sSCEAD1qpq0SrhhjOelcscS3KzR0yopRvcykGDjPFV7qMcmrWOabcLletdaZytoy9oo2ipSnPWk2e9VcRrJzH16UnIP9aow6pGc7VJGP1qWa6G0Yj4YZ4ah03szP6zDdGzb3rJHhkc47gVWu53nYFgQOwxWWdROwkJkA4xupo1JhwyEA8cNURw0Iu4Sx0mrF8KG5UAj60k6seAhP0FUm1FUUkrj05/+tWfd6uWLbUIxzu3dPwxWqppmLxVtzR8p/wDnm/8A3yaPJf8A55v/AN8msv8AtvHBYEjqcn/Cj+3B6j8z/hVeyI+ueRnQ3ahh82BV5L5AuGkXAPUHNc+j+S+0AkZGSR0Hr/h600vJLclYlPGcsBnHH1GK2U4Nao8iGIkdOk8UjBVZQTyAeKSW5SOPKgNnpjqaxo5JcL8wEgHqWHp+fSo5ZGEi7yxIHIH8PQ5+tZykuhq8S0i7cXyshZWP54OKzXlZyclsenWnFAH42cjDM54YdsH1yfSmSQIHcIxJ25yO3HTHet6M6W0zP29wO0nJ6/SjC/5FOXT3ZQRLJyM/cH+NL/Z0n/PWT/vgf/FV1Ww/84e1G3H/AC3/ANxP61Xtf+Pl/wAf5GrFx/y3/wBxP61Xtf8Aj5f8f5GvNOaO5Ncf8hUf7x/9mpT9yT60lx/yFR/vH/2alP3JPrUz2JqCT/dX6L/6EKbbf8hGH/eH/oNOn+6v0X/0IU22/wCQjD/vD/0GiGw0S0UUVJR//9k='}); }],
      currentUser: securityAuthorizationProvider.requireAdminUser
    })
    .whenEdit({
      user:['$route', 'Users', function ($route, Users) {
        return Users.getById($route.current.params.itemId);
      }],
      currentUser: securityAuthorizationProvider.requireAdminUser
    });
}]);
angular.module('admin-users-edit-uniqueEmail', ['resources.users'])

/**
 * A validation directive to ensure that the model contains a unique email address
 * @param  Users service to provide access to the server's user database
  */
.directive('uniqueEmail', ["Users", function (Users) {
  return {
    require:'ngModel',
    restrict:'A',
    link:function (scope, el, attrs, ctrl) {

      //TODO: We need to check that the value is different to the original
      
      //using push() here to run it as the last parser, after we are sure that other validators were run
      ctrl.$parsers.push(function (viewValue) {

        if (viewValue) {
          Users.getByEmail(viewValue + '_', function (users) {
            if (users.length === 0) {
              ctrl.$setValidity('uniqueEmail', true);
            } else {
              ctrl.$setValidity('uniqueEmail', false);
            }
          });
          return viewValue;
        }
      });
    }
  };
}]);
angular.module('admin-users-edit-validateEquals', [])

/**
 * A validation directive to ensure that this model has the same value as some other
 */
.directive('validateEquals', function() {
  return {
    restrict: 'A',
    require: 'ngModel',
    link: function(scope, elm, attrs, ctrl) {

      function validateEqual(myValue, otherValue) {
        if (myValue === otherValue) {
          ctrl.$setValidity('equal', true);
          return myValue;
        } else {
          ctrl.$setValidity('equal', false);
          return undefined;
        }
      }

      scope.$watch(attrs.validateEquals, function(otherModelValue) {
        ctrl.$setValidity('equal', ctrl.$viewValue === otherModelValue);
      });

      ctrl.$parsers.push(function(viewValue) {
        return validateEqual(viewValue, scope.$eval(attrs.validateEquals));
      });

      ctrl.$formatters.push(function(modelValue) {
        return validateEqual(modelValue, scope.$eval(attrs.validateEquals));
      });
    }
  };
});
angular.module('app', [
  'ngRoute',
  'calendar',
  'admin',
  'services.breadcrumbs',
  'services.i18nNotifications',
  'services.httpRequestTracker',
  'security',
  'directives.crud',
  'templates.app',
  'templates.common',
  'ui.bootstrap']);

angular.module('app').constant('MONGOLAB_CONFIG', {
  baseUrl: '/databases/',
  dbName: 'scrumdb'
});

//TODO: move those messages to a separate module
angular.module('app').constant('I18N.MESSAGES', {
  'errors.route.changeError':'Route change error',
  'crud.user.save.success':"A user with id '{{id}}' was saved successfully.",
  'crud.user.remove.success':"A user with id '{{id}}' was removed successfully.",
  'crud.user.remove.error':"Something went wrong when removing user with id '{{id}}'.",
  'crud.user.save.error':"Something went wrong when saving a user...",
  'crud.project.save.success':"A project with id '{{id}}' was saved successfully.",
  'crud.project.remove.success':"A project with id '{{id}}' was removed successfully.",
  'crud.project.save.error':"Something went wrong when saving a project...",
  'login.reason.notAuthorized':"You do not have the necessary access permissions.  Do you want to login as someone else?",
  'login.reason.notAuthenticated':"You must be logged in to access this part of the application.",
  'login.error.invalidCredentials': "Login failed.  Please check your credentials and try again.",
  'login.error.serverError': "There was a problem with authenticating: {{exception}}."
});

angular.module('app').config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
  $locationProvider.html5Mode(true);
  $routeProvider.otherwise({redirectTo:'/calendar'});
}]);

angular.module('app').run(['security', function(security) {
  // Get the current user when the application starts
  // (in case they are still logged in from a previous session)
  security.requestCurrentUser();
}]);

angular.module('app').controller('AppCtrl', ['$scope', 'i18nNotifications', 'localizedMessages', function($scope, i18nNotifications, localizedMessages) {

  $scope.notifications = i18nNotifications;

  $scope.removeNotification = function (notification) {
    i18nNotifications.remove(notification);
  };

  $scope.$on('$routeChangeError', function(event, current, previous, rejection){
    i18nNotifications.pushForCurrentRoute('errors.route.changeError', 'error', {}, {rejection: rejection});
  });
}]);

angular.module('app').controller('HeaderCtrl', ['$scope', '$location', '$route', 'security', 'breadcrumbs', 'notifications', 'httpRequestTracker',
  function ($scope, $location, $route, security, breadcrumbs, notifications, httpRequestTracker) {
  $scope.location = $location;
  $scope.breadcrumbs = breadcrumbs;

  $scope.isAuthenticated = security.isAuthenticated;
  $scope.isAdmin = security.isAdmin;

  $scope.home = function () {
    if (security.isAuthenticated()) {
      $location.path('/calendar');
    } else {
      $location.path('/calendar');
    }
  };

  $scope.isNavbarActive = function (navBarPath) {
    return navBarPath === breadcrumbs.getFirst().name;
  };

  $scope.hasPendingRequests = function () {
    return httpRequestTracker.hasPendingRequests();
  };
}]);

angular.module('calendar', [], ['$routeProvider', function($routeProvider){

  $routeProvider.when('/calendar', {
    templateUrl:'calendar/list.tpl.html',
    controller:'CalendarListCtrl'
  });
}]);

angular.module('calendar').controller('CalendarListCtrl', ['$scope', function($scope, projects){
}]);
angular.module('directives.crud', ['directives.crud.buttons', 'directives.crud.edit']);

angular.module('directives.crud.buttons', [])

.directive('crudButtons', function () {
  return {
    restrict:'E',
    replace:true,
    template:
      '<div>' +
      '  <button type="button" class="btn btn-primary save" ng-disabled="!canSave()" ng-click="save()">Save</button>' +
      '  <button type="button" class="btn btn-warning revert" ng-click="revertChanges()" ng-disabled="!canRevert()">Revert changes</button>'+
      '  <button type="button" class="btn btn-danger remove" ng-click="remove()" ng-show="canRemove()">Remove</button>'+
      '</div>'
  };
});
angular.module('directives.crud.edit', [])

// Apply this directive to an element at or below a form that will manage CRUD operations on a resource.
// - The resource must expose the following instance methods: $saveOrUpdate(), $id() and $remove()
.directive('crudEdit', ['$parse', function($parse) {
  return {
    // We ask this directive to create a new child scope so that when we add helper methods to the scope
    // it doesn't make a mess of the parent scope.
    // - Be aware that if you write to the scope from within the form then you must remember that there is a child scope at the point
    scope: true,
    // We need access to a form so we require a FormController from this element or a parent element
    require: '^form',
    // This directive can only appear as an attribute
    link: function(scope, element, attrs, form) {
      // We extract the value of the crudEdit attribute
      // - it should be an assignable expression evaluating to the model (resource) that is going to be edited
      var resourceGetter = $parse(attrs.crudEdit);
      var resourceSetter = resourceGetter.assign;
      // Store the resource object for easy access
      var resource = resourceGetter(scope);
      // Store a copy for reverting the changes
      var original = angular.copy(resource);

      var checkResourceMethod = function(methodName) {
        if ( !angular.isFunction(resource[methodName]) ) {
          throw new Error('crudEdit directive: The resource must expose the ' + methodName + '() instance method');
        }
      };
      checkResourceMethod('$saveOrUpdate');
      checkResourceMethod('$id');
      checkResourceMethod('$remove');

      // This function helps us extract the callback functions from the directive attributes
      var makeFn = function(attrName) {
        var fn = scope.$eval(attrs[attrName]);
        if ( !angular.isFunction(fn) ) {
          throw new Error('crudEdit directive: The attribute "' + attrName + '" must evaluate to a function');
        }
        return fn;
      };
      // Set up callbacks with fallback
      // onSave attribute -> onSave scope -> noop
      var userOnSave = attrs.onSave ? makeFn('onSave') : ( scope.onSave || angular.noop );
      var onSave = function(result, status, headers, config) {
        // Reset the original to help with reverting and pristine checks
        original = result;
        userOnSave(result, status, headers, config);
      };
      // onRemove attribute -> onRemove scope -> onSave attribute -> onSave scope -> noop
      var onRemove = attrs.onRemove ? makeFn('onRemove') : ( scope.onRemove || onSave );
      // onError attribute -> onError scope -> noop
      var onError = attrs.onError ? makeFn('onError') : ( scope.onError || angular.noop );

      // The following functions should be triggered by elements on the form
      // - e.g. ng-click="save()"
      scope.save = function() {
        resource.$saveOrUpdate(onSave, onSave, onError, onError);
      };
      scope.revertChanges = function() {
        resource = angular.copy(original);
        resourceSetter(scope, resource);
        form.$setPristine();
      };
      scope.remove = function() {
        if(resource.$id()) {
          resource.$remove(onRemove, onError);
        } else {
          onRemove();
        }
      };

      // The following functions can be called to modify the behaviour of elements in the form
      // - e.g. ng-disable="!canSave()"
      scope.canSave = function() {
        return form.$valid && !angular.equals(resource, original);
      };
      scope.canRevert = function() {
        return !angular.equals(resource, original);
      };
      scope.canRemove = function() {
        return resource.$id();
      };
      /**
       * Get the CSS classes for this item, to be used by the ng-class directive
       * @param {string} fieldName The name of the field on the form, for which we want to get the CSS classes
       * @return {object} A hash where each key is a CSS class and the corresponding value is true if the class is to be applied.
       */
      scope.getCssClasses = function(fieldName) {
        var ngModelController = form[fieldName];
        return {
          error: ngModelController.$invalid && !angular.equals(resource, original),
          success: ngModelController.$valid && !angular.equals(resource, original)
        };
      };
      /**
       * Whether to show an error message for the specified error
       * @param {string} fieldName The name of the field on the form, of which we want to know whether to show the error
       * @param  {string} error - The name of the error as given by a validation directive
       * @return {Boolean} true if the error should be shown
       */
      scope.showError = function(fieldName, error) {
        return form[fieldName].$error[error];
      };
    }
  };
}]);
angular.module('directives.gravatar', [])

// A simple directive to display a gravatar image given an email
.directive('gravatar', ['md5', function(md5) {

  return {
    restrict: 'E',
    template: '<img ng-src="http://www.gravatar.com/avatar/{{hash}}{{getParams}}"/>',
    replace: true,
    scope: {
      email: '=',
      size: '=',
      defaultImage: '=',
      forceDefault: '='
    },
    link: function(scope, element, attrs) {
      scope.options = {};
      scope.$watch('email', function(email) {
        if ( email ) {
          scope.hash = md5(email.trim().toLowerCase());
        }
      });
      scope.$watch('size', function(size) {
        scope.options.s = (angular.isNumber(size)) ? size : undefined;
        generateParams();
      });
      scope.$watch('forceDefault', function(forceDefault) {
        scope.options.f = forceDefault ? 'y' : undefined;
        generateParams();
      });
      scope.$watch('defaultImage', function(defaultImage) {
        scope.options.d = defaultImage ? defaultImage : undefined;
        generateParams();
      });
      function generateParams() {
        var options = [];
        scope.getParams = '';
        angular.forEach(scope.options, function(value, key) {
          if ( value ) {
            options.push(key + '=' + encodeURIComponent(value));
          }
        });
        if ( options.length > 0 ) {
          scope.getParams = '?' + options.join('&');
        }
      }
    }
  };
}])

.factory('md5', function() {
  function md5cycle(x, k) {
    var a = x[0],
      b = x[1],
      c = x[2],
      d = x[3];

    a = ff(a, b, c, d, k[0], 7, -680876936);
    d = ff(d, a, b, c, k[1], 12, -389564586);
    c = ff(c, d, a, b, k[2], 17, 606105819);
    b = ff(b, c, d, a, k[3], 22, -1044525330);
    a = ff(a, b, c, d, k[4], 7, -176418897);
    d = ff(d, a, b, c, k[5], 12, 1200080426);
    c = ff(c, d, a, b, k[6], 17, -1473231341);
    b = ff(b, c, d, a, k[7], 22, -45705983);
    a = ff(a, b, c, d, k[8], 7, 1770035416);
    d = ff(d, a, b, c, k[9], 12, -1958414417);
    c = ff(c, d, a, b, k[10], 17, -42063);
    b = ff(b, c, d, a, k[11], 22, -1990404162);
    a = ff(a, b, c, d, k[12], 7, 1804603682);
    d = ff(d, a, b, c, k[13], 12, -40341101);
    c = ff(c, d, a, b, k[14], 17, -1502002290);
    b = ff(b, c, d, a, k[15], 22, 1236535329);

    a = gg(a, b, c, d, k[1], 5, -165796510);
    d = gg(d, a, b, c, k[6], 9, -1069501632);
    c = gg(c, d, a, b, k[11], 14, 643717713);
    b = gg(b, c, d, a, k[0], 20, -373897302);
    a = gg(a, b, c, d, k[5], 5, -701558691);
    d = gg(d, a, b, c, k[10], 9, 38016083);
    c = gg(c, d, a, b, k[15], 14, -660478335);
    b = gg(b, c, d, a, k[4], 20, -405537848);
    a = gg(a, b, c, d, k[9], 5, 568446438);
    d = gg(d, a, b, c, k[14], 9, -1019803690);
    c = gg(c, d, a, b, k[3], 14, -187363961);
    b = gg(b, c, d, a, k[8], 20, 1163531501);
    a = gg(a, b, c, d, k[13], 5, -1444681467);
    d = gg(d, a, b, c, k[2], 9, -51403784);
    c = gg(c, d, a, b, k[7], 14, 1735328473);
    b = gg(b, c, d, a, k[12], 20, -1926607734);

    a = hh(a, b, c, d, k[5], 4, -378558);
    d = hh(d, a, b, c, k[8], 11, -2022574463);
    c = hh(c, d, a, b, k[11], 16, 1839030562);
    b = hh(b, c, d, a, k[14], 23, -35309556);
    a = hh(a, b, c, d, k[1], 4, -1530992060);
    d = hh(d, a, b, c, k[4], 11, 1272893353);
    c = hh(c, d, a, b, k[7], 16, -155497632);
    b = hh(b, c, d, a, k[10], 23, -1094730640);
    a = hh(a, b, c, d, k[13], 4, 681279174);
    d = hh(d, a, b, c, k[0], 11, -358537222);
    c = hh(c, d, a, b, k[3], 16, -722521979);
    b = hh(b, c, d, a, k[6], 23, 76029189);
    a = hh(a, b, c, d, k[9], 4, -640364487);
    d = hh(d, a, b, c, k[12], 11, -421815835);
    c = hh(c, d, a, b, k[15], 16, 530742520);
    b = hh(b, c, d, a, k[2], 23, -995338651);

    a = ii(a, b, c, d, k[0], 6, -198630844);
    d = ii(d, a, b, c, k[7], 10, 1126891415);
    c = ii(c, d, a, b, k[14], 15, -1416354905);
    b = ii(b, c, d, a, k[5], 21, -57434055);
    a = ii(a, b, c, d, k[12], 6, 1700485571);
    d = ii(d, a, b, c, k[3], 10, -1894986606);
    c = ii(c, d, a, b, k[10], 15, -1051523);
    b = ii(b, c, d, a, k[1], 21, -2054922799);
    a = ii(a, b, c, d, k[8], 6, 1873313359);
    d = ii(d, a, b, c, k[15], 10, -30611744);
    c = ii(c, d, a, b, k[6], 15, -1560198380);
    b = ii(b, c, d, a, k[13], 21, 1309151649);
    a = ii(a, b, c, d, k[4], 6, -145523070);
    d = ii(d, a, b, c, k[11], 10, -1120210379);
    c = ii(c, d, a, b, k[2], 15, 718787259);
    b = ii(b, c, d, a, k[9], 21, -343485551);

    x[0] = add32(a, x[0]);
    x[1] = add32(b, x[1]);
    x[2] = add32(c, x[2]);
    x[3] = add32(d, x[3]);

  }

  function cmn(q, a, b, x, s, t) {
    a = add32(add32(a, q), add32(x, t));
    return add32((a << s) | (a >>> (32 - s)), b);
  }

  function ff(a, b, c, d, x, s, t) {
    return cmn((b & c) | ((~b) & d), a, b, x, s, t);
  }

  function gg(a, b, c, d, x, s, t) {
    return cmn((b & d) | (c & (~d)), a, b, x, s, t);
  }

  function hh(a, b, c, d, x, s, t) {
    return cmn(b ^ c ^ d, a, b, x, s, t);
  }

  function ii(a, b, c, d, x, s, t) {
    return cmn(c ^ (b | (~d)), a, b, x, s, t);
  }

  function md51(s) {
    txt = '';
    var n = s.length,
      state = [1732584193, -271733879, -1732584194, 271733878],
      i;
    for (i = 64; i <= s.length; i += 64) {
      md5cycle(state, md5blk(s.substring(i - 64, i)));
    }
    s = s.substring(i - 64);
    var tail = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
    for (i = 0; i < s.length; i++) {
      tail[i >> 2] |= s.charCodeAt(i) << ((i % 4) << 3);
    }
    tail[i >> 2] |= 0x80 << ((i % 4) << 3);
    if (i > 55) {
      md5cycle(state, tail);
      for (i = 0; i < 16; i++) {
        tail[i] = 0;
      }
    }
    tail[14] = n * 8;
    md5cycle(state, tail);
    return state;
  }

  /* there needs to be support for Unicode here,
   * unless we pretend that we can redefine the MD-5
   * algorithm for multi-byte characters (perhaps
   * by adding every four 16-bit characters and
   * shortening the sum to 32 bits). Otherwise
   * I suggest performing MD-5 as if every character
   * was two bytes--e.g., 0040 0025 = @%--but then
   * how will an ordinary MD-5 sum be matched?
   * There is no way to standardize text to something
   * like UTF-8 before transformation; speed cost is
   * utterly prohibitive. The JavaScript standard
   * itself needs to look at this: it should start
   * providing access to strings as preformed UTF-8
   * 8-bit unsigned value arrays.
   */

  function md5blk(s) { /* I figured global was faster.   */
    var md5blks = [],
      i; /* Andy King said do it this way. */
    for (i = 0; i < 64; i += 4) {
      md5blks[i >> 2] = s.charCodeAt(i) + (s.charCodeAt(i + 1) << 8) + (s.charCodeAt(i + 2) << 16) + (s.charCodeAt(i + 3) << 24);
    }
    return md5blks;
  }

  var hex_chr = '0123456789abcdef'.split('');

  function rhex(n) {
    var s = '', j = 0;
    for (; j < 4; j++) {
      s += hex_chr[(n >> (j * 8 + 4)) & 0x0F] + hex_chr[(n >> (j * 8)) & 0x0F];
    }
    return s;
  }

  function hex(x) {
    for (var i = 0; i < x.length; i++) {
      x[i] = rhex(x[i]);
    }
    return x.join('');
  }

  function md5(s) {
    return hex(md51(s));
  }

  /* this function is much faster,
  so if possible we use it. Some IEs
  are the only ones I know of that
  need the idiotic second function,
  generated by an if clause.  */

  add32 = function(a, b) {
    return (a + b) & 0xFFFFFFFF;
  };

  if (md5('hello') !== '5d41402abc4b2a76b9719d911017c592') {
    add32 = function (x, y) {
      var lsw = (x & 0xFFFF) + (y & 0xFFFF),
        msw = (x >> 16) + (y >> 16) + (lsw >> 16);
      return (msw << 16) | (lsw & 0xFFFF);
    };
  }

  return md5;
});
angular.module('directives.modal', []).directive('modal', ['$parse',function($parse) {
  var backdropEl;
  var body = angular.element(document.getElementsByTagName('body')[0]);
  var defaultOpts = {
    backdrop: true,
    escape: true
  };
  return {
    restrict: 'ECA',
    link: function(scope, elm, attrs) {
      var opts = angular.extend(defaultOpts, scope.$eval(attrs.uiOptions || attrs.bsOptions || attrs.options));
      var shownExpr = attrs.modal || attrs.show;
      var setClosed;

      if (attrs.close) {
        setClosed = function() {
          scope.$apply(attrs.close);
        };
      } else {
        setClosed = function() {
          scope.$apply(function() {
            $parse(shownExpr).assign(scope, false);
          });
        };
      }
      elm.addClass('modal');

      if (opts.backdrop && !backdropEl) {
        backdropEl = angular.element('<div class="modal-backdrop"></div>');
        backdropEl.css('display','none');
        body.append(backdropEl);
      }

      function setShown(shown) {
        scope.$apply(function() {
          model.assign(scope, shown);
        });
      }

      function escapeClose(evt) {
        if (evt.which === 27) { setClosed(); }
      }
      function clickClose() {
        setClosed();
      }

      function close() {
        if (opts.escape) { body.unbind('keyup', escapeClose); }
        if (opts.backdrop) {
          backdropEl.css('display', 'none').removeClass('in');
          backdropEl.unbind('click', clickClose);
        }
        elm.css('display', 'none').removeClass('in');
        body.removeClass('modal-open');
      }
      function open() {
        if (opts.escape) { body.bind('keyup', escapeClose); }
        if (opts.backdrop) {
          backdropEl.css('display', 'block').addClass('in');
          backdropEl.bind('click', clickClose);
        }
        elm.css('display', 'block').addClass('in');
        body.addClass('modal-open');
      }

      scope.$watch(shownExpr, function(isShown, oldShown) {
        if (isShown) {
          open();
        } else {
          close();
        }
      });
    }
  };
}]);

angular.module('resources.users', ['mongolabResource']);
angular.module('resources.users').factory('Users', ['mongolabResource', function (mongoResource) {

  var userResource = mongoResource('users');
  userResource.prototype.getFullName = function () {
    return this.lastName + " " + this.firstName + " (" + this.email + ")";
  };
  return userResource;
}]);

angular.module('security.authorization', ['security.service'])

// This service provides guard methods to support AngularJS routes.
// You can add them as resolves to routes to require authorization levels
// before allowing a route change to complete
.provider('securityAuthorization', {

  requireAdminUser: ['securityAuthorization', function(securityAuthorization) {
    return securityAuthorization.requireAdminUser();
  }],

  requireAuthenticatedUser: ['securityAuthorization', function(securityAuthorization) {
    return securityAuthorization.requireAuthenticatedUser();
  }],

  $get: ['security', 'securityRetryQueue', function(security, queue) {
    var service = {

      // Require that there is an authenticated user
      // (use this in a route resolve to prevent non-authenticated users from entering that route)
      requireAuthenticatedUser: function() {
        var promise = security.requestCurrentUser().then(function(userInfo) {
          if ( !security.isAuthenticated() ) {
            return queue.pushRetryFn('unauthenticated-client', service.requireAuthenticatedUser);
          }
        });
        return promise;
      },

      // Require that there is an administrator logged in
      // (use this in a route resolve to prevent non-administrators from entering that route)
      requireAdminUser: function() {
        var promise = security.requestCurrentUser().then(function(userInfo) {
          if ( !security.isAdmin() ) {
            return queue.pushRetryFn('unauthorized-client', service.requireAdminUser);
          }
        });
        return promise;
      }

    };

    return service;
  }]
});
// Based loosely around work by Witold Szczerba - https://github.com/witoldsz/angular-http-auth
angular.module('security', [
  'security.service',
  'security.interceptor',
  'security.login',
  'security.authorization']);

angular.module('security.interceptor', ['security.retryQueue'])

// This http interceptor listens for authentication failures
.factory('securityInterceptor', ['$injector', 'securityRetryQueue', function($injector, queue) {
  return {
	'responseError': function(originalResponse) {
      if(originalResponse.status === 401) {
        // The request bounced because it was not authorized - add a new request to the retry queue
        promise = queue.pushRetryFn('unauthorized-server', function retryRequest() {
          // We must use $injector to get the $http service to prevent circular dependency
          return $injector.get('$http')(originalResponse.config);
        });
      }
      return promise;
    }
  };
}])

// We have to add the interceptor to the queue as a string because the interceptor depends upon service instances that are not available in the config block.
.config(['$httpProvider', function($httpProvider) {
  $httpProvider.interceptors.push('securityInterceptor');
}]);
angular.module('security.login.form', ['services.localizedMessages'])

// The LoginFormController provides the behaviour behind a reusable form to allow users to authenticate.
// This controller and its template (login/form.tpl.html) are used in a modal dialog box by the security service.
.controller('LoginFormController', ['$scope', 'security', 'localizedMessages', function($scope, security, localizedMessages) {
  // The model for this form 
  $scope.user = {};

  // Any error message from failing to login
  $scope.authError = null;

  // The reason that we are being asked to login - for instance because we tried to access something to which we are not authorized
  // We could do something diffent for each reason here but to keep it simple...
  $scope.authReason = null;
  if ( security.getLoginReason() ) {
    $scope.authReason = ( security.isAuthenticated() ) ?
      localizedMessages.get('login.reason.notAuthorized') :
      localizedMessages.get('login.reason.notAuthenticated');
  }

  // Attempt to authenticate the user specified in the form's model
  $scope.login = function() {
    // Clear any previous security errors
    $scope.authError = null;

    // Try to login
    security.login($scope.user.email, $scope.user.password).then(function(loggedIn) {
      if ( !loggedIn ) {
        // If we get here then the login failed due to bad credentials
        $scope.authError = localizedMessages.get('login.error.invalidCredentials');
      }
    }, function(x) {
      // If we get here then there was a problem with the login request to the server
      $scope.authError = localizedMessages.get('login.error.serverError', { exception: x });
    });
  };

  $scope.clearForm = function() {
    $scope.user = {};
  };

  $scope.cancelLogin = function() {
    security.cancelLogin();
  };
}]);

angular.module('security.login', ['security.login.form', 'security.login.toolbar']);
angular.module('security.login.toolbar', ['services.utilMethods'], ['$routeProvider', function($routeProvider){

  $routeProvider.when('/profile', {
    templateUrl:'security/login/profile-edit.tpl.html',
    controller:'EditProfileCtrl',
	resolve:{		
		user:['Users', 'security', function (Users, security) {
			return security.requestCurrentUser().then(function(value){return Users.getById(security.currentUser.id);});
		}]
	}
  });}])
.controller('EditProfileCtrl', ['$scope', '$location', 'i18nNotifications', 'user', 'security', 'utilMethods', function ($scope, $location, i18nNotifications, user, security, utilMethods) {

  $scope.user = user;
  $scope.password = user.password;

  $scope.fileChanged = utilMethods.fileInputOfUserViewChanged($scope);

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
angular.module('security.retryQueue', [])

// This is a generic retry queue for security failures.  Each item is expected to expose two functions: retry and cancel.
.factory('securityRetryQueue', ['$q', '$log', function($q, $log) {
  var retryQueue = [];
  var service = {
    // The security service puts its own handler in here!
    onItemAddedCallbacks: [],
    
    hasMore: function() {
      return retryQueue.length > 0;
    },
    push: function(retryItem) {
      retryQueue.push(retryItem);
      // Call all the onItemAdded callbacks
      angular.forEach(service.onItemAddedCallbacks, function(cb) {
        try {
          cb(retryItem);
        } catch(e) {
          $log.error('securityRetryQueue.push(retryItem): callback threw an error' + e);
        }
      });
    },
    pushRetryFn: function(reason, retryFn) {
      // The reason parameter is optional
      if ( arguments.length === 1) {
        retryFn = reason;
        reason = undefined;
      }

      // The deferred object that will be resolved or rejected by calling retry or cancel
      var deferred = $q.defer();
      var retryItem = {
        reason: reason,
        retry: function() {
          // Wrap the result of the retryFn into a promise if it is not already
          $q.when(retryFn()).then(function(value) {
            // If it was successful then resolve our deferred
            deferred.resolve(value);
          }, function(value) {
            // Othewise reject it
            deferred.reject(value);
          });
        },
        cancel: function() {
          // Give up on retrying and reject our deferred
          deferred.reject();
        }
      };
      service.push(retryItem);
      return deferred.promise;
    },
    retryReason: function() {
      return service.hasMore() && retryQueue[0].reason;
    },
    cancelAll: function() {
      while(service.hasMore()) {
        retryQueue.shift().cancel();
      }
    },
    retryAll: function() {
      while(service.hasMore()) {
        retryQueue.shift().retry();
      }
    }
  };
  return service;
}]);

// Based loosely around work by Witold Szczerba - https://github.com/witoldsz/angular-http-auth
angular.module('security.service', [
  'security.retryQueue',    // Keeps track of failed requests that need to be retried once the user logs in
  'security.login'         // Contains the login form template and controller
])

.factory('security', ['$http', '$q', '$location', 'securityRetryQueue', '$modal', function($http, $q, $location, queue, $modal) {

  // Redirect to the given url (defaults to '/')
  function redirect(url) {
    url = url || '/';
    $location.path(url);
  }

  // Login form dialog stuff
  var loginDialog = null;
  function openLoginDialog() {
    if ( loginDialog ) {
      throw new Error('Trying to open a dialog that is already open!');
    }
    loginDialog = $modal.open({templateUrl:'security/login/form.tpl.html', controller:'LoginFormController'});
    loginDialog.result.then(onLoginDialogClose);
  }
  function closeLoginDialog(success) {
    if (loginDialog) {
      loginDialog.close(success);
    }
  }
  function onLoginDialogClose(success) {
    loginDialog = null;
    if ( success ) {
      queue.retryAll();
    } else {
      queue.cancelAll();
      redirect();
    }
  }

  // Register a handler for when an item is added to the retry queue
  queue.onItemAddedCallbacks.push(function(retryItem) {
    if ( queue.hasMore() ) {
      service.showLogin();
    }
  });

  // The public API of the service
  var service = {

    // Get the first reason for needing a login
    getLoginReason: function() {
      return queue.retryReason();
    },

    // Show the modal login dialog
    showLogin: function() {
      openLoginDialog();
    },

    // Attempt to authenticate a user by the given email and password
    login: function(email, password) {
      var request = $http.post('/login', {email: email, password: password});
      return request.then(function(response) {
        service.currentUser = response.data.user;
        if ( service.isAuthenticated() ) {
          closeLoginDialog(true);
        }
        return service.isAuthenticated();
      });
    },

    // Give up trying to login and clear the retry queue
    cancelLogin: function() {
      closeLoginDialog(false);
      redirect();
    },

    // Logout the current user and redirect
    logout: function(redirectTo) {
      $http.post('/logout').then(function() {
        service.currentUser = null;
        redirect(redirectTo);
      });
    },

    // Ask the backend to see if a user is already authenticated - this may be from a previous session.
    requestCurrentUser: function() {
      if ( service.isAuthenticated() ) {
        return $q.when(service.currentUser);
      } else {
        return $http.get('/current-user').then(function(response) {
          service.currentUser = response.data.user;
          return service.currentUser;
        });
      }
    },

    // Information about the current user
    currentUser: null,

    // Is the current user authenticated?
    isAuthenticated: function(){
      return !!service.currentUser;
    },
    
    // Is the current user an adminstrator?
    isAdmin: function() {
      return !!(service.currentUser && service.currentUser.admin);
    }
  };

  return service;
}]);

angular.module('services.breadcrumbs', []);
angular.module('services.breadcrumbs').factory('breadcrumbs', ['$rootScope', '$location', function($rootScope, $location){

  var breadcrumbs = [];
  var breadcrumbsService = {};

  //we want to update breadcrumbs only when a route is actually changed
  //as $location.path() will get updated imediatelly (even if route change fails!)
  $rootScope.$on('$routeChangeSuccess', function(event, current){

    var pathElements = $location.path().split('/'), result = [], i;
    var breadcrumbPath = function (index) {
      return '/' + (pathElements.slice(0, index + 1)).join('/');
    };

    pathElements.shift();
    for (i=0; i<pathElements.length; i++) {
      result.push({name: pathElements[i], path: breadcrumbPath(i)});
    }

    breadcrumbs = result;
  });

  breadcrumbsService.getAll = function() {
    return breadcrumbs;
  };

  breadcrumbsService.getFirst = function() {
    return breadcrumbs[0] || {};
  };

  return breadcrumbsService;
}]);
angular.module('services.crud', ['services.crudRouteProvider']);
angular.module('services.crud').factory('crudEditMethods', function () {

  return function (itemName, item, formName, successcb, errorcb) {

    var mixin = {};

    mixin[itemName] = item;
    mixin[itemName+'Copy'] = angular.copy(item);

    mixin.save = function () {
      this[itemName].$saveOrUpdate(successcb, successcb, errorcb, errorcb);
    };

    mixin.canSave = function () {
      return this[formName].$valid && !angular.equals(this[itemName], this[itemName+'Copy']);
    };

    mixin.revertChanges = function () {
      this[itemName] = angular.copy(this[itemName+'Copy']);
    };

    mixin.canRevert = function () {
      return !angular.equals(this[itemName], this[itemName+'Copy']);
    };

    mixin.remove = function () {
      if (this[itemName].$id()) {
        this[itemName].$remove(successcb, errorcb);
      } else {
        successcb();
      }
    };

    mixin.canRemove = function() {
      return item.$id();
    };

    /**
     * Get the CSS classes for this item, to be used by the ng-class directive
     * @param {string} fieldName The name of the field on the form, for which we want to get the CSS classes
     * @return {object} A hash where each key is a CSS class and the corresponding value is true if the class is to be applied.
     */
    mixin.getCssClasses = function(fieldName) {
      var ngModelController = this[formName][fieldName];
      return {
        error: ngModelController.$invalid && ngModelController.$dirty,
        success: ngModelController.$valid && ngModelController.$dirty
      };
    };

    /**
     * Whether to show an error message for the specified error
     * @param {string} fieldName The name of the field on the form, of which we want to know whether to show the error
     * @param  {string} error - The name of the error as given by a validation directive
     * @return {Boolean} true if the error should be shown
     */
    mixin.showError = function(fieldName, error) {
      return this[formName][fieldName].$error[error];
    };

    return mixin;
  };
});

angular.module('services.crud').factory('crudListMethods', ['$location', function ($location) {

  return function (pathPrefix) {

    var mixin = {};

    mixin['new'] = function () {
      $location.path(pathPrefix+'/new');
    };

    mixin['edit'] = function (itemId) {
      $location.path(pathPrefix+'/'+itemId);
    };

    return mixin;
  };
}]);
(function() {

  function crudRouteProvider($routeProvider) {

    // This $get noop is because at the moment in AngularJS "providers" must provide something
    // via a $get method.
    // When AngularJS has "provider helpers" then this will go away!
    this.$get = angular.noop;

    // Again, if AngularJS had "provider helpers" we might be able to return `routesFor()` as the
    // crudRouteProvider itself.  Then we would have a much cleaner syntax and not have to do stuff
    // like:
    //
    // ```
    // myMod.config(function(crudRouteProvider) {
    //   var routeProvider = crudRouteProvider.routesFor('MyBook', '/myApp');
    // });
    // ```
    //
    // but instead have something like:
    //
    //
    // ```
    // myMod.config(function(crudRouteProvider) {
    //   var routeProvider = crudRouteProvider('MyBook', '/myApp');
    // });
    // ```
    //
    // In any case, the point is that this function is the key part of this "provider helper".
    // We use it to create routes for CRUD operations.  We give it some basic information about
    // the resource and the urls then it it returns our own special routeProvider.
    this.routesFor = function(resourceName, urlPrefix, routePrefix) {
      var baseUrl = resourceName.toLowerCase();
      var baseRoute = '/' + resourceName.toLowerCase();
      routePrefix = routePrefix || urlPrefix;

      // Prepend the urlPrefix if available.
      if ( angular.isString(urlPrefix) && urlPrefix !== '' ) {
        baseUrl = urlPrefix + '/' + baseUrl;
      }

      // Prepend the routePrefix if it was provided;
      if (routePrefix !== null && routePrefix !== undefined && routePrefix !== '') {
        baseRoute = '/' + routePrefix + baseRoute;
      }

      // Create the templateUrl for a route to our resource that does the specified operation.
      var templateUrl = function(operation) {
        return baseUrl + '/' + resourceName.toLowerCase() +'-'+operation.toLowerCase()+'.tpl.html';
      };
      // Create the controller name for a route to our resource that does the specified operation.
      var controllerName = function(operation) {
        return resourceName + operation +'Ctrl';
      };

      // This is the object that our `routesFor()` function returns.  It decorates `$routeProvider`,
      // delegating the `when()` and `otherwise()` functions but also exposing some new functions for
      // creating CRUD routes.  Specifically we have `whenList(), `whenNew()` and `whenEdit()`.
      var routeBuilder = {
        // Create a route that will handle showing a list of items
        whenList: function(resolveFns) {
          routeBuilder.when(baseRoute, {
            templateUrl: templateUrl('List'),
            controller: controllerName('List'),
            resolve: resolveFns
          });
          return routeBuilder;
        },
        // Create a route that will handle creating a new item
        whenNew: function(resolveFns) {
          routeBuilder.when(baseRoute +'/new', {
            templateUrl: templateUrl('Create'),
            controller: controllerName('Edit'),
            resolve: resolveFns
          });
          return routeBuilder;
        },
        // Create a route that will handle editing an existing item
        whenEdit: function(resolveFns) {
          routeBuilder.when(baseRoute+'/:itemId', {
            templateUrl: templateUrl('Edit'),
            controller: controllerName('Edit'),
            resolve: resolveFns
          });
          return routeBuilder;
        },
        // Pass-through to `$routeProvider.when()`
        when: function(path, route) {
          $routeProvider.when(path, route);
          return routeBuilder;
        },
        // Pass-through to `$routeProvider.otherwise()`
        otherwise: function(params) {
          $routeProvider.otherwise(params);
          return routeBuilder;
        },
        // Access to the core $routeProvider.
        $routeProvider: $routeProvider
      };
      return routeBuilder;
    };
  }
  // Currently, v1.0.3, AngularJS does not provide annotation style dependencies in providers so,
  // we add our injection dependencies using the $inject form
  crudRouteProvider.$inject = ['$routeProvider'];

  // Create our provider - it would be nice to be able to do something like this instead:
  //
  // ```
  // angular.module('services.crudRouteProvider', [])
  //   .configHelper('crudRouteProvider', ['$routeProvider, crudRouteProvider]);
  // ```
  // Then we could dispense with the $get, the $inject and the closure wrapper around all this.
  angular.module('services.crudRouteProvider', ['ngRoute']).provider('crudRoute', crudRouteProvider);
})();

angular.module('services.exceptionHandler', ['services.i18nNotifications']);

angular.module('services.exceptionHandler').factory('exceptionHandlerFactory', ['$injector', function($injector) {
  return function($delegate) {

    return function (exception, cause) {
      // Lazy load notifications to get around circular dependency
      //Circular dependency: $rootScope <- notifications <- i18nNotifications <- $exceptionHandler
      var i18nNotifications = $injector.get('i18nNotifications');

      // Pass through to original handler
      $delegate(exception, cause);

      // Push a notification error
      i18nNotifications.pushForCurrentRoute('error.fatal', 'error', {}, {
        exception:exception,
        cause:cause
      });
    };
  };
}]);

angular.module('services.exceptionHandler').config(['$provide', function($provide) {
  $provide.decorator('$exceptionHandler', ['$delegate', 'exceptionHandlerFactory', function ($delegate, exceptionHandlerFactory) {
    return exceptionHandlerFactory($delegate);
  }]);
}]);

angular.module('services.httpRequestTracker', []);
angular.module('services.httpRequestTracker').factory('httpRequestTracker', ['$http', function($http){

  var httpRequestTracker = {};
  httpRequestTracker.hasPendingRequests = function() {
    return $http.pendingRequests.length > 0;
  };

  return httpRequestTracker;
}]);
angular.module('services.i18nNotifications', ['services.notifications', 'services.localizedMessages']);
angular.module('services.i18nNotifications').factory('i18nNotifications', ['localizedMessages', 'notifications', function (localizedMessages, notifications) {

  var prepareNotification = function(msgKey, type, interpolateParams, otherProperties) {
     return angular.extend({
       message: localizedMessages.get(msgKey, interpolateParams),
       type: type
     }, otherProperties);
  };

  var I18nNotifications = {
    pushSticky:function (msgKey, type, interpolateParams, otherProperties) {
      return notifications.pushSticky(prepareNotification(msgKey, type, interpolateParams, otherProperties));
    },
    pushForCurrentRoute:function (msgKey, type, interpolateParams, otherProperties) {
      return notifications.pushForCurrentRoute(prepareNotification(msgKey, type, interpolateParams, otherProperties));
    },
    pushForNextRoute:function (msgKey, type, interpolateParams, otherProperties) {
      return notifications.pushForNextRoute(prepareNotification(msgKey, type, interpolateParams, otherProperties));
    },
    getCurrent:function () {
      return notifications.getCurrent();
    },
    remove:function (notification) {
      return notifications.remove(notification);
    }
  };

  return I18nNotifications;
}]);
angular.module('services.localizedMessages', []).factory('localizedMessages', ['$interpolate', 'I18N.MESSAGES', function ($interpolate, i18nmessages) {

  var handleNotFound = function (msg, msgKey) {
    return msg || '?' + msgKey + '?';
  };

  return {
    get : function (msgKey, interpolateParams) {
      var msg =  i18nmessages[msgKey];
      if (msg) {
        return $interpolate(msg)(interpolateParams);
      } else {
        return handleNotFound(msg, msgKey);
      }
    }
  };
}]);
angular.module('services.notifications', []).factory('notifications', ['$rootScope', function ($rootScope) {

  var notifications = {
    'STICKY' : [],
    'ROUTE_CURRENT' : [],
    'ROUTE_NEXT' : []
  };
  var notificationsService = {};

  var addNotification = function (notificationsArray, notificationObj) {
    if (!angular.isObject(notificationObj)) {
      throw new Error("Only object can be added to the notification service");
    }
    notificationsArray.push(notificationObj);
    return notificationObj;
  };

  $rootScope.$on('$routeChangeSuccess', function () {
    notifications.ROUTE_CURRENT.length = 0;

    notifications.ROUTE_CURRENT = angular.copy(notifications.ROUTE_NEXT);
    notifications.ROUTE_NEXT.length = 0;
  });

  notificationsService.getCurrent = function(){
    return [].concat(notifications.STICKY, notifications.ROUTE_CURRENT);
  };

  notificationsService.pushSticky = function(notification) {
    return addNotification(notifications.STICKY, notification);
  };

  notificationsService.pushForCurrentRoute = function(notification) {
    return addNotification(notifications.ROUTE_CURRENT, notification);
  };

  notificationsService.pushForNextRoute = function(notification) {
    return addNotification(notifications.ROUTE_NEXT, notification);
  };

  notificationsService.remove = function(notification){
    angular.forEach(notifications, function (notificationsByType) {
      var idx = notificationsByType.indexOf(notification);
      if (idx>-1){
        notificationsByType.splice(idx,1);
      }
    });
  };

  notificationsService.removeAll = function(){
    angular.forEach(notifications, function (notificationsByType) {
      notificationsByType.length = 0;
    });
  };

  return notificationsService;
}]);
angular.module('services.utilMethods', []);
angular.module('services.utilMethods').factory('utilMethods', [function(){

  var utilMethodsService = {};
  var container = {};
  utilMethodsService.fileInputOfUserViewChanged = function (scope) {
        return function (fileInput) {
            var file = fileInput.files[0];
            var FR= new FileReader();
            FR.onload = function(e) {
                scope.user.base64Image = e.target.result;
                scope.$digest();
            };
            FR.readAsDataURL( file );
        }
  };

    utilMethodsService.save = function(key, value){
        container[key] = value;
    }

    utilMethodsService.get = function(key){
        return container[key];
    }

  return utilMethodsService;
}]);
angular.module('templates.app', ['admin/users/column-header.tpl.html', 'admin/users/users-create.tpl.html', 'admin/users/users-edit.tpl.html', 'admin/users/users-list.tpl.html', 'calendar/list.tpl.html', 'header.tpl.html', 'notifications.tpl.html']);

angular.module("admin/users/column-header.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("admin/users/column-header.tpl.html",
    "<a href=\"#\" ng-click=\"headerClick()\">\n" +
    "    {{columnHeader}}\n" +
    "    <span ng-show=\"sortingField == columnField\">\n" +
    "        <span ng-show=\"reverse == 'false'\">\n" +
    "            <i class=\"icon-chevron-down\"></i>\n" +
    "        </span>\n" +
    "        <span ng-show=\"reverse == 'true'\">\n" +
    "            <i class=\"icon-chevron-up\"></i>\n" +
    "        </span>\n" +
    "    </span>\n" +
    "</a>");
}]);

angular.module("admin/users/users-create.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("admin/users/users-create.tpl.html",
    "<div class=\"well\">\n" +
    "    <form name=\"form\" novalidate crud-edit=\"user\">\n" +
    "        <legend>User</legend>\n" +
    "        <input type=\"file\" style=\"display:none\"\n" +
    "               id=\"file\" name='file' onchange=\"angular.element(this).scope().fileChanged(this)\" />\n" +
    "        <img  width=\"200\" height=\"200\" ng-src=\"{{user.base64Image}}\" onclick=\"$('#file').click();\" class=\"img-polaroid pull-right\"></gravatar>\n" +
    "        <label for=\"email\">E-mail</label>\n" +
    "        <input class=\"span6\" type=\"email\" id=\"email\" name=\"email\" ng-model=\"user.email\" required unique-email>\n" +
    "        <span ng-show=\"showError('email', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "        <span ng-show=\"showError('email', 'email')\" class=\"help-inline\">Please enter a valid email address.</span>\n" +
    "        <span ng-show=\"showError('email', 'uniqueEmail')\" class=\"help-inline\">This email address is not available - please enter another.</span>\n" +
    "        <label for=\"name\">Name</label>\n" +
    "        <input class=\"span6\" type=\"text\" id=\"name\" name=\"name\" ng-model=\"user.name\" required>\n" +
    "        <span ng-show=\"showError('name', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "        <label for=\"password\">Password</label>\n" +
    "        <input class=\"span6\" type=\"password\" id=\"password\" name=\"password\" ng-model=\"user.password\" required>\n" +
    "        <span ng-show=\"showError('password', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "        <span ng-show=\"showError('passwordRepeat', 'equal')\" class=\"help-inline\">Passwords do not match.</span>\n" +
    "        <label for=\"passwordRepeat\">Password (repeat)</label>\n" +
    "        <input class=\"span6\" type=\"password\" id=\"passwordRepeat\" name=\"passwordRepeat\" ng-model=\"password\" required validate-equals=\"user.password\">\n" +
    "        <span ng-show=\"showError('passwordRepeat', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "        <span ng-show=\"showError('passwordRepeat', 'equal')\" class=\"help-inline\">Passwords do not match.</span>\n" +
    "        <label>Admin</label>\n" +
    "        <input type=\"checkbox\" ng-model=\"user.admin\">\n" +
    "        <div ng-if=\"!user.admin\">\n" +
    "            <label for=\"street\">Street</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"street\" name=\"street\" ng-model=\"user.street\" required>\n" +
    "            <span ng-show=\"showError('street', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label for=\"address\">Address</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"address\" name=\"address\" ng-model=\"user.address\">\n" +
    "            <label for=\"plz\">PLZ</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"plz\" name=\"plz\" ng-model=\"user.plz\" required>\n" +
    "            <span ng-show=\"showError('plz', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label for=\"city\">City</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"city\" name=\"city\" ng-model=\"user.city\" required>\n" +
    "            <span ng-show=\"showError('city', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label for=\"telephoneNumber\">Telephone number</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"telephoneNumber\" name=\"telephoneNumber\" ng-model=\"user.telephoneNumber\" required>\n" +
    "            <span ng-show=\"showError('telephoneNumber', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label>Transport type</label>\n" +
    "            <label><input name=\"transportType\" type=\"radio\" ng-model=\"user.transportType\" value=\"bicycle\" ng-required=\"!user.transportType\">Bicycle</label>\n" +
    "            <label><input name=\"transportType\" type=\"radio\" ng-model=\"user.transportType\" value=\"auto\" ng-required=\"!user.transportType\">Auto</label>\n" +
    "            <span ng-show=\"showError('transportType', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label>Telephone type</label>\n" +
    "            <label><input name=\"telephoneType\" type=\"radio\" ng-model=\"user.telephoneType\" value=\"ios\" ng-required=\"!user.telephoneType\">IOS</label>\n" +
    "            <label><input name=\"telephoneType\" type=\"radio\" ng-model=\"user.telephoneType\" value=\"android\" ng-required=\"!user.telephoneType\">Android</label>\n" +
    "            <span ng-show=\"showError('telephoneType', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label for=\"iban\">IBAN</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"iban\" name=\"iban\" ng-model=\"user.iban\" required>\n" +
    "            <span ng-show=\"showError('iban', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label for=\"bic\">BIC</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"bic\" name=\"bic\" ng-model=\"user.bic\" required>\n" +
    "            <span ng-show=\"showError('bic', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label>Contract type</label>\n" +
    "            <label><input name=\"contractType\" type=\"radio\" ng-model=\"user.contractType\" value=\"minijob\" ng-required=\"!user.contractType\">Minijob</label>\n" +
    "            <label><input name=\"contractType\" type=\"radio\" ng-model=\"user.contractType\" value=\"flexible\" ng-required=\"!user.contractType\">Flexible</label>\n" +
    "            <span ng-show=\"showError('contractType', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "        </div>\n" +
    "        <hr>\n" +
    "        <crud-buttons></crud-buttons>\n" +
    "    </form>\n" +
    "</div>");
}]);

angular.module("admin/users/users-edit.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("admin/users/users-edit.tpl.html",
    "<div class=\"well\">\n" +
    "    <form name=\"form\" novalidate crud-edit=\"user\">\n" +
    "        <legend>User</legend>\n" +
    "        <input type=\"file\" style=\"display:none\"\n" +
    "               id=\"file\" name='file' onchange=\"angular.element(this).scope().fileChanged(this)\" />\n" +
    "        <img  width=\"200\" height=\"200\" ng-src=\"{{user.base64Image}}\" onclick=\"$('#file').click();\" class=\"img-polaroid pull-right\"></gravatar>\n" +
    "        <label for=\"email\">E-mail</label>\n" +
    "        <input class=\"span6\" type=\"email\" id=\"email\" name=\"email\" ng-model=\"user.email\" disabled>\n" +
    "        <label for=\"name\">Name</label>\n" +
    "        <input class=\"span6\" type=\"text\" id=\"name\" name=\"name\" ng-model=\"user.name\" required>\n" +
    "        <span ng-show=\"showError('name', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "        <label for=\"password\">Password</label>\n" +
    "        <input class=\"span6\" type=\"password\" id=\"password\" name=\"password\" ng-model=\"user.password\" required>\n" +
    "        <span ng-show=\"showError('password', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "        <span ng-show=\"showError('passwordRepeat', 'equal')\" class=\"help-inline\">Passwords do not match.</span>\n" +
    "        <label for=\"passwordRepeat\">Password (repeat)</label>\n" +
    "        <input class=\"span6\" type=\"password\" id=\"passwordRepeat\" name=\"passwordRepeat\" ng-model=\"password\" required validate-equals=\"user.password\">\n" +
    "        <span ng-show=\"showError('passwordRepeat', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "        <span ng-show=\"showError('passwordRepeat', 'equal')\" class=\"help-inline\">Passwords do not match.</span>\n" +
    "        <label>Admin</label>\n" +
    "        <input type=\"checkbox\" ng-model=\"user.admin\">\n" +
    "        <div ng-if=\"!user.admin\">\n" +
    "            <label for=\"street\">Street</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"street\" name=\"street\" ng-model=\"user.street\" required>\n" +
    "            <span ng-show=\"showError('street', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label for=\"address\">Address</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"address\" name=\"address\" ng-model=\"user.address\">\n" +
    "            <label for=\"plz\">PLZ</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"plz\" name=\"plz\" ng-model=\"user.plz\" required>\n" +
    "            <span ng-show=\"showError('plz', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label for=\"city\">City</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"city\" name=\"city\" ng-model=\"user.city\" required>\n" +
    "            <span ng-show=\"showError('city', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label for=\"telephoneNumber\">Telephone number</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"telephoneNumber\" name=\"telephoneNumber\" ng-model=\"user.telephoneNumber\" required>\n" +
    "            <span ng-show=\"showError('telephoneNumber', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label>Transport type</label>\n" +
    "            <label><input name=\"transportType\" type=\"radio\" ng-model=\"user.transportType\" value=\"bicycle\" ng-required=\"!user.transportType\">Bicycle</label>\n" +
    "            <label><input name=\"transportType\" type=\"radio\" ng-model=\"user.transportType\" value=\"auto\" ng-required=\"!user.transportType\">Auto</label>\n" +
    "            <span ng-show=\"showError('transportType', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label>Telephone type</label>\n" +
    "            <label><input name=\"telephoneType\" type=\"radio\" ng-model=\"user.telephoneType\" value=\"ios\" ng-required=\"!user.telephoneType\">IOS</label>\n" +
    "            <label><input name=\"telephoneType\" type=\"radio\" ng-model=\"user.telephoneType\" value=\"android\" ng-required=\"!user.telephoneType\">Android</label>\n" +
    "            <span ng-show=\"showError('telephoneType', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label for=\"iban\">IBAN</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"iban\" name=\"iban\" ng-model=\"user.iban\" required>\n" +
    "            <span ng-show=\"showError('iban', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label for=\"bic\">BIC</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"bic\" name=\"bic\" ng-model=\"user.bic\" required>\n" +
    "            <span ng-show=\"showError('bic', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label>Contract type</label>\n" +
    "            <label><input name=\"contractType\" type=\"radio\" ng-model=\"user.contractType\" value=\"minijob\" ng-required=\"!user.contractType\">Minijob</label>\n" +
    "            <label><input name=\"contractType\" type=\"radio\" ng-model=\"user.contractType\" value=\"flexible\" ng-required=\"!user.contractType\">Flexible</label>\n" +
    "            <span ng-show=\"showError('contractType', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "        </div>\n" +
    "        <hr>\n" +
    "        <crud-buttons></crud-buttons>\n" +
    "    </form>\n" +
    "</div>");
}]);

angular.module("admin/users/users-list.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("admin/users/users-list.tpl.html",
    "<div class=\"well\">\n" +
    "    <span>{{totalItems}} users in database. </span><br/>\n" +
    "    <span>Users per page: </span>\n" +
    "    <select ng-model=\"itemsPerPageStr\" ng-change=\"changePageVolume()\">\n" +
    "        <option value=\"5\" ng-selected=\"itemsPerPage==5\">5</option>\n" +
    "        <option value=\"10\" ng-selected=\"itemsPerPage==10\">10</option>\n" +
    "        <option value=\"20\" ng-selected=\"itemsPerPage==20\">20</option>\n" +
    "        <option value=\"50\" ng-selected=\"itemsPerPage==50\">50</option>\n" +
    "    </select><br/>\n" +
    "    <button class=\"btn\" ng-click=\"new()\">New User</button>\n" +
    "</div>\n" +
    "<pagination ng-change=\"pageChanged()\" items-per-page=\"itemsPerPage\" total-items=\"totalItems\" ng-model=\"currentPage\" max-size=\"maxSize\" class=\"pagination-sm\" boundary-links=\"true\"></pagination>\n" +
    "<table class=\"table table-bordered table-condensed table-striped table-hover\">\n" +
    "    <thead>\n" +
    "    <tr>\n" +
    "        <th></th>\n" +
    "        <th column-header=\"E-mail\" column-field=\"email\" sorting-field=\"{{sortingField}}\" reverse=\"{{reverse}}\" sort=\"sort(field)\"></th>\n" +
    "        <th column-header=\"Name\" column-field=\"name\" sorting-field=\"{{sortingField}}\"  reverse=\"{{reverse}}\"sort=\"sort(field)\"></th>\n" +
    "    </tr>\n" +
    "    </thead>\n" +
    "    <tbody>\n" +
    "    <tr ng-repeat=\"user in users\" ng-click=\"editUser(user.$id())\">\n" +
    "        <td><gravatar email=\"user.email\" size=\"50\" default-image=\"'monsterid'\"></gravatar></td>\n" +
    "        <td>{{user.email}}</td>\n" +
    "        <td>{{user.name}}</td>\n" +
    "        <td><button class=\"btn btn-danger remove\" ng-click=\"remove(user, $index, $event)\">Remove</button></td>\n" +
    "    </tr>\n" +
    "    </tbody>\n" +
    "</table>\n" +
    "<pagination ng-change=\"pageChanged()\" items-per-page=\"itemsPerPage\" total-items=\"totalItems\" ng-model=\"currentPage\" max-size=\"maxSize\" class=\"pagination-sm\" boundary-links=\"true\"></pagination>\n" +
    "");
}]);

angular.module("calendar/list.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("calendar/list.tpl.html",
    "<h3>My calendars <i class=\"icon-upload\"></i></h3>");
}]);

angular.module("header.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("header.tpl.html",
    "<div class=\"navbar\" ng-controller=\"HeaderCtrl\">\n" +
    "    <div class=\"navbar-inner\">\n" +
    "        <div class=\"container\">\n" +
    "            <a class=\"btn btn-navbar\" data-toggle=\"collapse\" data-target=\".nav-collapse\">\n" +
    "                <span class=\"icon-bar\"></span>\n" +
    "                <span class=\"icon-bar\"></span>\n" +
    "                <span class=\"icon-bar\"></span>\n" +
    "            </a>\n" +
    "            <a class=\"brand\" ng-click=\"home()\">VoloCalendar</a>\n" +
    "            <div class=\"nav-collapse\">\n" +
    "                <ul class=\"nav\">\n" +
    "                    <li ng-class=\"{active:isNavbarActive('calendar')}\"><a href=\"/calendar\">My calendar</a></li>\n" +
    "                    <li class=\"dropdown\" ng-class=\"{active:isNavbarActive('admin'), open:isAdminOpen}\" ng-show=\"isAdmin()\">\n" +
    "                        <a id=\"adminmenu\" role=\"button\" class=\"dropdown-toggle\" ng-click=\"isAdminOpen=!isAdminOpen\">Admin<b class=\"caret\"></b></a>\n" +
    "                        <ul class=\"dropdown-menu\" role=\"menu\" aria-labelledby=\"adminmenu\">\n" +
    "                            <li><a tabindex=\"-1\" href=\"/admin/users\" ng-click=\"isAdminOpen=false\">Manage Users</a></li>\n" +
    "                        </ul>\n" +
    "                    </li>\n" +
    "                </ul>\n" +
    "                <login-toolbar></login-toolbar>\n" +
    "                <ul class=\"nav pull-right\" ng-show=\"hasPendingRequests()\">\n" +
    "                    <li class=\"divider-vertical\"></li>\n" +
    "                    <li><a href=\"#\"><img src=\"/static/img/spinner.gif\"></a></li>\n" +
    "                </ul>\n" +
    "            </div><!-- /.nav-collapse -->\n" +
    "        </div>\n" +
    "    </div><!-- /navbar-inner -->\n" +
    "    <div>\n" +
    "        <ul class=\"breadcrumb\">\n" +
    "            <li ng-repeat=\"breadcrumb in breadcrumbs.getAll()\">\n" +
    "                <span class=\"divider\">/</span>\n" +
    "                <ng-switch on=\"$last\">\n" +
    "                    <span ng-switch-when=\"true\">{{breadcrumb.name}}</span>\n" +
    "                    <span ng-switch-default><a href=\"{{breadcrumb.path}}\">{{breadcrumb.name}}</a></span>\n" +
    "                </ng-switch>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "    </div>\n" +
    "</div>");
}]);

angular.module("notifications.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("notifications.tpl.html",
    "<div ng-class=\"['alert', 'alert-'+notification.type]\" ng-repeat=\"notification in notifications.getCurrent()\">\n" +
    "    <button class=\"close\" ng-click=\"removeNotification(notification)\">x</button>\n" +
    "    {{notification.message}}\n" +
    "</div>\n" +
    "");
}]);

angular.module('templates.common', ['security/login/form.tpl.html', 'security/login/profile-edit.tpl.html', 'security/login/toolbar.tpl.html']);

angular.module("security/login/form.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("security/login/form.tpl.html",
    "<form name=\"form\" novalidate class=\"login-form\">\n" +
    "    <div class=\"modal-header\">\n" +
    "        <h4>Sign in</h4>\n" +
    "    </div>\n" +
    "    <div class=\"modal-body\">\n" +
    "        <div class=\"alert alert-warning\" ng-show=\"authReason\">\n" +
    "            {{authReason}}\n" +
    "        </div>\n" +
    "        <div class=\"alert alert-error\" ng-show=\"authError\">\n" +
    "            {{authError}}\n" +
    "        </div>\n" +
    "        <div class=\"alert alert-info\">Please enter your login details</div>\n" +
    "        <label>E-mail</label>\n" +
    "        <input name=\"login\" type=\"email\" ng-model=\"user.email\" required autofocus>\n" +
    "        <label>Password</label>\n" +
    "        <input name=\"pass\" type=\"password\" ng-model=\"user.password\" required>\n" +
    "    </div>\n" +
    "    <div class=\"modal-footer\">\n" +
    "        <button class=\"btn btn-primary login\" ng-click=\"login()\" ng-disabled='form.$invalid'>Sign in</button>\n" +
    "        <button class=\"btn clear\" ng-click=\"clearForm()\">Clear</button>\n" +
    "        <button class=\"btn btn-warning cancel\" ng-click=\"cancelLogin()\">Cancel</button>\n" +
    "    </div>\n" +
    "</form>\n" +
    "");
}]);

angular.module("security/login/profile-edit.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("security/login/profile-edit.tpl.html",
    "<div class=\"well\">\n" +
    "    <form name=\"form\" novalidate crud-edit=\"user\">\n" +
    "        <legend>User</legend>\n" +
    "        <input type=\"file\" style=\"display:none\"\n" +
    "               id=\"file\" name='file' onchange=\"angular.element(this).scope().fileChanged(this)\" />\n" +
    "        <img  width=\"200\" height=\"200\" ng-src=\"{{user.base64Image}}\" onclick=\"$('#file').click();\" class=\"img-polaroid pull-right\"></gravatar>\n" +
    "        <label for=\"email\">E-mail</label>\n" +
    "        <input class=\"span6\" type=\"email\" id=\"email\" name=\"email\" ng-model=\"user.email\" disabled>\n" +
    "        <label for=\"name\">Name</label>\n" +
    "        <input class=\"span6\" type=\"text\" id=\"name\" name=\"name\" ng-model=\"user.name\" required>\n" +
    "        <span ng-show=\"showError('name', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "        <label for=\"password\">Password</label>\n" +
    "        <input class=\"span6\" type=\"password\" id=\"password\" name=\"password\" ng-model=\"user.password\" required>\n" +
    "        <span ng-show=\"showError('password', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "        <span ng-show=\"showError('passwordRepeat', 'equal')\" class=\"help-inline\">Passwords do not match.</span>\n" +
    "        <label for=\"passwordRepeat\">Password (repeat)</label>\n" +
    "        <input class=\"span6\" type=\"password\" id=\"passwordRepeat\" name=\"passwordRepeat\" ng-model=\"password\" required validate-equals=\"user.password\">\n" +
    "        <span ng-show=\"showError('passwordRepeat', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "        <span ng-show=\"showError('passwordRepeat', 'equal')\" class=\"help-inline\">Passwords do not match.</span>\n" +
    "        <label>Admin</label>\n" +
    "        <input type=\"checkbox\" ng-model=\"user.admin\" disabled>\n" +
    "        <div ng-if=\"!user.admin\">\n" +
    "            <label for=\"street\">Street</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"street\" name=\"street\" ng-model=\"user.street\" required>\n" +
    "            <span ng-show=\"showError('street', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label for=\"address\">Address</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"address\" name=\"address\" ng-model=\"user.address\">\n" +
    "            <label for=\"plz\">PLZ</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"plz\" name=\"plz\" ng-model=\"user.plz\" required>\n" +
    "            <span ng-show=\"showError('plz', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label for=\"city\">City</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"city\" name=\"city\" ng-model=\"user.city\" required>\n" +
    "            <span ng-show=\"showError('city', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label for=\"telephoneNumber\">Telephone number</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"telephoneNumber\" name=\"telephoneNumber\" ng-model=\"user.telephoneNumber\" required>\n" +
    "            <span ng-show=\"showError('telephoneNumber', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label>Transport type</label>\n" +
    "            <label><input name=\"transportType\" type=\"radio\" ng-model=\"user.transportType\" value=\"bicycle\" ng-required=\"!user.transportType\">Bicycle</label>\n" +
    "            <label><input name=\"transportType\" type=\"radio\" ng-model=\"user.transportType\" value=\"auto\" ng-required=\"!user.transportType\">Auto</label>\n" +
    "            <span ng-show=\"showError('transportType', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label>Telephone type</label>\n" +
    "            <label><input name=\"telephoneType\" type=\"radio\" ng-model=\"user.telephoneType\" value=\"ios\" ng-required=\"!user.telephoneType\">IOS</label>\n" +
    "            <label><input name=\"telephoneType\" type=\"radio\" ng-model=\"user.telephoneType\" value=\"android\" ng-required=\"!user.telephoneType\">Android</label>\n" +
    "            <span ng-show=\"showError('telephoneType', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label for=\"iban\">IBAN</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"iban\" name=\"iban\" ng-model=\"user.iban\" required>\n" +
    "            <span ng-show=\"showError('iban', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label for=\"bic\">BIC</label>\n" +
    "            <input class=\"span6\" type=\"text\" id=\"bic\" name=\"bic\" ng-model=\"user.bic\" required>\n" +
    "            <span ng-show=\"showError('bic', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "            <label>Contract type</label>\n" +
    "            <label><input name=\"contractType\" type=\"radio\" ng-model=\"user.contractType\" value=\"minijob\" disabled>Minijob</label>\n" +
    "            <label><input name=\"contractType\" type=\"radio\" ng-model=\"user.contractType\" value=\"flexible\" disabled>Flexible</label>\n" +
    "        </div>\n" +
    "        <hr>\n" +
    "        <div>\n" +
    "			<button type=\"button\" class=\"btn btn-primary save\" ng-disabled=\"!canSave()\" ng-click=\"save()\">Save</button>\n" +
    "			<button type=\"button\" class=\"btn btn-warning revert\" ng-click=\"revertChanges()\" ng-disabled=\"!canRevert()\">Revert changes</button>\n" +
    "		</div>		\n" +
    "    </form>\n" +
    "</div>");
}]);

angular.module("security/login/toolbar.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("security/login/toolbar.tpl.html",
    "<ul class=\"nav pull-right\">\n" +
    "  <li class=\"divider-vertical\"></li>\n" +
    "  <li class=\"dropdown\" ng-class=\"{open:isProfileOpen}\" ng-show=\"isAuthenticated()\">\n" +
    "		<a id=\"profilemenu\" role=\"button\" class=\"dropdown-toggle\" ng-click=\"isProfileOpen=!isProfileOpen\">{{currentUser.name}}<b class=\"caret\"></b></a>\n" +
    "		<ul class=\"dropdown-menu\" role=\"menu\" aria-labelledby=\"profilemenu\">\n" +
    "			<li><a tabindex=\"-1\" href=\"/profile\" ng-click=\"isProfileOpen=false\">Edit profile</a></li>\n" +
    "			<li><a href=\"#\" tabindex=\"-1\" ng-click=\"logout();isProfileOpen=false;\">Logout</a></li>\n" +
    "		</ul>\n" +
    "  </li>\n" +
    "  <li ng-hide=\"isAuthenticated()\" class=\"login\">\n" +
    "      <form class=\"navbar-form\">\n" +
    "          <button class=\"btn login\" ng-click=\"login()\">Log in</button>\n" +
    "      </form>\n" +
    "  </li>\n" +
    "</ul>");
}]);
