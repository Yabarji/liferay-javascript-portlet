# Liferay Javascript Portlet

Generic purpose portlet to display any application made with any modern JavaScript framework. There's an simple example made with [Angular1](http://angularjs.org) and REST backend with [Spring Boot](http://projects.spring.io/spring-boot/). As easily it works with [ReactJS](https://facebook.github.io/react/) and others.

## Why?

**Short answer:** Because Liferay development is very slow and painful (change, build, deploy, pray, restart portal, clear temp & work & webapp and try again...)
 
**Longer answer:** ... and there are great new waves on Web development. Namely modern JavaScript libraries like ReactJS, AngularJS as well as frameworks to create REST API services that are very lightweight. Here I use Spring Boot but you may choose whatever you like even made with other languages. I don't judge :) 

The development process goes more like change, automatic refresh, change, automatic refresh, .. and this is the way it should go. This project aims to be easy-to-use tool to embed any custom JavaScript software into Liferay portal and taking care of all the heavy lifting. This of course works as long as you don't need any Liferay's internal objects. 

## Features

* Fetches _index.html_ from external source as [Thymeleaf template](http://www.thymeleaf.org) or just plain simple HTML file if you don't need parameters.
* Caches the index page internally for faster accessibility
* Support for multiple portlets
* Support for multiple JavaScript applications (as different portlet instances)
* Support for multiple REST services. Configure **application.yml** inside the WAR package
* Support for JWT tokens. Get user token from URL __/delegate/user/me__ when logged in

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

#### Configuring portlet

![Not configured Portlet](example/screenshot01_notconfigured.png?raw=true "Portlet added but not configured")
Portlet added but not configured.

![Portlet configuration](example/screenshot02_configuration.png?raw=true "Configure Portlet as Administrator")
Configure Portlet as Administrator.

![UI Root configured](example/screenshot03_uirootset.png?raw=true "Write page URL on the input field where we can find index.html")
Write page URL on the input field where we can find index.html.

On bottom of the configuration page, there is a checkbox for enabling/disabling developer mode. Basically this means that application does not go to cache.

![Refreshed](example/screenshot04_refreshed.png?raw=true "Portlet will show up after page is refreshed")
Portlet will show up after page is refreshed.

# Running example project

There is an example project in example directory. Example project contains two modules: __angular1__ and __rest-service__. Naming should be self-explaining :) 

#### Running Angular example

Go to __example/angular1__ and run `npm install && gulp serve:dist`. This will install dependencies and run client on port 3000. Angular client runs on NodeJS and BrowserSync.

#### Running rest-service

Go to __example/rest-service__ and run `mvn spring-boot:run`. This will run REST API on port 8081.

#### And install Portlet to your Liferay instance

...Just like above.

## Application development

- [General information](docs/APP_DEV.md)
- [REST service configuration](docs/REST_SERVICE.md)
- Frameworks:
    - [AngularJS 1](docs/ANGULAR1.md) 