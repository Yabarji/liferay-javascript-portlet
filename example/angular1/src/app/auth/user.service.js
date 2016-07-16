export class UserService {
  /** @ngInject */
  constructor($http, isStandalone, $q, $timeout) {
    this.$http = $http;
    this.standalone = isStandalone;
    this.$q = $q;
    this.$timeout = $timeout;
  }

  getUser() {
    if (this.standalone) {
      const defer = this.$q.defer();
      this.$timeout(() => {
        defer.resolve({
          jwttoken: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGxpZmVyYXkuY29tIiwicm9sZXMiOlsiUk9MRV9BRE1JTiJdLCJpYXQiOjE0NjgzNTgyMTl9.jPc22bi-z-BksmrFQXngSItjn3kbDMgisyTNsJxswNg",
          user: 'test@liferay.com',
          roles: ['ROLE_ADMINISTRATOR']
        });
      }, 100);
      return defer.promise;
    }
    return this.$http
      .get(`/delegate/user/me`)
      .then(res => {
        return res.data;
      });
  }
}
