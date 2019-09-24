package com.ng.restclientdemo.restclientdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExternalAPIProxy implements ExternalAPI {
	@Autowired private RestTemplate basicRestTemplate;
	@Autowired private AccessTokenGenerator accessTokenGenerator;
	
	@Override
	public String getUser(String user) throws Exception {
		String url = "http://localhost:8080/api/user";
		String accessToken = accessTokenGenerator.getCurrentAccessTokenForUser(user).getAccessToken();
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-MYHEADER", user);
		headers.add("Authorization", "Bearer " + accessToken);
		HttpEntity entity = new HttpEntity(headers);
		
		return this.basicRestTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
	}
	
	@Override
	public String createUser(String username, String password) throws Exception {
		String url = "http://localhost:8080/api/user";
		accessTokenGenerator.setAccessTokenForUser(username, password);
		log.info("new user - access token saved");
		String accessToken = accessTokenGenerator.getCurrentAccessTokenForUser(username).getAccessToken();
		log.info("new user - access token fetched: {}", accessToken);
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-MYHEADER", username);
		headers.add("Authorization", "Bearer " + accessToken);
		HttpEntity entity = new HttpEntity(headers);
		
		return this.basicRestTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
	}

}
