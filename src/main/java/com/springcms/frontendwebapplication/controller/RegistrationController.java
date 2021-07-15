package com.springcms.frontendwebapplication.controller;

import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springcms.frontendwebapplication.entity.User;
import com.springcms.frontendwebapplication.service.UserRestService;
import com.springcms.frontendwebapplication.user.CMSUser;

@Controller
@RequestMapping("/register")
public class RegistrationController {

	@Autowired
	private UserRestService userService;
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	// add support to trim empty string to null
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping("/showRegistrationForm")
	public String showRegistrationPage(Model model) {
		
		model.addAttribute("CMSUser", new CMSUser());
		
		return "registration-form";
	}
	
	@PostMapping("/processRegistrationForm")
	public String processRegistrationForm(
			@Valid @ModelAttribute("CMSUser") CMSUser cmsUser,
			BindingResult bindingResult,
			Model model) {
		
		// form validation
		if(bindingResult.hasErrors()) {
			return "registration-form";
		}
		
		String username = cmsUser.getUsername();
		logger.info("Processing registration form for: " + username);
		
		// check the database if user already exists
		User existing = userService.findByUsername(username);
		logger.info("Fetched Username: " + existing);
		if(existing != null) {
			model.addAttribute("CMSUser", new CMSUser());
			model.addAttribute("registrationError", "User name already exists.");
			
			logger.warning("User name already exists.");
			return "registration-form";
		}
		
		// create user account
		userService.save(cmsUser);
		
		logger.info("Successfully create user: " + username);
		
		return "redirect:/register/registrationConfirmation";
	}
	
	@GetMapping("/registrationConfirmation")
	public String registrationConfirmation() {
		return "registration-confirmation";
	}

}
