import angular from 'angular';
import {TodoService} from "./todo.service";
import {todos} from "./todo.controller";
import {todoEdit} from "./todoform.controller";

export const todoModule = 'todo';

angular
  .module(todoModule, [])
  .service('todoService', TodoService)
  .component('fountainTodos', todos)
  .component('fountainEditTodo', todoEdit);
