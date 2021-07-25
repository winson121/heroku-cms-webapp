package com.springcms.frontendwebapplication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springcms.frontendwebapplication.service.UserRestService;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter{
	
	// add a reference to our security data source
	@Autowired
	private UserRestService userService;
	
	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		String[] userWhitelist = new String[]{"/home", "/courses", 
			"/courses/courseDetails",
			"/courses/courseCatalog",
			"/courses/userCourses",
			"/courses/search",
			"/courses/searchUserCourses"};
		
		String[] teacherOnlyWhitelist = new String[] {"/courses/saveUserCourse",
				"/courses/showFormForUpdate", "/courses/showFormForAdd",
				"/courses/delete"};
		
		String[] studentOnlyWhitelist = new String[] {"/courses/enroll", "/courses/unenroll"};
		http.authorizeRequests()
			.antMatchers("/").permitAll()
			.antMatchers(userWhitelist).hasAnyRole("STUDENT", "TEACHER", "ADMIN")
			.antMatchers(teacherOnlyWhitelist).hasRole("TEACHER")
			.antMatchers(studentOnlyWhitelist).hasRole("STUDENT")
			.and()
			.formLogin()
			.loginPage("/showLoginPage")
			.loginProcessingUrl("/authenticateTheUser")
			.successHandler(customAuthenticationSuccessHandler)
			.permitAll()
			.and()
			.logout()
			.logoutSuccessUrl("/")
			.permitAll()
			.and()
			.exceptionHandling().accessDeniedPage("/access-denied");
	}
	
	// bcrypt bean definition
	@Bean
	public PasswordEncoder passwordEncoder() {
		// encoder that do nothing but return the string
		return new CustomPasswordEncoder();
	}
	
	// authenticationProvider bean definition
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userService); //set the custom user details service
		auth.setPasswordEncoder(passwordEncoder()); // set the custom password encoder
		return auth;
	}
}