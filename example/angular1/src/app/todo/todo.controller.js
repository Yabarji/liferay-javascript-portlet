
class TodoController {
  /** @ngInject */
  constructor(todoService, authService) {
    this.service = todoService;
    this.service.list().then(() => {
      this.todos = todoService.todos;
    });
    authService.login().then(user => {
      this.isLoggedIn = true;
    });
  }

  removeTodo(todo) {
    this.service.remove(todo);
  }
}

export const todos = {
  controller: TodoController,
  templateUrl: 'src/app/todo/todo.html'
};
