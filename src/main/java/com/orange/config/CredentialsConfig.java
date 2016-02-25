package com.orange.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.model.CredentialsMap;

@Configuration
public class CredentialsConfig {
	
	@Bean
	public CredentialsMap credentialsMap(){
		CredentialsMap credentialsMap = new CredentialsMap();
		Map<String, String> env = System.getenv();
		for (Map.Entry<String, String> entry : env.entrySet()) {
			String[] serviceIDandCredentialName = getServiceIDandCredentialName(entry.getKey());
			if (serviceIDandCredentialName == null) {
				continue;
			}
			// no credential name means its value is a json hash string which contains multiple credentials
			if (serviceIDandCredentialName.length == 1) {
				credentialsMap.addCredentials(serviceIDandCredentialName[0], parseCredentialsJSON(entry.getValue()));
			}
			else {
				credentialsMap.addCredential(serviceIDandCredentialName[0], serviceIDandCredentialName[1], entry.getValue());
			}
		}
		return credentialsMap;
	}
	
	/**
	 * get the service id and service property name from a system env variable key which starts with "SERVICES_" and contains "_CREDENTIALS"
	 * @param key
	 * @return
	 */
	private static String[] getServiceIDandCredentialName(String key){
		if (!key.startsWith("SERVICES_") || !key.contains("_CREDENTIALS"))
			return null;
		int indexIDStart = "SERVICES_".length();
		int indexIDEnd = key.indexOf("_CREDENTIALS");
		String serviceID = key.substring(indexIDStart,indexIDEnd);
		if (key.endsWith("_CREDENTIALS")) {
			return new String[]{serviceID};
		}
		else if (key.contains("_CREDENTIALS_")) {
			int indexCredential = indexIDEnd + "_CREDENTIALS_".length();
			String credentialName = key.substring(indexCredential);
			return new String[]{serviceID, credentialName};
		}
		return null;
	}
	
	private Map<String, Object> parseCredentialsJSON(String credentials_str){
		Map<String, Object> credentials = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			credentials = mapper.readValue(credentials_str, new TypeReference<Map<String, String>>(){});
		} catch (IOException e) {
			throw new IllegalArgumentException("JSON parsing error: " + credentials_str);
		}
		return credentials;
	}
	
}
