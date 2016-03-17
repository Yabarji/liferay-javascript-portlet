# Liferay Javascript Portlet

Generic purpose portlet to display any application made with any modern JavaScript framework. There's an simple example made with [Angular1](http://angularjs.org) and REST backend with [Spring Boot](http://projects.spring.io/spring-boot/). As easily it works with [ReactJS](https://facebook.github.io/react/) and others.

## Why?

**Short answer:** Because Liferay development is very slow and painful (change, build, deploy, pray, restart portal, clear temp & work & webapp and try again...)
 
**Longer answer:** ... and there are great new waves on Web development. Namely modern JavaScript libraries like ReactJS, AngularJS as well as frameworks to create REST API services that are very lightweight. Here I use Spring Boot but you may choose whatever you like even made with other languages. I don't judge :) 

The development process goes more like change, automatic refresh, change, automatic refresh, .. and this is the way it should go. This project aims to be easy-to-use tool to embed any custom JavaScript software into Liferay portal and taking care of all the heavy lifting. This of course works as long as you don't need any Liferay's internal objects. 

## Features

* Fetches _index.html_ from external source as [Thymeleaf template](http://www.thymeleaf.org) or just plain simple HTML file if you don't need parameters.
* Caches resources from the index page internally for faster accessibility
* For even faster performance there are support for using CDN (Content Delivery Network)
* Support for multiple portlets  
* Support for multiple JavaScript applications (as different portlet instances)
* Support for multiple REST services. Configure *application.json* inside the WAR package
* Support for JWT tokens. Get user token from URL __/delegate/rest/me__

## Quickstart

You must have JavaScript application running somewhere for Liferay to find it. Optionally you may have REST service running as well.

1. Install portlet. Drop the portlet into Liferay deploy directory.
    - There are **application.yml** inside the portlet to set REST API addresses by URL patterns
2. Drag and drop portlet into some page as an administrator. Portlet can be found under *JavaScript* -> *JavaScript Portlet*.
3. Configure portlet. Set the URL where we can fetch index.html (See screen shots below).

#### Configuring REST services

Find **application.yml** inside WAR package. There's already configured a single REST API to be used with an example project. It looks like following:

```yaml
rest:
  - pattern: /rest/api/todo/**
    location: http://localhost:8081
```

> Note! Access URL is **/delegate/rest/api/** but while requesting proxy through Liferay's Delegate Servlet, **/delegate** gets stripped off.

> Note! Background service sees only **/api/todo** part of the request. 

#### Configuring portlet

![Not configured Portlet](example/screenshot01_notconfigured.png?raw=true "Portlet added but not configured")
Portlet added but not configured.

![Portlet configuration](example/screenshot02_configuration.png?raw=true "Configure Portlet as Administrator")
Configure Portlet as Administrator.

![UI Root configured](example/screenshot03_uirootset.png?raw=true "Write page URL on the input field where we can find index.html")
Write page URL on the input field where we can find index.html.

There are also checkbox to enable/disable so called CDN mode. If you are publishing JavaScript application into a server that can be seen and accessed by your users, you may want to enable this. Basically what it does, is to write resource links (images, JavaScripts and CSS files) to external CDN server instead of proxying them through js-portlet. 

![Refreshed](example/screenshot04_refreshed.png?raw=true "Portlet will show up after page is refreshed")
Portlet will show up after page is refreshed.

# Running example project

There is an example project in example directory. Example project contains two modules: __angular-client__ and __rest-service__. Naming should be self-explaining :) 

#### Running angular-client

Go to __example/angular-client__ and run `npm install && gulp serve:dist`. This will install dependencies and run client on port 3000. Angular client runs on NodeJS and BrowserSync.

#### Running rest-service

Go to __example/rest-service__ and run `mvn spring-boot:run`. This will run REST API on port 8081.

#### And install Portlet to your Liferay instance

...Just like above.

## TODO

- [x] ~~Cache fetching for static resources~~ DONE
- [x] ~~Configurable JWT secret key~~ DONE
- [ ] Way better error communication
- [ ] An actual architecture picture
- [ ] Support for externalized REST configuration
- [x] ~~CDN support to improve performance. Portlet rewrite paths to external source.~~

