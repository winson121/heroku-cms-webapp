package com.springcms.frontendwebapplication.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AOPExpression {

	@Pointcut("execution(* com.springcms.frontendwebapplication.controller.*.*(..))")
	public void forControllerPackage() {}
	
	@Pointcut("execution(* com.springcms.frontendwebapplication.controller.AppController.showLanding())")
	public void forLandingPage() {}
	
	@Pointcut("execution(* com.springcms.frontendwebapplication.controller.LoginController.*(..))")
	public void forLoginController() {}
	
	@Pointcut("execution(* com.springcms.frontendwebapplication.controller.RegistrationController.*(..))")
	public void forRegistrationController() {}
	
	@Pointcut("forControllerPackage() && !(forLoginController() || forRegistrationController() || forLandingPage())")
	public void forControllerPackageNoLandingOrAuthPage() {}
}
