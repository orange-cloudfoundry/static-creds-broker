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

@EnableGlobalMethodSecurity
@SpringBootApplication(scanBasePackages = { "com.orange" })
public class Application {

	public static void main(String[] args) {
		List<String> mandatoryProperties = Arrays.asList("SECURITY_PASSWORD", "SERVICES_ID_NAME", "SERVICES_ID_DESCRIPTION");
		checkMandatoryPropertiesDefined(mandatoryProperties);
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
		properties.put("security.user.name", username);
		properties.put("security.user.password", password);
		return properties;
	}

	/**
	 * check whether mandatory properties are defined in the system environment 
	 * @param mandatoryProperties List<String> contains property names
	 * @throws IllegalArgumentException when find mandatory property not defined in the system environment , error message contains missing mandatory property name
	 */
	public static void checkMandatoryPropertiesDefined(List<String> mandatoryProperties)
			throws IllegalArgumentException {
		Map<String, String> env = System.getenv();
		for (String mandatoryProperty : mandatoryProperties) {
			if (env.get(mandatoryProperty) == null) {
				throw new IllegalArgumentException("Mandatory property: " + mandatoryProperty + " missing");
			}
		}
	}
}