package com.ng.restclientdemo.restclientdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ng.restclientdemo.restclientdemo.dto.AccessTokenResponse;
import com.ng.restclientdemo.restclientdemo.service.AccessTokenGenerator;
import com.ng.restclientdemo.restclientdemo.service.DemoService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class DemoController {
	
	@Autowired private AccessTokenGenerator accessTokenGenerator;
	@Autowired private DemoService demoService;
	
	@GetMapping("/getuseraccesstoken")
	public AccessTokenResponse getUserAT() {
		return accessTokenGenerator.getNewAccessToken("user", "pwd");
	}
	
	@PostMapping("/getuseraccesstoken2")
	public AccessTokenResponse getUserAT2(@RequestBody String refreshToken) {
		return accessTokenGenerator.getAccessTokenFromRefreshToken(refreshToken);
	}
	
	@PostMapping("/createuser")
	public String createUser() throws Exception {
		demoService.createUser();
		return "SUCCESS";
	}
	
	@GetMapping("/getuser")
	public String getUser() throws Exception {
		return demoService.getUser();
	}

}
