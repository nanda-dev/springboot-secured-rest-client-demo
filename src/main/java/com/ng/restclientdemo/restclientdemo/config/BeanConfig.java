package com.ng.restclientdemo.restclientdemo.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.ng.restclientdemo.restclientdemo.dto.TempTokenStore;
import com.ng.restclientdemo.restclientdemo.service.AccessTokenGenerator;

@Configuration
public class BeanConfig {
	@Autowired private AccessTokenGenerator accessTokenGenerator;
	
	@Bean	
	public RestTemplate basicRestTemplate() {
		ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory());
		//HttpComponentsClientHttpRequestFactory / SimpleClientHttpRequestFactory
		RestTemplate restTemplate = new RestTemplate(factory);
		restTemplate.setInterceptors(Collections.singletonList(requestResponseLoggingInterceptor()));
		//restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor(accessTokenGenerator)));
		return restTemplate;
	}
	
	@Bean
	public TempTokenStore tempTokenStore() {
		return new TempTokenStore();
	}
	
	@Bean
	public RequestResponseLoggingInterceptor requestResponseLoggingInterceptor() {
		return new RequestResponseLoggingInterceptor();
	}

}
