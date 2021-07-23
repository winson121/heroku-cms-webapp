package com.springcms.frontendwebapplication.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.springcms.frontendwebapplication.exception.UserNotLoggedInException;

@ControllerAdvice
public class GlobalExceptionHandler{
	
	// Add exception handler for UserNotLoggedInException
	@ExceptionHandler
	public String handleException(UserNotLoggedInException exc, HttpServletRequest request) {
		return "redirect:/showLoginPage";
	}
}
