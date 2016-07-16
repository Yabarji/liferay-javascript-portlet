# Development with AngularJS 1 & Liferay

This document contains instructions to set up Liferay compatible AngularJS 1 application. Example implementation can be found [here](../example/angular1). Application base is generated using [Fountain Generation](fountainjs.io) with [EcmaScript6](http://es6-features.org/) and [UI router](https://github.com/angular-ui/ui-router).

## Application bootstrapping <a name="bootstrap"></a>

##### Thymeleaf template configuration

First of all JavaScript application needs configuration object. I created one in [index.html](../example/angular1/src/index.html):

```html
<script type="text/javascript" th:inline="javascript">
    window['angular1'] = {
      ajaxURL: /*[[${ajaxURL}]]*/ "http://localhost:8081/",
      isStandalone: /*[[${standalone}]]*/ true,
      authenticatedUser: /*[[${authenticatedUser}]]*/ "anonymous",
      portletId: /*[[${portletId}]]*/ 'app',
      portletAppContextPath: /*[[${portletAppContextPath}]]*/ ''
    };
</script>
```

<!--
##### `angular.bootstrap` manually into an element

[src/index.js](../example/angular1/src/index.js):

```javascript
// After creating application module

const appRootElem = document.getElementById(window.angular1.portletId);
angular.bootstrap(appRootElem, ['app']);
```

> Note! If you use static value for root element ID, make sure it doesn't conflict on portal content.
-->
## Configuring URL routing

You need to configure one method of routing for application to know on which state it is. There are two options: path based routing and hash based routing. Differences and pros & cons are described [here](PATH_HASH_HISTORY.md).

### Configuring path URL routing <a name="pathhistory"></a>

[src/routes.js](../example/angular1/src/routes.js):

##### set HTML5 (push state mode) to `true`

```javascript
function routesConfig($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider) {
  $locationProvider.html5Mode(true);
  // ... more stuff
});
```

##### Set base href dynamically

[src/index.html](../example/angular1/src/index.html):

```html
<base href="/" th:href="${portletFriendlyUrl} + ${'/'}">
```

> Note! Trailing slash is important.

##### Configure your HTTP server to support path URLs

Instructions [here](WEBSERVER_CONFIG.md).

##### Setting base URL for development

Coming up...

### Configuring hash URL routing <a name="hashhistory"></a>

##### set HTML5 (push state mode) to `false`

[src/routes.js](../example/angular1/src/routes.js):

```javascript
function routesConfig($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider) {
  $locationProvider.html5Mode(false);
  // ... more stuff
});
```

## Authentication/Authorization

There are an example module in [Angular1 example application](../example/angular1/src/app/auth). It contains following files:

- [auth.interceptor.js](../example/angular1/src/app/auth/auth.interceptor.js) to inject JWT token into AJAX requests
- [authorizationCheck.js](../example/angular1/src/app/auth/authorizationCheck.js) to check if the next state needs authorization
- [auth.service.js](../example/angular1/src/app/auth/auth.service.js) to provide user login and logout methods. Uses session storage to hold on to JWT token
- [user.service.js](../example/angular1/src/app/auth/user.service.js) to handle AJAX calls to fetch token from [com.hannikkala.jsliferay.delegate.controller.MeController](../js-portlet/src/main/java/com/hannikkala/jsliferay/delegate/controller/MeController.java)
- [index.js](../example/angular1/src/app/auth/index.js) to tie it all together

Configuration of the module can found in [index.js](../example/angular1/src/index.js):

```javascript
// .. other imports
import {authModule} from "./app/auth/index";
import authorizationCheck from "./app/auth/authorizationCheck";

angular
  .module('app', [todoModule, authModule, 'ui.router']) // <-- authModule here
  .config(routesConfig)
  .component('app', main)
  .component('fountainHeader', header)
  .component('fountainTitle', title)
  .component('fountainFooter', footer)
  .constant('ajaxURL', window.angular1.ajaxURL)
  .constant('isStandalone', window.angular1.isStandalone)
  .run(authorizationCheck); // <-- authorization check here
```

With this configuration every state will be checked if they need authorization and fetch JWT Token from the portlet.

And in [routes.js](../example/angular1/src/routes.js):

```javascript
function routesConfig($stateProvider, $urlRouterProvider, $locationProvider, $httpProvider) {
  ... other stuff
  $httpProvider.interceptors.push('authInterceptor');
  
  ... routes
}
```

Interceptor configuration injects JWT Token into every REST call automatically.

## Use REST URL as constant

Use Thymeleaf template variable in your [index.html](../example/angular1/src/index.html) to inject variable from portlet and make it Angular constant.

```html
<script type="text/javascript" th:inline="javascript">
    window['angular1'] = {
      ajaxURL: /*[[${ajaxURL}]]*/ "http://localhost:8081/"
    };
</script>
```

Use variable in [index.js](../example/angular1/src/index.js):

```javascript
angular
  .module('app', [todoModule, authModule, 'ui.router'])
  // ...
  .constant('ajaxURL', window.angular1.ajaxURL)
```

Use constant as dependency in your services. [Example](../example/angular1/src/app/todo/todo.service.js):

```javascript
// Inject ajaxUrl into your services
export class TodoService {
  /** @ngInject */
  constructor($http, ajaxURL) {
    this.$http = $http;
    this.ajaxURL = ajaxURL;
    this.todos = [];
  }
  ...
}
```

