<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
    <link rel="stylesheet" type="text/css" href="/static/angular-app.css"/>
    <script type="text/javascript" src="/static/jquery.js"></script>
    <script type="text/javascript" src="/static/angular.js"></script>
    <script type="text/javascript" src="/static/mongolab.js"></script>
    <script type="text/javascript" src="/static/bootstrap.js"></script>
    <script type="text/javascript" src="/static/angular-app.js"></script>
</head>

<body ng-controller="AppCtrl">
<div ng-include="'header.tpl.html'"></div>
<div ng-include="'notifications.tpl.html'" class="container-fluid" ng-show="notifications.getCurrent().length"></div>
<div ng-view class="container-fluid"></div>
</body>
</html>