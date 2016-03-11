'use strict';

angular.module('pocAngularClient')
  .factory('User', function ($http, $q, ajaxUrl, isStandalone, $timeout) {
    return {
      get: function() {
        var deferred = $q.defer();
        if(isStandalone) {
          $timeout(function() {
            deferred.resolve({username: 'test@liferay.com', roles: ['ROLE_ADMIN'], jwttoken: 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0b21taS5oYW5uaWtrYWxhQGdtYWlsLmNvbSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE0NTYxNDk4NzJ9.IxzweE38eP8yFeQZkFtFtPOKPCMWCMzwdRoCF7FRHyU'});
          }, 500);
        } else {
          $http.get(ajaxUrl + 'me').success(function(user) {
            deferred.resolve(user.data);
          });
        }
        return deferred.promise;
      }
    };
  });
