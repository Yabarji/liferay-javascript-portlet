(function() {
'use strict';

angular.module('pocAngularClient')
  .factory('Auth', function Auth($http, User, $cookies, $q, ajaxUrl) {
    /**
     * Return a callback or noop function
     *
     * @param  {Function|*} cb - a 'potential' function
     * @return {Function}
     */
    var safeCb = function(cb) {
        return (angular.isFunction(cb)) ? cb : angular.noop;
      },

      currentUser = {};

    if ($cookies.get('token')) {
      currentUser = User.get();
    }

    return {

      login: function(callback) {
        return $http.get(ajaxUrl + '/me')
          .success(function(data) {
            $cookies.put('token', data.jwttoken);
            currentUser = data.user;
            currentUser.roles = data.roles;
            safeCb(callback)(null, data.user);
          });
      },

      /**
       * Delete access token and user info
       */
      logout: function() {
        $cookies.remove('token');
        currentUser = {};
      },

      /**
       * Gets all available info on a user
       *   (synchronous|asynchronous)
       *
       * @param  {Function|*} callback - optional, funciton(user)
       * @return {Object|Promise}
       */
      getCurrentUser: function(callback) {
        if (arguments.length === 0) {
          return currentUser;
        }

        var value = (currentUser.hasOwnProperty('$promise')) ? currentUser.$promise : currentUser;
        return $q.when(value)
          .then(function(user) {
            safeCb(callback)(user);
            return user;
          }, function() {
            safeCb(callback)({});
            return {};
          });
      },

      /**
       * Check if a user is logged in
       *   (synchronous|asynchronous)
       *
       * @param  {Function|*} callback - optional, function(is)
       * @return {Bool|Promise}
       */
      isLoggedIn: function(callback) {
        if (arguments.length === 0) {
          return currentUser.hasOwnProperty('roles');
        }

        return this.getCurrentUser(null)
          .then(function(user) {
            var is = user.hasOwnProperty('roles');
            safeCb(callback)(is);
            return is;
          });
      },

      /**
       * Check if a user is an admin
       *   (synchronous|asynchronous)
       *
       * @param  {Function|*} callback - optional, function(is)
       * @return {Bool|Promise}
       */
      isAdmin: function(callback) {
        if (arguments.length === 0) {
          return 'ROLE_ADMIN' in currentUser.roles;
        }

        return this.getCurrentUser(null)
          .then(function(user) {
            var is = 'ROLE_ADMIN' in user.roles;
            safeCb(callback)(is);
            return is;
          });
      },

      /**
       * Get auth token
       *
       * @return {String} - a token string used for authenticating
       */
      getToken: function() {
        return $cookies.get('token');
      }
    };
  });
})();
