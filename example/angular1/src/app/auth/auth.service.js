import angular from 'angular';

export class AuthenticationService {
  /** @ngInject */
  constructor(userService, $window) {
    this.userService = userService;
    this.$window = $window;
  }

  login() {
    return this.userService.getUser()
      .then(user => {
        this.$window.sessionStorage.setItem('user', angular.toJson(user));
        return user;
      });
  }

  isLoggedIn() {
    const user = angular.fromJson(this.$window.sessionStorage.getItem('user'));
    if (user) {
      return user;
    }
    return false;
  }

  logout() {
    this.$window.sessionStorage.removeItem('user');
  }
}
