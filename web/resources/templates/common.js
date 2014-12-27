angular.module('templates.common', ['security/login/form.tpl.html', 'security/login/profile-edit.tpl.html', 'security/login/toolbar.tpl.html']);

angular.module("security/login/form.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("security/login/form.tpl.html",
    "<form name=\"form\" novalidate class=\"form-horizontal\" role=\"form\">\n" +
    "    <div class=\"modal-header\">\n" +
    "        <h4>Sign in</h4>\n" +
    "    </div>\n" +
    "    <div class=\"modal-body\">\n" +
    "        <div class=\"alert alert-warning\" ng-show=\"authReason\">\n" +
    "            {{authReason}}\n" +
    "        </div>\n" +
    "        <div class=\"alert alert-danger\" ng-show=\"authError\">\n" +
    "            {{authError}}\n" +
    "        </div>\n" +
    "        <div class=\"alert alert-info\">Please enter your login details</div>\n" +
    "        <div class=\"form-group form-group-sm\">\n" +
    "            <label class=\"col-sm-2 control-label\" for=\"email\">E-mail</label>\n" +
    "\n" +
    "            <div class=\"col-sm-8\">\n" +
    "                <input id=\"email\" class=\"form-control\" name=\"email\" type=\"email\" ng-model=\"user.email\" required\n" +
    "                       autofocus>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "        <div class=\"form-group form-group-sm\">\n" +
    "            <label class=\"col-sm-2 control-label\" for=\"password\">Password</label>\n" +
    "\n" +
    "            <div class=\"col-sm-8\">\n" +
    "                <input id=\"password\" class=\"form-control\" name=\"password\" type=\"password\" ng-model=\"user.password\"\n" +
    "                       required>\n" +
    "            </div>\n" +
    "        </div>\n" +
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
    "    <form name=\"form\" novalidate class=\"form-horizontal\" role=\"form\">\n" +
    "        <legend>User</legend>\n" +
    "        <div class=\"row\">\n" +
    "            <div class=\"col-xs-8\">\n" +
    "                <div class=\"form-group form-group-sm\">\n" +
    "                    <label class=\"col-sm-2 control-label\" for=\"email\">E-mail</label>\n" +
    "\n" +
    "                    <div class=\"col-sm-5\">\n" +
    "                        <input class=\"form-control\" type=\"email\" id=\"email\" name=\"email\" ng-model=\"user.email\" disabled>\n" +
    "                    </div>\n" +
    "                </div>\n" +
    "                <div class=\"form-group form-group-sm\">\n" +
    "                    <label class=\"col-sm-2 control-label\" for=\"name\">Name</label>\n" +
    "\n" +
    "                    <div class=\"col-sm-5\">\n" +
    "                        <input class=\"form-control\" type=\"text\" id=\"name\" name=\"name\" ng-model=\"user.name\" required>\n" +
    "                    </div>\n" +
    "                    <span ng-show=\"showError('name', 'required')\"\n" +
    "                          class=\"alert alert-danger\">This field is required.</span>\n" +
    "                </div>\n" +
    "                <div class=\"form-group form-group-sm\">\n" +
    "                    <label class=\"col-sm-2 control-label\" for=\"password\">Password</label>\n" +
    "\n" +
    "                    <div class=\"col-sm-5\">\n" +
    "                        <input class=\"form-control\" type=\"password\" id=\"password\" name=\"password\"\n" +
    "                               ng-model=\"user.password\"\n" +
    "                               required>\n" +
    "                    </div>\n" +
    "                    <span ng-show=\"showError('password', 'required')\"\n" +
    "                          class=\"alert alert-danger\">This field is required.</span>\n" +
    "                </div>\n" +
    "                <div class=\"form-group form-group-sm\">\n" +
    "                    <label class=\"col-sm-2 control-label\" for=\"passwordRepeat\">Password (repeat)</label>\n" +
    "\n" +
    "                    <div class=\"col-sm-5\">\n" +
    "                        <input class=\"form-control\" type=\"password\" id=\"passwordRepeat\" name=\"passwordRepeat\"\n" +
    "                               ng-model=\"password\" required validate-equals=\"user.password\">\n" +
    "                    </div>\n" +
    "                    <span ng-show=\"showError('passwordRepeat', 'equal')\" class=\"alert alert-danger\">Passwords do not match.</span>\n" +
    "                </div>\n" +
    "                <div class=\"form-group form-group-sm\">\n" +
    "                    <label class=\"col-sm-2 control-label\" for=\"admin\">Admin</label>\n" +
    "\n" +
    "                    <div class=\"col-sm-5\">\n" +
    "                        <input id=\"admin\" class=\"checkbox-inline\" type=\"checkbox\" ng-model=\"user.admin\" disabled>\n" +
    "                    </div>\n" +
    "                </div>\n" +
    "                <div ng-if=\"!user.admin\">\n" +
    "                    <div class=\"form-group form-group-sm\">\n" +
    "                        <label class=\"col-sm-2 control-label\" for=\"street\">Street</label>\n" +
    "\n" +
    "                        <div class=\"col-sm-5\">\n" +
    "                            <input class=\"form-control\" type=\"text\" id=\"street\" name=\"street\" ng-model=\"user.street\"\n" +
    "                                   required>\n" +
    "                        </div>\n" +
    "                        <span ng-show=\"showError('street', 'required')\" class=\"alert alert-danger\">This field is required.</span>\n" +
    "                    </div>\n" +
    "                    <div class=\"form-group form-group-sm\">\n" +
    "                        <label class=\"col-sm-2 control-label\" for=\"address\">Address</label>\n" +
    "\n" +
    "                        <div class=\"col-sm-5\">\n" +
    "                            <input class=\"form-control\" type=\"text\" id=\"address\" name=\"address\" ng-model=\"user.address\">\n" +
    "                        </div>\n" +
    "                    </div>\n" +
    "                    <div class=\"form-group form-group-sm\">\n" +
    "                        <label class=\"col-sm-2 control-label\" for=\"plz\">PLZ</label>\n" +
    "\n" +
    "                        <div class=\"col-sm-5\">\n" +
    "                            <input class=\"form-control\" type=\"text\" id=\"plz\" name=\"plz\" ng-model=\"user.plz\" required>\n" +
    "                        </div>\n" +
    "                        <span ng-show=\"showError('plz', 'required')\"\n" +
    "                              class=\"alert alert-danger\">This field is required.</span>\n" +
    "                    </div>\n" +
    "                    <div class=\"form-group form-group-sm\">\n" +
    "                        <label class=\"col-sm-2 control-label\" for=\"city\">City</label>\n" +
    "\n" +
    "                        <div class=\"col-sm-5\">\n" +
    "                            <input class=\"form-control\" type=\"text\" id=\"city\" name=\"city\" ng-model=\"user.city\" required>\n" +
    "                        </div>\n" +
    "                        <span ng-show=\"showError('city', 'required')\"\n" +
    "                              class=\"alert alert-danger\">This field is required.</span>\n" +
    "                    </div>\n" +
    "                    <div class=\"form-group form-group-sm\">\n" +
    "                        <label class=\"col-sm-2 control-label\" for=\"telephoneNumber\">Telephone number</label>\n" +
    "\n" +
    "                        <div class=\"col-sm-5\">\n" +
    "                            <input class=\"form-control\" type=\"text\" id=\"telephoneNumber\" name=\"telephoneNumber\"\n" +
    "                                   ng-model=\"user.telephoneNumber\" required>\n" +
    "                        </div>\n" +
    "                        <span ng-show=\"showError('telephoneNumber', 'required')\"\n" +
    "                              class=\"alert alert-danger\">This field is required.</span>\n" +
    "                    </div>\n" +
    "                    <div class=\"form-group form-group-sm\">\n" +
    "                        <label class=\"col-sm-2 control-label\">Transport type</label>\n" +
    "\n" +
    "                        <div class=\"col-sm-5\">\n" +
    "                            <label class=\"radio-inline\">\n" +
    "                                <input name=\"transportType\" type=\"radio\" ng-model=\"user.transportType\" value=\"bicycle\"\n" +
    "                                       ng-required=\"!user.transportType\">Bicycle\n" +
    "                            </label>\n" +
    "                            <label class=\"radio-inline\">\n" +
    "                                <input name=\"transportType\" type=\"radio\" ng-model=\"user.transportType\" value=\"auto\"\n" +
    "                                       ng-required=\"!user.transportType\">Auto\n" +
    "                            </label>\n" +
    "                        </div>\n" +
    "                        <span ng-show=\"showError('transportType', 'required')\"\n" +
    "                              class=\"alert alert-danger\">This field is required.</span>\n" +
    "                    </div>\n" +
    "                    <div class=\"form-group form-group-sm\">\n" +
    "                        <label class=\"col-sm-2 control-label\">Telephone type</label>\n" +
    "\n" +
    "                        <div class=\"col-sm-5\">\n" +
    "                            <label class=\"radio-inline\">\n" +
    "                                <input name=\"telephoneType\" type=\"radio\" ng-model=\"user.telephoneType\" value=\"ios\"\n" +
    "                                       ng-required=\"!user.telephoneType\">IOS\n" +
    "                            </label>\n" +
    "                            <label class=\"radio-inline\">\n" +
    "                                <input name=\"telephoneType\" type=\"radio\" ng-model=\"user.telephoneType\" value=\"android\"\n" +
    "                                       ng-required=\"!user.telephoneType\">Android\n" +
    "                            </label>\n" +
    "                        </div>\n" +
    "                        <span ng-show=\"showError('telephoneType', 'required')\"\n" +
    "                              class=\"alert alert-danger\">This field is required.</span>\n" +
    "                    </div>\n" +
    "                    <div class=\"form-group form-group-sm\">\n" +
    "                        <label class=\"col-sm-2 control-label\" for=\"iban\">IBAN</label>\n" +
    "\n" +
    "                        <div class=\"col-sm-5\">\n" +
    "                            <input class=\"form-control\" type=\"text\" id=\"iban\" name=\"iban\" ng-model=\"user.iban\" required>\n" +
    "                        </div>\n" +
    "                        <span ng-show=\"showError('iban', 'required')\"\n" +
    "                              class=\"alert alert-danger\">This field is required.</span>\n" +
    "                    </div>\n" +
    "                    <div class=\"form-group form-group-sm\">\n" +
    "                        <label class=\"col-sm-2 control-label\" for=\"bic\">BIC</label>\n" +
    "\n" +
    "                        <div class=\"col-sm-5\">\n" +
    "                            <input class=\"form-control\" type=\"text\" id=\"bic\" name=\"bic\" ng-model=\"user.bic\" required>\n" +
    "                        </div>\n" +
    "                        <span ng-show=\"showError('bic', 'required')\"\n" +
    "                              class=\"alert alert-danger\">This field is required.</span>\n" +
    "                    </div>\n" +
    "                    <div class=\"form-group form-group-sm\">\n" +
    "                        <label class=\"col-sm-2 control-label\">Contract type</label>\n" +
    "\n" +
    "                        <div class=\"col-sm-5\">\n" +
    "                            <label class=\"radio-inline\">\n" +
    "                                <input name=\"contractType\" type=\"radio\" ng-model=\"user.contractType\" value=\"minijob\"\n" +
    "                                       ng-required=\"!user.contractType\">Minijob\n" +
    "                            </label>\n" +
    "                            <label class=\"radio-inline\">\n" +
    "                                <input name=\"contractType\" type=\"radio\" ng-model=\"user.contractType\" value=\"flexible\"\n" +
    "                                       ng-required=\"!user.contractType\">Flexible\n" +
    "                            </label>\n" +
    "                        </div>\n" +
    "                        <span ng-show=\"showError('contractType', 'required')\"\n" +
    "                              class=\"alert alert-danger\">This field is required.</span>\n" +
    "                    </div>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "            <div class=\"col-xs-4\">\n" +
    "                <input type=\"file\" style=\"display:none\"\n" +
    "                       id=\"file\" name='file' onchange=\"angular.element(this).scope().fileChanged(this)\"/>\n" +
    "                <img width=\"200\" height=\"200\" ng-src=\"{{user.base64Image}}\" onclick=\"$('#file').click();\"\n" +
    "                     class=\"img-polaroid pull-right\"/>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "        <hr>\n" +
    "        <div>\n" +
    "            <button type=\"button\" class=\"btn btn-primary\" ng-disabled=\"!canSave()\" ng-click=\"save()\">Save</button>\n" +
    "            <button type=\"button\" class=\"btn btn-warning\" ng-click=\"revertChanges()\" ng-disabled=\"!canRevert()\">Revert\n" +
    "                changes\n" +
    "            </button>\n" +
    "        </div>\n" +
    "    </form>\n" +
    "</div>");
}]);

