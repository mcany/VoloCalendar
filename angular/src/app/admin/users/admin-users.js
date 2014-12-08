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