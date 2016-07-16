import angular from 'angular';
import {UserService} from './user.service';
import {AuthenticationService} from './auth.service';
import authInterceptor from './auth.interceptor';

export const authModule = 'auth';

angular.module(authModule, [])
  .service('userService', UserService)
  .service('authService', AuthenticationService)
  .factory('authInterceptor', authInterceptor);
