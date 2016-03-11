(function() {
  'use strict';

  angular
    .module('pocAngularClient')
    .controller('MainController', MainController);

  /** @ngInject */
  function MainController(TodoService, _) {
    var vm = this;

    TodoService.list().then(function(data) {
      vm.todos = data;
    });

    vm.saveTodo = function(todo) {
      if(todo.id) {
        TodoService.update(todo.id, todo).then(function(todo) {
          var t = _.find(vm.todos, {id: todo.id});
          if(t) {
            t.title = todo.title;
            t.done = todo.done;
          }
          vm.newTodo = {};
        })
      } else {
        TodoService.create(todo).then(function(todo) {
          vm.todos.push(todo);
          vm.newTodo = {};
        });
      }
    };

    vm.editTodo = function(todo) {
      vm.newTodo = angular.copy(todo);
    };

    vm.updateTodo = function(todo) {
      TodoService.update(todo.id, todo).then(function(todo) {
        var t = _.find(vm.todos, {id: todo.id});
        t.title = todo.title;
        t.done = todo.done;
      });
    };

    vm.removeTodo = function(todo) {
      TodoService.remove(todo.id).then(function() {
        _.remove(vm.todos, {id: todo.id});
      });
    };
  }
})();
