import angular from 'angular';

import 'angular-ui-router';
import routesConfig from './routes.js';

import {main} from './app/main.js';
import {header} from './app/header.js';
import {title} from './app/title.js';
import {footer} from './app/footer.js';
import {todoModule} from './app/todo/index.js';
import {authModule} from "./app/auth/index";
import authorizationCheck from "./app/auth/authorizationCheck";

angular
  .module('app', [todoModule, authModule, 'ui.router'])
  .config(routesConfig)
  .component('app', main)
  .component('fountainHeader', header)
  .component('fountainTitle', title)
  .component('fountainFooter', footer)
  .constant('ajaxURL', window.angular1.ajaxURL)
  .constant('isStandalone', window.angular1.isStandalone)
  .run(authorizationCheck);
