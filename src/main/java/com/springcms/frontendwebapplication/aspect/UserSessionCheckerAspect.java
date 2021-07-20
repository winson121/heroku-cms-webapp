package com.springcms.frontendwebapplication.aspect;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.springcms.frontendwebapplication.entity.User;
import com.springcms.frontendwebapplication.exception.UserNotLoggedInException;

@Aspect
@Component
public class UserSessionCheckerAspect {
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Before("execution(* com.springcms.frontendwebapplication.controller.*.*(..)) && args(.., request)")
	public void performanceApiAnalytics(JoinPoint jointPoint, HttpServletRequest request) {
		logger.info("Check if user attribute exist...");
		// check if a user is logged in
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		if (user == null) {
			throw new UserNotLoggedInException("User are not authorized to access this resource!");
		}
	}
}
