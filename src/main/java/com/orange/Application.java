package com.orange;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.servicebroker.model.BrokerApiVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import static org.springframework.cloud.servicebroker.model.BrokerApiVersion.API_VERSION_ANY;

import com.orange.util.ParserSystemEnvironment;

@EnableGlobalMethodSecurity
@SpringBootApplication(scanBasePackages = { "com.orange" })
public class Application {

	public static void main(String[] args) {
		List<String> mandatoryProperties = Arrays.asList("SECURITY_USER_PASSWORD"); 
		ParserSystemEnvironment.checkMandatoryPropertiesDefined(mandatoryProperties);
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public BrokerApiVersion brokerApiVersion() {
		return new BrokerApiVersion(API_VERSION_ANY);
	}
}