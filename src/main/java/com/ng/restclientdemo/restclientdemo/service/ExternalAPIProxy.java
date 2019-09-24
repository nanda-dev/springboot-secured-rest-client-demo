package com.ng.restclientdemo.restclientdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ng.restclientdemo.restclientdemo.dto.AccessTokenResponse;

@Service
public class ExternalAPIProxy implements ExternalAPI {
	@Autowired private RestTemplate basicRestTemplate;
	
	public String getUser() {
		String url = "http://localhost:8080/api/user";
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-MYHEADER", "007");
		headers.add("Authentication", "Bearer ");
		HttpEntity entity = new HttpEntity(headers);
		
		return this.basicRestTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
	}
	
	
	public AccessTokenResponse getNewAccessToken(String username, String password) {
		String url = "http://localhost:8080/oauth/token";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("client_id", "client")
                .queryParam("client_secret", "secret")
                .queryParam("username", username)
                .queryParam("password", password)
                .queryParam("grant_type", "password");
		return this.basicRestTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, AccessTokenResponse.class).getBody();
	}
	
	public AccessTokenResponse getAccessTokenFromRefreshToken(String refreshToken) {
		String url = "http://localhost:8080/oauth/token";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("client_id", "client")
                .queryParam("client_secret", "secret")
                .queryParam("refresh_token", refreshToken)
                .queryParam("grant_type", "refresh_token");
		return this.basicRestTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, AccessTokenResponse.class).getBody();
	}
	

}
