export default routesConfig;

/** @ngInject */
function routesConfig($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider) {
  $locationProvider.html5Mode(true).hashPrefix('!');
  $urlRouterProvider.otherwise('/');
  $httpProvider.interceptors.push('authInterceptor');

  $stateProvider
    .state('app', {
      template: '<app></app>',
      abstract: true
    })
    .state('app.listTodo', {
      url: '/',
      template: '<fountain-todos></fountain-todos>'
    })
    .state('app.addTodo', {
      url: '/todo',
      template: '<fountain-edit-todo></fountain-edit-todo>',
      authenticate: true
    })
    .state('app.editTodo', {
      url: '/todo/:id',
      template: '<fountain-edit-todo></fountain-edit-todo>',
      authenticate: true
    })
    .state('unauthorized', {
      url: '/unauthorized',
      templateUrl: 'src/app/unauthorized.html'
    });
}
