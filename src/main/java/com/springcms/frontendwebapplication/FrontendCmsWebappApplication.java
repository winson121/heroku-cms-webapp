package com.springcms.frontendwebapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableAspectJAutoProxy
public class FrontendCmsWebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontendCmsWebappApplication.class, args);
	}
	
	// define bean for RestTemplate to make client REST calls
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
