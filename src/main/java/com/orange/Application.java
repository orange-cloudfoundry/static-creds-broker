package com.orange;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.servicebroker.model.BrokerApiVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import static org.springframework.cloud.servicebroker.model.BrokerApiVersion.API_VERSION_ANY;

@EnableGlobalMethodSecurity
@SpringBootApplication(scanBasePackages = { "com.orange" })
public class Application {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		Map<String, Object> properties = new HashMap<String, Object>();
		Map<String, String> env = System.getenv();
		String username = env.get("SECURITY_USER");
		String password = env.get("SECURITY_PASSWORD");
		properties.put("security.user.name", username);
		properties.put("security.user.password", password);
		app.setDefaultProperties(properties);
		app.run(args);
//		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public BrokerApiVersion brokerApiVersion() {
		return new BrokerApiVersion(API_VERSION_ANY);
	}
}