# heroku-cms-webapp
Simple Course Management System Rest Web Application built with java Spring framework using Springboot with SpringMVC, Spring Security, AOP, Thymeleaf template engine.
You can see the Backend server Rest API project [here](https://github.com/winson121/heroku-cms-rest-backend). This is a personal project to learn on how to create REST web application using java Spring framework and not for commercial purpose. Please don't use any personal information when signup for this application.

this is the link to the web application: http://herokuprototypecms.herokuapp.com

## Table of Contents
  * [Overview](#overview)
  * [Spring Security](#spring-security)
  * [Rest Client Service](#rest-service)
  * [Other Resources](#other-resources)
 
<a id="overview"/>

## Overview

The Web application allows user to signup as either the teacher role or student role. after signing up, the user could see the whole course catalog, or the user's course catalog which are course that's the user are enrolled to if the user is a student. If the user is a teacher, then the user's course catalog will show the course that is created by the teacher. User could see the details of the course in course catalog. if a user has student role, the user could enroll or unenroll the course. If the user has teacher role, the user could add new courses, update or delete the existing courses that belong to the user. 
At low-level, the application have the following architecture:

![cms-architecture](https://user-images.githubusercontent.com/45975038/127320391-14eb5de8-1d04-4b78-8302-cdd4c0b6036d.png)

When the client browser want to access resources, the http request from the browser will first be intercepted by spring security to check if a user is authenticated and authorized to access the web resources(the url), then the [UserSessionCheckerAspect](https://github.com/winson121/heroku-cms-webapp/blob/main/src/main/java/com/springcms/frontendwebapplication/aspect/UserSessionCheckerAspect.java) will intercept the authenticated request to the controller to check if the user session exist or not. If the 'user' session attribute is null, the "user" is not logged in to the session yet and UserSessionCheckerAspect will throw UserNotLoggedInException, which will be catch by the GlobalExceptionHandler @ControllerAdvice. The GlobalExceptionHandler method for UserNotLoggedInException will then redirect the user to the login form. If the user is logged in, then their request will be passed to the [Spring MVC Controller](https://github.com/winson121/heroku-cms-webapp/tree/main/src/main/java/com/springcms/frontendwebapplication/controller) and the controller will call HttpRequest to the REST api url from the [RestClientService](https://github.com/winson121/heroku-cms-webapp/tree/main/src/main/java/com/springcms/frontendwebapplication/service) while passing the user credentials to the HttpHeaders for authorization to perform CRUD operation. if the HttpStatus code is success, the retrieved json data will be mapped to the Model POJO class (e.g. Course, User) and the retrieved data may be displayed by thymeleaf template in HTML page.

<a id="spring-security"/>

## Spring Security

To filter which resources a user could enter, we divided the role into Student, Teacher and Admin. In current version of our web application, there are still no specific resources for admin role to enter. The configuration for Filtering which web resources(url) can be seen in [AppSecurityConfig](https://github.com/winson121/heroku-cms-webapp/blob/main/src/main/java/com/springcms/frontendwebapplication/config/AppSecurityConfig.java) class. The following image shows the spring security workflow of the web application:


![Spring Security Overview](https://user-images.githubusercontent.com/45975038/127424745-47243cdb-48ef-4709-95ce-7bab8f0f82f7.png)

<a id="rest-service"/>

## Rest Client Service
when the user access a web resources to get, update, delete or save new data (e.g. signup a new user, login, enroll in course, add/update/delete courses), the Spring MVC Controller will invoke a method from Service object in the [service package](https://github.com/winson121/heroku-cms-webapp/tree/main/src/main/java/com/springcms/frontendwebapplication/service). The service method will then send an HttpRequest to the rest api in the backend server with the credentials of the user attached to the Http headers. The service method will receive a ResponseEntity object from the backend server with the body and status code of the resources. By default, if the status code is of series 400 and 500 series, the [RestTemplate] object(http://www.springframework.net/rest/doc-latest/reference/html/resttemplate.html), which is used to call the REST API will throws RuntimeException. In this web application, we implements a custom error handler in the  [ClientStatusErrorHandler](https://github.com/winson121/heroku-cms-webapp/blob/main/src/main/java/com/springcms/frontendwebapplication/handler/ClientStatusErrorHandler.java) which will log response status code and response headers for 400 series error. This will prevent the web application from stopping when the resources is not found or the ResponseEntity body is null.

<a id="other-resources"/>

## Other Resources

* [Frontend page used for errors](https://freefrontend.com/500-error-page-html-templates/)



