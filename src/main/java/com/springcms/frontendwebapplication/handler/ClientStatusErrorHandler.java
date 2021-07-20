package com.springcms.frontendwebapplication.handler;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

// http response error handler class for RestTemplate
public class ClientStatusErrorHandler implements ResponseErrorHandler{
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		return new DefaultResponseErrorHandler().hasError(response);
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		// handle error for status code 4xx
		if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
			
			logger.info("==>Error status code: " + response.getStatusCode());
			logger.info("==>Http headers: " + response.getHeaders());
		}
	}


}
