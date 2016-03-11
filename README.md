# Liferay Javascript Portlet

Generic purpose portlet to display any application made with any modern JavaScript framework. There's an simple example made with Angular1 and REST backend with Spring Boot. As easily it works with ReactJS and others.

## Quickstart

You must have JavaScript application running somewhere for Liferay to find it. Optionally you may have REST service running also.

1. Install portlet. Drop the portlet into Liferay deploy directory.
    - There are **application.json** inside the portlet to set REST API addresses by URL patterns
2. Drag and drop portlet into some page as an administrator.
3. Configure portlet. Set the URL where we can fetch index.html.

#### Configuring REST services

Find **application.json** inside WAR package. There's already configured a single REST API to be used with an example project. It looks like following:

```json
{
  "rest": [
    {
      "pattern": "/rest/api/todo/**",
      "location": "http://localhost:8081"
    }
  ]
}
```

> Note! Access URL is **/delegate/rest/api/** but while requesting proxy through Liferay's Delegate Servlet, **/delegate** gets stripped off.

> Note! Background service sees only **/api/todo** part of the request. 

#### Configuring portlet

![Not configured Portlet](example/screenshot01_notconfigured.png?raw=true "Portlet Added but not configured")
![Portlet configuration](example/screenshot02_configuration.png?raw=true "Configure Portlet as administrator")
![UI Root configured](example/screenshot03_configuration.png?raw=true "Write page URL on the input field where we can find index.html")
![Refreshed](example/screenshot04_refreshed.png?raw=true "Portlet will show up after page is refreshed")

## Features

* Fetches _index.html_ from external source as [Thymeleaf template](http://www.thymeleaf.org)
* Caches resources from the index page internally for faster accessibility (upcoming)
* Support for multiple portlets  
* Support for multiple JavaScript applications (as different portlet instances)
* Support for multiple REST services. Configure in application.json inside the WAR package
* Support for JWT tokens. Get user token from URL __/delegate/rest/me__

## TODO

- [ ] Cache fetching for static resources
- [ ] Configurable JWT secret key
- [ ] Way better error communication
- [ ] Better architecture picture
- [ ] Support for externalized REST configuration

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

__Tips and tricks__

- To use multiple portlets on the same page, you might need to dynamically name your root element. Portlet ID that is provided when calling through Portlet will help you. Example:
 
```html
<div id="myrootelement" th:id="${portletId}></div> 
```

- Start your application manually and attach it to your root element. ReactJS works this way by default but in Angular, **don't** use **ng-app** -attribute. Instead use `angular.bootstrap(angular.bootstrap(document.getElementById(portletId), ['mymodule']);)`.

#### Resource paths

All resources from index.html are mapped as __/poc-js-portlet/p/\<portlet_instance_id\>/path__. You can use variables set to an index page as stated above.

#### REST Proxy

Portlet comes with the REST API proxy. REST API's URLs are __/delegate/rest/api/\<mapping\>__. This delegate Servlet uses classpath resource **application.json** to find out where to forward the request. 

#### Fetching JWT token

When you are logged in to Liferay, you can use **/delegate/rest/me** to fetch one. The request must go through Liferay Delegate Servlet (very undocumented feature) to gain access to user that's logged in. That's why there must be **/delegate** -prefix in the request.

## Running example project

There is an example project in example directory. Example project contains two modules: __poc-angular-client__ and __rest-service__. Naming should be self-explaining :) 

#### Running poc-angular-client

Go to __example/poc-angular-client__ and run `npm install && gulp serve:dist`. This will install dependencies and run client on port 3000. Angular client runs on NodeJS and BrowserSync.

#### Running rest-service

Go to __example/rest-service__ and run `mvn spring-boot:run`. This will run REST API on port 8081.

## Installing non-Maven dependency

Portlet uses [Jaunt API](http://www.jaunt-api.com) to fetch websites and cache them for faster accessibility. Here's how to install the file (provided) into your local Maven repository:

```shell
mvn install:install-file -Dfile=poc-js-portlet/lib/jaunt1.1.4.jar -DgroupId=com.jaunt-api -DartifactId=jaunt -Dversion=1.1.4 -Dpackaging=jar
```