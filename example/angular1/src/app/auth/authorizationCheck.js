export default authorizationCheck;

/** @ngInject */
function authorizationCheck($rootScope, $state, authService) {
  const destroyCb = $rootScope.$on('$stateChangeStart', (event, next) => {
    if (next.authenticate) {
      if (!authService.isLoggedIn()) {
        authService.login().then(user => {
          if (!user) {
            event.preventDefault();
            $state.go('unauthorized');
          }
        });
      }
    }
  });
  $rootScope.$on('$destroy', destroyCb);
}
