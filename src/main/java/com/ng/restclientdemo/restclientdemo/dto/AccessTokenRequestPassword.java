package com.ng.restclientdemo.restclientdemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenRequestPassword {
	private String username;
	private String password;
	@JsonProperty("grant_type")
	private String grantType;

}
