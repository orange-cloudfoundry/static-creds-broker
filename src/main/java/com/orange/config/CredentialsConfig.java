package com.orange.config;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.orange.model.CredentialsMap;
import com.orange.util.ParserProperties;
import com.orange.util.ParserSystemEnvironment;

@Configuration
public class CredentialsConfig {
	
	/**
	 * find the map between all plans of all services and its corresponding credentials.
	 * The credentials defined for the whole services may be overridden by plan specific credentials values, if conflict.
	 * @return a credentialsMap whose key is Arrays.asList(serviceName, planName), and
	 * 			value is the credentials of corresponding plan.
	 */
	@Bean
	public CredentialsMap credentialsMap(){
		ParserProperties parserProperties = new ParserSystemEnvironment();
		CredentialsMap idCredentialsMap = parserProperties.parseCredentialsProperties();
		CredentialsMap nameCredentialsMap = new CredentialsMap();
		// credentials for all plans of the service
		for (Entry<List<String>,Map<String,Object>> entry : idCredentialsMap.getEntrySet()) {
			List<String> service_plan_id = entry.getKey(); 
			if (service_plan_id.size() == 1) { 
				String service_id = service_plan_id.get(0);
				String service_name = parserProperties.getServiceName(service_id);
				for (String plan_name : parserProperties.parsePlansProperties(service_id).getNames()) {
					nameCredentialsMap.addCredentials(service_name, plan_name, entry.getValue());
				}
			}
		}
		// credentials for specific plans
		for (Entry<List<String>,Map<String,Object>> entry : idCredentialsMap.getEntrySet()) {
			List<String> service_plan_id = entry.getKey(); 
			if (service_plan_id.size() == 2) { 
				String service_id = service_plan_id.get(0);
				String service_name = parserProperties.getServiceName(service_id);
				String plan_id = service_plan_id.get(1);
				String plan_name = parserProperties.getPlanName(service_id, plan_id);
				nameCredentialsMap.addCredentials(service_name, plan_name, entry.getValue());
			}
		}
		return nameCredentialsMap;
	}
}
