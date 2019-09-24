package com.ng.restclientdemo.restclientdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenPojo {
	private String accessToken;
	private String refreshToken;
}
