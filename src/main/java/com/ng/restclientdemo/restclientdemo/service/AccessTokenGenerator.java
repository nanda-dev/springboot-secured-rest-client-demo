package com.ng.restclientdemo.restclientdemo.service;

import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ng.restclientdemo.restclientdemo.dto.AccessTokenResponse;
import com.ng.restclientdemo.restclientdemo.dto.TempTokenStore;
import com.ng.restclientdemo.restclientdemo.dto.TokenPojo;

@Service
public class AccessTokenGenerator {
	
	@Autowired private TempTokenStore tempTokenStore;
	
	@Autowired private RestTemplate basicRestTemplate;
	
	public TokenPojo getCurrentAccessTokenForUser(String user) throws Exception {
		Map<String, TokenPojo> tokens = tempTokenStore.getTokenStore();
		
		if(tokens == null || tokens.isEmpty()) {
			//generateToken for user?
			throw new Exception("getCurrentAccessTokenForUser::Token not yet created for this user: " + user);
		} else {
			return tokens.get(user);
		}
	}
	
	public TokenPojo getAccessTokenFromRefreshTokenForUser(String user, String refreshToken) throws Exception {
		Map<String, TokenPojo> tokens = tempTokenStore.getTokenStore();
		
		if(tokens == null || tokens.isEmpty()) {
			//generateToken for user?
			throw new Exception("getAccessTokenFromRefreshToken::Token not yet created for this user: " + user);
		} else {
			AccessTokenResponse newTokens = this.getAccessTokenFromRefreshToken(refreshToken);
			tokens.put(user, new TokenPojo(newTokens.getAccessToken(), newTokens.getRefeshToken()));
			return tokens.get(user);
		}
	}
	
	public void setAccessTokenForUser(String username, String password) {
		AccessTokenResponse newTokens = this.getNewAccessToken(username, password);
		Map<String, TokenPojo> tokens = tempTokenStore.getTokenStore();
		tokens.put(username, new TokenPojo(newTokens.getAccessToken(), newTokens.getRefeshToken()));
		
	}
	
	public AccessTokenResponse getNewAccessToken(String username, String password) {
		String url = "http://localhost:8080/oauth/token";
		
		String auth = "client:secret";
		//byte[] encodedAuth = Base64.encodeBase64( 
           //auth.getBytes(Charset.forName("US-ASCII")) );        
		byte[] plainCredsBytes = auth.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        //String base64Creds = new String(base64CredsBytes);
        //String authHeader = "Basic " + new String( encodedAuth );
        String authHeader = "Basic " + new String( base64CredsBytes );
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);

        //AccessTokenRequestPassword req = new AccessTokenRequestPassword(username, password, "password");
        //HttpEntity<AccessTokenRequestPassword> entity = new HttpEntity<>(req, headers);
        
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);
        map.add("grant_type", "password");
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
                //.queryParam("client_id", "client")
                //.queryParam("client_secret", "secret")
                //.queryParam("username", username)
                //.queryParam("password", password)
                //.queryParam("grant_type", "password");
		return this.basicRestTemplate.exchange(builder.toUriString(), HttpMethod.POST, request, AccessTokenResponse.class).getBody();
	}
	
	public AccessTokenResponse getAccessTokenFromRefreshToken(String refreshToken) {
		String url = "http://localhost:8080/oauth/token";
		
		String auth = "client:secret";
		byte[] plainCredsBytes = auth.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String authHeader = "Basic " + new String( base64CredsBytes );
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("refresh_token", refreshToken);
        map.add("grant_type", "refresh_token");
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
                //.queryParam("client_id", "client")
                //.queryParam("client_secret", "secret")
                //.queryParam("refresh_token", refreshToken)
                //.queryParam("grant_type", "refresh_token");
		return this.basicRestTemplate.exchange(builder.toUriString(), HttpMethod.POST, request, AccessTokenResponse.class).getBody();
	}

}
