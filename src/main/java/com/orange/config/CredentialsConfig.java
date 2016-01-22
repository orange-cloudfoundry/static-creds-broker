package com.orange.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.orange.model.Credentials;

@Configuration
public class CredentialsConfig {
	@Value("#{ systemEnvironment['SERVICES_ID_CREDENTIALS_URI'] }")
	private String credentials_uri;
	@Value("#{ systemEnvironment['SERVICES_ID_CREDENTIALS_HOSTNAME'] }")
	private String credentials_hostname;
	@Value("#{ systemEnvironment['SERVICES_ID_CREDENTIALS_MYOWNKEY'] }")
	private String credentials_myownkey;
	
	@Bean
	public Credentials credentials(){
		Map<String, Object> credentials = new HashMap<String, Object>();
		if (credentials_uri != null) {
			credentials.put("uri", credentials_uri);
		}
		if (credentials_hostname != null) {
			credentials.put("hostname", credentials_hostname);
		}
		if (credentials_myownkey != null) {
			credentials.put("myownkey", credentials_myownkey);
		}
		return new Credentials(credentials);
	}

}