## Application development

The main idea is to create portlet that only has to be configured to use with any JavaScript application and REST API. 

#### Fetching index page

First of all portlet has to fetch the index page. Modern JavaScript frameworks come with some kind of container to run, for example NodeJS. The portlet fetches and processes the index page as [Thymeleaf](http://www.thymeleaf.org) template. This way it can be developed as standalone application as well.

__Variables that will be processed for index:__

Variable            | Description
--------------------|----------------------------------------------------------------
ajaxURL             | Relative URL to REST API proxy. Usually /rest/api
standalone          | Is application standalone? Will be set to *false*
authenticatedUser   | Assigns authenticated user email. For example test@liferay.com
portletId           | Assigns full portlet ID and instance ID. Example: **p_p_id_jsportlet_WAR_jsportlet_INSTANCE_MVFuHik6CyK0_**
portletAppContextPath | This variable can be used as prefix to images and any other resources.

###### Example variables

```javascript
<script type="text/javascript">
var ajaxURL = /*[[${ajaxURL}]]*/ "http://localhost:8081/";
var isStandalone = /*[[${standalone}]]*/ true;
var authenticatedUser = /*[[${authenticatedUser}]]*/ "anonymous";
var portletId = /*[[${portletId}]]*/ 'pocAngularClient';
var portletAppContextPath = /*[[${portletAppContextPath}]]*/ '';
// Do here whatever you need to use them.
</script>
```

#### Using different JavaScript libraries

##### AngularJS 1

- Use hash paths:

```javascript
angular.module('yourModule').config(function($locationProvider) {
    $locationProvider.setHtml5Mode(false);
});
```

- `angular.bootstrap` manually into an element

```javascript
angular.bootstrap(document.getElementById(rootElementId), ['mymodule']);
```

> Note! Don't use **ng-app**.

- Use REST URL as constant

```javascript
var ajaxURL = /*[[${ajaxURL}]]*/ "http://localhost:8081/";
app.constant('ajaxUrl', ajaxURL);

app.service('MyService', function(ajaxUrl, $http, $q) {
    return {
          list: function() {
            var deferred = $q.defer();
            $http.get(ajaxUrl + "api/todo").then(function(todos) {
              deferred.resolve(todos.data);
            });
            return deferred.promise;
          }
    }
});
```

##### ReactJS

- Use hash URLs
 
**ReactJS Router**

The trick is done by using **hashHistory** instead of **browserHistory**:

```javascript
import { Router, Route, hashHistory } from 'react-router';

render((
    <Router history={hashHistory}>
        <Route path="/" component="App" />
        // ... Other routes
    </Router>
), document.getElementById('reactRootElement'));
```

#### Tips and tricks

##### Use dynamic root element

To use multiple portlets on the same page, you might need to dynamically name your root element. Portlet ID that is provided when calling through Portlet will help you. Example:
 
```html
<div id="myrootelement" th:id="${portletId}></div> 
```

##### Use stateless REST services. 

It's recommended to use provided JWT tokens to transport user information to REST service. Portlet provides token generation REST endpoint at **/delegate/rest/me**. To configure JWT secret key, edit file **application.properties** inside the portlet application directory **\<LIFERAY_HOME\>/tomcat-\<VERSION\>/webapps/js-portlet/WEB-INF/classes**. 

#### Resource paths

All resources from *index.html* are mapped as __/js-portlet/p/\<portlet_instance_id\>/path__. You can use variables set to an index page as stated above.

#### REST Proxy

Portlet comes with the REST API proxy. REST API's URLs are __/delegate/rest/api/\<mapping\>__. This delegate Servlet uses classpath resource **application.yml** to find out where to forward the request. 

#### Fetching JWT token

When you are logged in to Liferay, you can use **/delegate/rest/me** to fetch one. The request must go through Liferay Delegate Servlet (very undocumented feature) to gain access to user that's currently logged in. That's why there must be **/delegate** -prefix on the request.
