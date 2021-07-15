package com.springcms.frontendwebapplication.exception;

public class UserNotLoggedInException extends RuntimeException{

	public UserNotLoggedInException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UserNotLoggedInException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UserNotLoggedInException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
