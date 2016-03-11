# Liferay Javascript Portlet

Generic purpose portlet to display any application made with any modern JavaScript framework. There's an simple example made with Angular1 and REST backend with Spring Boot. As easily it works with ReactJS and others.

## Quickstart

1. Install portlet. Drop the portlet into Liferay deploy directory.
    - There are application.json inside the portlet to set REST API addresses by URL patterns
2. Drag and drop portlet into some page as an administrator.
3. Configure portlet. Set the URL where we can fetch index.html.

## Features

* Fetches index.html from external source as [Thymeleaf template](http://www.thymeleaf.org)
* Caches resources from the index page internally for faster accessibility
* Support for multiple portlets  
* Support for multiple JavaScript applications (as different portlet instances)
* Support for multiple REST services. Configure in application.json inside the WAR package
* Support for JWT tokens. Get user token from URL <b>/delegate/rest/me</b>

## Application development

The main idea is to create portlet that only has to be configured to use with any JavaScript application and REST API. 

#### Fetching index page

First of all portlet has to fetch the index page. Modern JavaScript frameworks come with some kind of container to run, for example NodeJS. The portlet fetches and processes the index page as [Thymeleaf](http://www.thymeleaf.org) template. This way it can be developed as standalone application as well.

<b>Variables that will be processed for index:</b>

Variable            | Description
--------------------|----------------------------------------------------------------
ajaxURL             | Relative URL to REST API proxy. Usually /rest/api
standalone          | Is application standalone? Will be set to <i>false</i>
authenticatedUser   | Assigns authenticated user email. For example test@liferay.com
portletId           | Assigns full portlet ID and instance ID. Example: p_p_id_pocangularportlet_WAR_pocjsportlet_INSTANCE_MVFuHik6CyK0_
portletAppContextPath | This variable can be used as prefix to images and any other resources.

###### Example variables

```javascript
var ajaxURL = /*[[${ajaxURL}]]*/ "http://localhost:8081/";
var isStandalone = /*[[${standalone}]]*/ true;
var authenticatedUser = /*[[${authenticatedUser}]]*/ "anonymous";
var portletId = /*[[${portletId}]]*/ 'pocAngularClient';
var portletAppContextPath = /*[[${portletAppContextPath}]]*/ '';
```

#### Resource paths

All resources from index.html are mapped as _/poc-js-portlet/p/\<portlet_instance_id\>/path_. You can use variables set to an index page as stated above.

#### REST Proxy

Portlet comes with the REST API proxy. REST API's URLs are _/delegate/rest/api/\<mapping\>_. This delegate Servlet uses classpath resource *application.json* to find out where to forward the request. 

## Installing non-Maven dependency

Portlet uses [Jaunt API](http://www.jaunt-api.com] to fetch websites and cache them for faster accessibility. Here's how to install the file (provided) into your local Maven repository:

```shell
mvn install:install-file -Dfile=poc-js-portlet/lib/jaunt1.1.4.jar -DgroupId=com.jaunt-api -DartifactId=jaunt -Dversion=1.1.4 -Dpackaging=jar
```