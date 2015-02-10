angular.module('services.utilMethods', []);
angular.module('services.utilMethods').factory('utilMethods', [function () {

    var utilMethodsService = {};
    var monthNames = {
        1: 'January',
        2: 'February',
        3: 'March',
        4: 'April',
        5: 'May',
        6: 'June',
        7: 'Jule',
        8: 'August',
        9: 'September',
        10: 'October',
        11: 'November',
        12: 'December'
    }

    var weekDayNames = {
        1: 'Monday',
        2: 'Tuesday',
        3: 'Wednesday',
        4: 'Thursday',
        5: 'Friday',
        6: 'Saturday',
        0: 'Sunday'
    }
    var container = {'monthNames': monthNames, 'weekDayNames': weekDayNames};
    utilMethodsService.fileInputOfUserViewChanged = function (scope) {
        return function (fileInput) {
            var file = fileInput.files[0];
            var FR = new FileReader();
            FR.onload = function (e) {
                scope.user.base64Image = e.target.result;
                scope.$digest();
            };
            FR.readAsDataURL(file);
        }
    };

    utilMethodsService.save = function (key, value) {
        container[key] = value;
    }

    utilMethodsService.get = function (key) {
        return container[key];
    }

    var getGreenColorShadePrivate = function (actualValue, max) {
        if (max < actualValue) {
            return 'white';
        }
        var r = Math.floor(124 * (1 - actualValue / (2 * max)));
        var g = Math.floor(252 * (1 - actualValue / (2 * max)));
        var b = Math.floor(0 * (1 - actualValue / (2 * max)));
        var result = 'rgb(' + r + ',' + g + ',' + b + ')';
        return result;
    }

    utilMethodsService.maxAllowed = 15;
    utilMethodsService.getGreenColorShade = function (actualValue) {
        var result;
        if (actualValue <= 0) {
            result = 'white';
        } else {
            result = getGreenColorShadePrivate(actualValue, utilMethodsService.maxAllowed);
        }
        return result;
    };

    utilMethodsService.getGreenColorShadeWithMaxDefined = function (actualValue, max) {
        var result;
        if (actualValue <= 0) {
            result = 'white';
        } else {
            result = getGreenColorShadePrivate(actualValue, max);
        }
        return result;
    }

    return utilMethodsService;
}]);