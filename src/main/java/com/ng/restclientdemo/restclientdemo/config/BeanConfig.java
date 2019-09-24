package com.ng.restclientdemo.restclientdemo.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.ng.restclientdemo.restclientdemo.dto.TempTokenStore;

@Configuration
public class BeanConfig {
	
	@Bean
	public RestTemplate basicRestTemplate() {
		ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory());
		//HttpComponentsClientHttpRequestFactory / SimpleClientHttpRequestFactory
		RestTemplate restTemplate = new RestTemplate(factory);
		restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
		return restTemplate;
	}
	
	@Bean
	public TempTokenStore tempTokenStore() {
		return new TempTokenStore();
	}

}
