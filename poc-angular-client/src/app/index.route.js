(function() {
  'use strict';

  angular
    .module('pocAngularClient')
    .config(routerConfig);

  /** @ngInject */
  function routerConfig($stateProvider, $urlRouterProvider) {
    $stateProvider
      .state('home', {
        url: '/',
        templateUrl: 'app/main/main.html',
        controller: 'MainController',
        controllerAs: 'main',
        authenticate: true
      })
      .state('unauthorized', {
        url: '/unauthorized',
        templateUrl: 'app/unauthorized/unauthorized.html',
        controller: 'UnauthorizedController',
        controllerAs: 'unauth'
      });

    $urlRouterProvider.otherwise('/');
  }

})();
