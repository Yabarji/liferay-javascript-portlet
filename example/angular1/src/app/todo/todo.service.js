import _ from 'lodash';

export class TodoService {
  /** @ngInject */
  constructor($http, ajaxURL) {
    this.$http = $http;
    this.ajaxURL = ajaxURL;
    this.todos = [];
  }

  list() {
    return this.$http
      .get(`${this.ajaxURL}api/todo`)
      .then(response => {
        this.todos = response.data;
        return this.todos;
      });
  }

  get(id) {
    return this.$http
      .get(`${this.ajaxURL}api/todo/${id}`)
      .then(response => {
        return response.data;
      });
  }

  save(todo) {
    if (todo.id) {
      return this.$http
        .put(`${this.ajaxURL}api/todo/${todo.id}`, todo)
        .then(() => {
          this.todos = _.replace(this.todos, {id: todo.id}, todo);
          return todo;
        });
    }
    return this.$http
      .post(`${this.ajaxURL}api/todo`, todo)
      .then(response => {
        this.todos.push(response.data);
        return response.data;
      });
  }

  remove(todo) {
    return this.$http
      .delete(`${this.ajaxURL}api/todo/${todo.id}`)
      .then(() => {
        _.remove(this.todos, {id: todo.id});
      });
  }
}
