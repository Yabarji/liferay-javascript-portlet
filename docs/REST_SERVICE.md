# Configuring REST services

This chapter gives instructions to configure REST services to use with your JavaScript applications.

## Example architecture

Let's say you have two REST services you need to use in your application. Both of services need Liferay user authorization for some operations. In this example we are going to use REST proxy included with the portlet.

![Example architecture](images/architecture.png?raw=true) 

### Use stateless REST services. 

You need to disable session creation on REST API. Otherwise it's going to mess up Portal sessions when used through REST Proxy. Example for Spring Boot with Spring Security can be found [here](../example/rest-service/src/main/java/com/hannikkala/jsliferay/rest/config/RestConfig).

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.httpBasic().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // This here.
         .and()
            .authorizeRequests()
            .anyRequest().permitAll()
         .and()
            .anonymous()
         .and()
            .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
            .csrf().disable();
}
```

### Configure JWT Secret key 

To configure JWT secret key, edit file **application.properties** inside the portlet application directory **\<LIFERAY_HOME\>/tomcat-\<VERSION\>/webapps/js-portlet/WEB-INF/classes**. This key is used to encrypt JWT token.

> Note! Use the same key for the portlet and all the services.

### Generate JWT Token

It's recommended to use provided JWT tokens to transport user information to REST service. Portlet provides token generation REST endpoint at **/delegate/user/me**. It returns current user as JSON object. If user is not logged in, it returns **HTTP status 401** (Unauthorized).

**Example response**

```json
{
    "jwttoken":"eyJhbGciOiJIUzI1NiJ9eyJzdWIiOiJ0ZXN0Iiwicm9....",
    "user":"test@liferay.com",
    "roles":["ROLE_ADMINISTRATOR","ROLE_POWER_USER","ROLE_USER"]
}
```

### REST Proxy

Portlet comes with the REST API proxy. REST API's URLs are __/delegate/rest/\<mapping\>__. This delegate Servlet uses classpath resource **application.yml** to find out where to forward the request. 

In our example we have two REST services: Todo and User services. Below you can see mapping for services.

```yml
rest:
  - pattern: /rest/todo/**
    location: http://localhost:8081/api
  - pattern: /rest/user/**  
    location: http://localhost:8082/user
```

Here's some example requests:

| Requested URL               | REST URL                           |
|-----------------------------|------------------------------------|
| /delegate/rest/todo/todo/1  | http://localhost:8081/api/todo/1   |
| /delegate/rest/user/3       | http://localhost:8082/user/3       |

### Request with JWT Token

To use JWT token there must be **Authorization** header with value **Bearer \<jwt_token_from_above\>**. 

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIi...
```

## Externalizing REST configuration

You may want to put REST configuration away from web application directory. For this purpose there's a mechanism for that: set system property `jsportlet.rest.resource` on Spring resource format (default value is `classpath:application.yml`). 
 
**Examples:**
- In Tomcat environment you can modify `CATALINA_OPTS`-variable in *setenv.sh* or *setenv.bat*. Add `-Djsportlet.rest.resource=<resource>`
- Resource syntax examples can be found from [Spring Framework documentation](http://docs.spring.io/autorepo/docs/spring/current/spring-framework-reference/htmlsingle/#resources-resourceloader) 

> Note! Access URL is **/delegate/rest/** but while requesting proxy through Liferay's Delegate Servlet, **/delegate** gets stripped off.

> Note! Background service sees only **/api/todo** part of the request. 
