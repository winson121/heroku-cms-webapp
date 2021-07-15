package com.springcms.frontendwebapplication.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.springcms.frontendwebapplication.dto.UserDTO;
import com.springcms.frontendwebapplication.entity.Role;
import com.springcms.frontendwebapplication.entity.User;
import com.springcms.frontendwebapplication.handler.ClientStatusErrorHandler;
import com.springcms.frontendwebapplication.user.CMSUser;

@Service
public class UserRestServiceImpl implements UserRestService{
	
	private RestTemplate restTemplate;
	
	private String userRestUrl;
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserRestServiceImpl(RestTemplate restTemplate, @Value("${user.rest.url}") String url) {
		this.restTemplate = restTemplate;
		this.userRestUrl = url;
		
		restTemplate.setErrorHandler(new ClientStatusErrorHandler());
		logger.info("Loaded property:  tdl.rest.url=" + userRestUrl);
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	@Override
	public User findByUsername(String username) {
		
		User user;
		// make REST call;
		logger.info("Calling: " + userRestUrl+"/user/getuser/"+username);
		ResponseEntity<User> responseEntity = 
				restTemplate.getForEntity(userRestUrl+"/user/getuser/"+username, User.class);
		
		user = responseEntity.getBody();
		return user;
	}

	@Override
	public void save(CMSUser cmsUser) {
		logger.info("Calling POST method on: " + userRestUrl + "/user/signup");
		UserDTO userDto = new UserDTO();
		
		// assign user details to the user object
		userDto.setUsername(cmsUser.getUsername());
		userDto.setPassword(passwordEncoder.encode(cmsUser.getPassword()));
		userDto.setFirstName(cmsUser.getFirstName());
		userDto.setLastName(cmsUser.getLastName());
		userDto.setEmail(cmsUser.getEmail());
		userDto.setRole(cmsUser.getRole());
		
		// save user in the backend server by calling POST method
		restTemplate.postForEntity(userRestUrl + "/user/signup", userDto, ResponseEntity.class);
		
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}
}
