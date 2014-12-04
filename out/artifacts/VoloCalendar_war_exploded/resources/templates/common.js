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
