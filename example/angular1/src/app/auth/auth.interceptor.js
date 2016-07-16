import angular from 'angular';

export default authInterceptor;

/** @ngInject */
function authInterceptor($q, $window) {
  return {
    request: config => {
      const user = angular.fromJson($window.sessionStorage.getItem('user'));
      if (user) {
        config.headers.Authorization = `Bearer ${user.jwttoken}`;
      }
      return config;
    },

    response: response => {
      if (response.status === 401) {
        // clear any JWT token being stored
        $window.sessionStorage.removeItem('user');
        // do a hard page refresh
        $window.location.reload();
        return $q.reject(response);
      }
      return $q.resolve(response);
    }
  };
}
