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