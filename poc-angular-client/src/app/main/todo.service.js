/**
 * Created by bleed on 03/03/16.
 */
(function() {
  function TodoService($http, $q, ajaxUrl) {
    return {
      list: function() {
        var deferred = $q.defer();
        $http.get(ajaxUrl + "api/todo").then(function(todos) {
          deferred.resolve(todos.data);
        });
        return deferred.promise;
      },
      create: function(model) {
        var deferred = $q.defer();
        $http.post(ajaxUrl + "api/todo", model).then(function(todo) {
          deferred.resolve(todo.data);
        });
        return deferred.promise;
      },
      update: function(id, model) {
        var deferred = $q.defer();
        $http.put(ajaxUrl + "api/todo/" + id, model).then(function(todo) {
          deferred.resolve(todo.data);
        });
        return deferred.promise;
      },
      remove: function(id) {
        var deferred = $q.defer();
        $http.delete(ajaxUrl + "api/todo/" + id).then(function() {
          deferred.resolve();
        });
        return deferred.promise;
      }
    }
  }

  angular.module('pocAngularClient')
    .factory('TodoService', TodoService);
})();
