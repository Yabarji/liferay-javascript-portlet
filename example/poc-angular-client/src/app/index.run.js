(function() {
  'use strict';

  angular
    .module('pocAngularClient')
    .run(authorizationCheck);

  /** @ngInject */
  function authorizationCheck($rootScope, $state, Auth) {
    // Redirect to login if route requires auth and the user is not logged in
    var destroyCallback = $rootScope.$on('$stateChangeStart', function(event, next) {
      if (next.authenticate) {
        Auth.isLoggedIn(function(loggedIn) {
          if (!loggedIn) {
            event.preventDefault();
            $state.go('unauthorized');
          }
        });
      }
    });

    $rootScope.$on('$destroy', destroyCallback);
  }

})();
