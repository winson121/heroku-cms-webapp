package com.springcms.frontendwebapplication.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.springcms.frontendwebapplication.entity.User;
import com.springcms.frontendwebapplication.formentity.CMSUser;

public interface UserRestService extends UserDetailsService{
	
	User findByUsername(String username);
	
	void save(CMSUser cmsUser);
}
