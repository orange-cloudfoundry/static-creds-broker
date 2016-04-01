package com.orange.config;

import java.util.Map.Entry;

import com.orange.model.Credentials;
import com.orange.model.ParsedCredentialsRepository;
import com.orange.model.ServicePlanID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.orange.util.ParserProperties;
import com.orange.model.ParsingCredentialsRepository;

@Configuration
public class CredentialsConfig {
	@Autowired
	@Qualifier("parserProperties")
	private ParserProperties parserProperties;
	/**
	 * find the map between all plans of all services and its corresponding credentials.
	 * The credentials defined for the whole services may be overridden by plan specific credentials values, if conflict.
	 * @return a credentialsMap whose key is Arrays.asList(serviceName, planName), and
	 * 			value is the credentials of corresponding plan.
	 */
	@Bean
	public ParsedCredentialsRepository parsedCredentialsRepository(){
		ParsingCredentialsRepository parsingCredentialsRepository = parserProperties.parseCredentialsProperties();
		parserProperties.checkAllServicesPlansHaveCredentialDefinition(parsingCredentialsRepository);
		ParsedCredentialsRepository parsedCredentialsRepository = new ParsedCredentialsRepository();
		// credentials for all plans of the service
		for (Entry<String, Credentials> entry : parsingCredentialsRepository.findAllServicesCredentials()) {
			String service_id = entry.getKey();
			String service_name = parserProperties.getServiceName(service_id);
			for (String plan_name : parserProperties.parsePlansProperties(service_id).getNames()) {
				parsedCredentialsRepository.save(service_name, plan_name, entry.getValue());
			}
		}
		// credentials for specific plans
		for (Entry<ServicePlanID, Credentials> entry : parsingCredentialsRepository.findAllPlansCredentials()) {
			ServicePlanID service_plan_id = entry.getKey();
			String service_id = service_plan_id.getServiceId();
			String service_name = parserProperties.getServiceName(service_id);
			String plan_id = service_plan_id.getPlanId();
			String plan_name = parserProperties.getPlanName(service_id, plan_id);
			parsedCredentialsRepository.save(service_name, plan_name, entry.getValue());
		}
		return parsedCredentialsRepository;
	}
}
