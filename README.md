# heroku-cms-webapp
Simple Course Management System Rest Web Application built with java Spring framework using Springboot with SpringMVC, Spring Security, AOP, Thymeleaf template engine.
You can see the Backend server Rest API project [here](https://github.com/winson121/heroku-cms-rest-backend). At low-level, the application have the following architecture:

When the client browser want to access a resources, the http request from the browser will first be intercepted by spring security to check if a user is authenticated and authorized to access the web resources(the url), then the UserSessionCheckerAspect will intercept the authenticated request to the controller to check if the user session exist or not. If the 'user' session attribute is null, the "user" is not logged in to the session yet and UserSessionCheckerAspect will throw UserNotLoggedInException, which will be catch by the GlobalExceptionHandler @ControllerAdvice. The GlobalExceptionHandler method for UserNoLoggedInException will then redirect the user to the login form. If the user is logged in, then their request will be passed to the Spring MVC Controller and the controller will call HttpRequest to the REST api url with RestTemplate to perform CRUD operation. if the HttpStatus code is success, the retrieved json data will be mapped to the Model POJO class (e.g. Course, User) and the retrieved data may be displayed by thymeleaf template in HTML page 

<hr>

## Table of Contents
  * [Overview](#overview)
  * [Spring Security](#spring-security)

<a id="overview"/>

## Overview

This is a personal project to learn on how to create REST web application using java Spring framework and not for commercial purpose. Please don't use any personal information when signup for this application.

The Web application allows user to signup as either the teacher role or student role. after signing up, the user could see the whole course catalog, or the user's course catalog which are course that's the user are enrolled to if the user is a student. If the user is a teacher, then the user's course catalog will show the course that is created by the teacher. User could see the details of the course in course catalog. if a user has student role, the user could enroll or unenroll the course. If the user has teacher role, the user could add new courses, update or delete the existing courses that belong to the user. 






