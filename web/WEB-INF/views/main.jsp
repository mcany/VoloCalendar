<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
    <base href="/">
    <link rel="stylesheet" type="text/css" href="/static/bootstrap.css"/>
    <script type="text/javascript" src="/static/jquery.js"></script>
    <script type="text/javascript" src="/static/angular.js"></script>
    <script type="text/javascript" src="/static/mongolab.js"></script>
    <script type="text/javascript" src="/static/bootstrap.js"></script>
    <script type="text/javascript" src="/static/angular-app.js"></script>
</head>

<body ng-controller="AppCtrl">
    <div ng-include="'header.tpl.html'"></div>
    <div class="container">
        <div ng-include="'notifications.tpl.html'" class="container-fluid" ng-show="notifications.getCurrent().length"></div>
        <div ng-view></div>
    </div>
</body>
</html>