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
		app.setDefaultProperties(getSecurityProperties());
		app.run(args);
//		SpringApplication.run(Application.class, args);
	}

	@Bean
	public BrokerApiVersion brokerApiVersion() {
		return new BrokerApiVersion(API_VERSION_ANY);
	}

	/**
	 * get SecurityProperties for system environment variables
	 * 
	 * @return SecurityProperties which contains security.user.name and
	 *         security.user.password
	 */
	public static Map<String, Object> getSecurityProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
		Map<String, String> env = System.getenv();
		String username = env.get("SECURITY_USER");
		String password = env.get("SECURITY_PASSWORD");
		Map<String, Object> mandatoryProperties = new HashMap<String, Object>();
		mandatoryProperties.put("SECURITY_PASSWORD", password);
		checkMandatoryPropertiesDefined(mandatoryProperties);
		properties.put("security.user.name", username);
		properties.put("security.user.password", password);
		return properties;
	}

	/**
	 * throw IllegalArgumentException when the mandatory property
	 * (SECURITY_PASSWORD) is not set
	 * 
	 * @throws IllegalArgumentException
	 */
	public static void checkMandatoryPropertiesDefined(Map<String, Object> mandatoryProperties)
			throws IllegalArgumentException {
		for (Map.Entry<String, Object> mandatoryProperty : mandatoryProperties.entrySet()) {
			if (mandatoryProperty.getValue() == null) {
				throw new IllegalArgumentException("Mandatory property: " + mandatoryProperty.getKey() + " missing");
			}
		}
	}
}