angular.module("security/login/toolbar.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("security/login/toolbar.tpl.html",
    "<ul class=\"nav navbar-nav pull-right\">\n" +
    "    <li class=\"dropdown\" ng-class=\"{open:isProfileOpen}\" ng-show=\"isAuthenticated()\">\n" +
    "        <a id=\"profilemenu\" role=\"button\" class=\"dropdown-toggle\" ng-click=\"isProfileOpen=!isProfileOpen\">{{currentUser.name}}<b\n" +
    "                class=\"caret\"></b></a>\n" +
    "        <ul class=\"dropdown-menu\" role=\"menu\" aria-labelledby=\"profilemenu\">\n" +
    "            <li><a tabindex=\"-1\" href=\"/profile\" ng-click=\"isProfileOpen=false\">Edit profile</a></li>\n" +
    "            <li>\n" +
    "                <a href=\"#\" tabindex=\"-1\" ng-click=\"logout();isProfileOpen=false;\">\n" +
    "                    <span class=\"glyphicon glyphicon-log-out\"></span> Logout\n" +
    "                </a>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "    </li>\n" +
    "    <li ng-hide=\"isAuthenticated()\">\n" +
    "        <form class=\"navbar-form\">\n" +
    "            <button class=\"btn login\" ng-click=\"login()\">\n" +
    "                <span class=\"glyphicon glyphicon-log-in\"></span> Log in\n" +
    "            </button>\n" +
    "        </form>\n" +
    "    </li>\n" +
    "</ul>");
}]);
