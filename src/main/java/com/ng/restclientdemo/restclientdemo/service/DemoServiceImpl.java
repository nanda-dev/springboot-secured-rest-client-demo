package com.ng.restclientdemo.restclientdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DemoServiceImpl implements DemoService {
	@Autowired private ExternalAPI api;
	
	@Override
	public void createUser() throws Exception {
		api.createUser("user", "pwd");
	}
	
	@Override
	public String getUser() throws Exception {
		return api.getUser("user");
	}

}
