package com.orange.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.orange.model.CredentialsMap;
import com.orange.util.ParserSystemEnvironment;

@Configuration
public class CredentialsConfig {
	
	@Bean
	public CredentialsMap credentialsMap(){
		return ParserSystemEnvironment.parseCredentialsProperties();
	}
	
}
