package com.ng.restclientdemo.restclientdemo.service;

public interface ExternalAPI {

	String getUser(String user) throws Exception;

	String createUser(String username, String password) throws Exception;

}
