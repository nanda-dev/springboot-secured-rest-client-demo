package com.ng.restclientdemo.restclientdemo.config;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.ng.restclientdemo.restclientdemo.dto.TokenPojo;
import com.ng.restclientdemo.restclientdemo.service.AccessTokenGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Component
public class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {
	@Autowired
	private AccessTokenGenerator accessTokenGenerator;
	//@Autowired
	//private ApplicationContext context;

	// private final Logger log = LoggerFactory.getLogger(this.getClass());

	//public RequestResponseLoggingInterceptor() {
	//	super();
	//	this.accessTokenGenerator = (AccessTokenGenerator) context.getBean("accessTokenGenerator");
	//}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		logRequest(request, body);
		ClientHttpResponse response = execution.execute(request, body);
		if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
			log.info("REQ-INTERCEPTOR::UNAUTHORIZED");
			HttpHeaders headers = request.getHeaders();
			List<String> headerVals = headers.remove("X-MYHEADER");
			if (headerVals != null && !headerVals.isEmpty()) {
				String username = headerVals.get(0);
				try {
					// TODO: Fix accessTokenGenerator == null
					log.info("{}, {}", (accessTokenGenerator == null), "test");
					String currentRefreshToken = accessTokenGenerator.getCurrentAccessTokenForUser(username)
							.getRefreshToken();
					TokenPojo newTokens = accessTokenGenerator.getAccessTokenFromRefreshTokenForUser(username,
							currentRefreshToken);
					headers.remove("Authorization");
					headers.add("Authorization", "Bearer " + newTokens.getAccessToken());
					response = execution.execute(request, body);
				} catch (Exception e) {
					log.error("REQ-INTERCEPTOR::ERROR WHILE FETCHING ACCESSTOKENS");
					e.printStackTrace();
				}
			} else {
				// throw error?
				log.error("REQ-INTERCEPTOR::CANNOT FIND USERNAME IN HEADER");
			}
		}
		logResponse(response);
		return response;
	}

	private void logRequest(HttpRequest request, byte[] body) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("===========================request begin================================================");
			log.debug("URI         : {}", request.getURI());
			log.debug("Method      : {}", request.getMethod());
			log.debug("Headers     : {}", request.getHeaders());
			log.debug("Request body: {}", new String(body, "UTF-8"));
			log.debug("==========================request end================================================");
		}
	}

	private void logResponse(ClientHttpResponse response) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("============================response begin==========================================");
			log.debug("Status code  : {}", response.getStatusCode());
			log.debug("Status text  : {}", response.getStatusText());
			log.debug("Headers      : {}", response.getHeaders());
			log.debug("Response body: {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
			log.debug("=======================response end=================================================");
		}
	}
}