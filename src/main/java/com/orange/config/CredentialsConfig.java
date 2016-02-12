package com.orange.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.model.Credentials;

@Configuration
public class CredentialsConfig {
	@Value("#{ systemEnvironment['SERVICES_ID_CREDENTIALS_URI'] }")
	private String credentials_uri;
	@Value("#{ systemEnvironment['SERVICES_ID_CREDENTIALS_HOSTNAME'] }")
	private String credentials_hostname;
	@Value("#{ systemEnvironment['SERVICES_ID_CREDENTIALS_MYOWNKEY'] }")
	private String credentials_myownkey_str;
	
	@Bean
	public Credentials credentials(){
		Map<String, Object> credentials = new HashMap<String, Object>();
		if (credentials_uri != null) {
			credentials.put("uri", credentials_uri);
		}
		if (credentials_hostname != null) {
			credentials.put("hostname", credentials_hostname);
		}
		if (credentials_myownkey_str != null) {
			if (!credentials_myownkey_str.startsWith("{")) { // my_own_key is no a json hash
				credentials.put("myownkey", credentials_myownkey_str);
			}
			else {
				Map<String, Object> myownkey_map = new HashMap<>();
				ObjectMapper mapper = new ObjectMapper();
				try {
					myownkey_map = mapper.readValue(credentials_myownkey_str, new TypeReference<Map<String, String>>(){});
					credentials.putAll(myownkey_map);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new Credentials(credentials);
	}

}
