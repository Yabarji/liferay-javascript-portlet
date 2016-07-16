
class TodoformController {
  /** @ngInject */
  constructor($stateParams, $state, todoService) {
    if ($stateParams.id) {
      todoService.get($stateParams.id).then(todo => {
        this.todo = todo;
      });
    } else {
      this.todo = {};
    }
    this.service = todoService;
    this.$state = $state;
  }

  saveTodo(todo) {
    this.service.save(todo).then(() => {
      this.todo = {};
      this.$state.go('app.listTodo');
    });
  }
}

export const todoEdit = {
  controller: TodoformController,
  templateUrl: 'src/app/todo/todoform.html'
};
