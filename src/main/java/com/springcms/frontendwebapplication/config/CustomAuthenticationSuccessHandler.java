package com.springcms.frontendwebapplication.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.springcms.frontendwebapplication.entity.User;
import com.springcms.frontendwebapplication.service.UserRestService;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
	
	@Autowired
	private UserRestService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		String userName = authentication.getName();
		
		
		User user = userService.findByUsername(userName);
		
		// now place in the session
		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		
		// forward to home page
		
		response.sendRedirect(request.getContextPath() + "/home");
		
	}

}
