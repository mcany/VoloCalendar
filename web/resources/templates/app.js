angular.module('templates.app', ['admin/users/users-create.tpl.html', 'admin/users/users-edit.tpl.html', 'admin/users/users-list.tpl.html', 'calendar/list.tpl.html', 'header.tpl.html', 'notifications.tpl.html']);

angular.module("admin/users/users-create.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("admin/users/users-create.tpl.html",
    "<div class=\"well\">\n" +
    "    <form name=\"form\" novalidate crud-edit=\"user\">\n" +
    "        <legend>User</legend>\n" +
    "        <gravatar email=\"user.email\" size=\"200\" class=\"img-polaroid pull-right\"></gravatar>\n" +
    "        <label for=\"email\">E-mail</label>\n" +
    "        <input class=\"span6\" type=\"email\" id=\"email\" name=\"email\" ng-model=\"user.email\" required unique-email>\n" +
    "        <span ng-show=\"showError('email', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "        <span ng-show=\"showError('email', 'email')\" class=\"help-inline\">Please enter a valid email address.</span>\n" +
    "        <span ng-show=\"showError('email', 'uniqueEmail')\" class=\"help-inline\">This email address is not available - please enter another.</span>\n" +
    "        <label for=\"lastName\">Last name</label>\n" +
    "        <input class=\"span6\" type=\"text\" id=\"lastName\" name=\"lastName\" ng-model=\"user.lastName\" required>\n" +
    "        <span ng-show=\"showError('lastName', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "        <label for=\"firstName\">First name</label>\n" +
    "        <input class=\"span6\" type=\"text\" id=\"firstName\" name=\"firstName\" ng-model=\"user.firstName\" required>\n" +
    "        <span ng-show=\"showError('firstName', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
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
    "        <gravatar email=\"user.email\" size=\"200\" class=\"img-polaroid pull-right\"></gravatar>\n" +
    "        <label for=\"email\">E-mail</label>\n" +
    "        <input class=\"span6\" type=\"email\" id=\"email\" name=\"email\" ng-model=\"user.email\" disabled>\n" +
    "        <label for=\"lastName\">Last name</label>\n" +
    "        <input class=\"span6\" type=\"text\" id=\"lastName\" name=\"lastName\" ng-model=\"user.lastName\" required>\n" +
    "        <span ng-show=\"showError('lastName', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
    "        <label for=\"firstName\">First name</label>\n" +
    "        <input class=\"span6\" type=\"text\" id=\"firstName\" name=\"firstName\" ng-model=\"user.firstName\" required>\n" +
    "        <span ng-show=\"showError('firstName', 'required')\" class=\"help-inline\">This field is required.</span>\n" +
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
    "        <hr>\n" +
    "        <crud-buttons></crud-buttons>\n" +
    "    </form>\n" +
    "</div>");
}]);

angular.module("admin/users/users-list.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("admin/users/users-list.tpl.html",
    "<table class=\"table table-bordered table-condensed table-striped table-hover\">\n" +
    "    <thead>\n" +
    "    <tr>\n" +
    "        <th></th>\n" +
    "        <th>E-mail</th>\n" +
    "        <th>Last name</th>\n" +
    "        <th>First name</th>\n" +
    "    </tr>\n" +
    "    </thead>\n" +
    "    <tbody>\n" +
    "    <tr ng-repeat=\"user in users\" ng-click=\"edit(user.$id())\">\n" +
    "        <td><gravatar email=\"user.email\" size=\"50\" default-image=\"'monsterid'\"></gravatar></td>\n" +
    "        <td>{{user.email}}</td>\n" +
    "        <td>{{user.lastName}}</td>\n" +
    "        <td>{{user.firstName}}</td>\n" +
    "        <td><button class=\"btn btn-danger remove\" ng-click=\"remove(user, $index, $event)\">Remove</button></td>\n" +
    "    </tr>\n" +
    "    </tbody>\n" +
    "</table>\n" +
    "<div class=\"well\">\n" +
    "    <button class=\"btn\" ng-click=\"new()\">New User</button>\n" +
    "</div>\n" +
    "");
}]);

angular.module("calendar/list.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("calendar/list.tpl.html",
    "<h3>My calendars</h3>");
}]);

angular.module("header.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("header.tpl.html",
    "<div class=\"navbar\" ng-controller=\"HeaderCtrl\">\n" +
    "    <div class=\"navbar-inner\">\n" +
    "        <a class=\"brand\" ng-click=\"home()\">VoloCalendar</a>\n" +
    "        <ul class=\"nav\">\n" +
    "            <li ng-class=\"{active:isNavbarActive('calendar')}\"><a href=\"/calendar\">My calendar</a></li>\n" +
    "        </ul>\n" +
    "\n" +
    "        <ul class=\"nav\" ng-show=\"isAuthenticated()\">            \n" +
    "            <li class=\"dropdown\" ng-class=\"{active:isNavbarActive('admin'), open:isAdminOpen}\" ng-show=\"isAdmin()\">\n" +
    "                <a id=\"adminmenu\" role=\"button\" class=\"dropdown-toggle\" ng-click=\"isAdminOpen=!isAdminOpen\">Admin<b class=\"caret\"></b></a>\n" +
    "                <ul class=\"dropdown-menu\" role=\"menu\" aria-labelledby=\"adminmenu\">\n" +
    "                    <li><a tabindex=\"-1\" href=\"/admin/users\" ng-click=\"isAdminOpen=false\">Manage Users</a></li>\n" +
    "                </ul>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "        <ul class=\"nav pull-right\" ng-show=\"hasPendingRequests()\">\n" +
    "            <li class=\"divider-vertical\"></li>\n" +
    "            <li><a href=\"#\"><img src=\"/static/img/spinner.gif\"></a></li>\n" +
    "        </ul>\n" +
    "        <login-toolbar></login-toolbar>\n" +
    "    </div>\n" +
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
