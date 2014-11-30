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
    "        <input type=\"checkbox\" ng-model=\"user.admin\" disabled>\n" +
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
    "		<a id=\"profilemenu\" role=\"button\" class=\"dropdown-toggle\" ng-click=\"isProfileOpen=!isProfileOpen\">{{currentUser.firstName}} {{currentUser.lastName}}<b class=\"caret\"></b></a>\n" +
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
