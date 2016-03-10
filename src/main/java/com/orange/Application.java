package com.orange;

import java.util.HashMap;
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
		new ParserSystemEnvironment().checkPasswordDefined();
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public BrokerApiVersion brokerApiVersion() {
		return new BrokerApiVersion(API_VERSION_ANY);
	}
}