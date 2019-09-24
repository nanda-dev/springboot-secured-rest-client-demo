package com.ng.restclientdemo.restclientdemo.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class TempTokenStore {
	private Map<String, TokenPojo> tokenStore;
	public TempTokenStore() {
		this.tokenStore = new HashMap<>();
	}
}
