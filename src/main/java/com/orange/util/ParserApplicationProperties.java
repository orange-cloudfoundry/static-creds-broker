package com.orange.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.orange.model.*;

@Configuration
@Qualifier("parserApplicationProperties")
@ConfigurationProperties
public class ParserApplicationProperties extends ParserProperties{
	private Map<String, Object> services = new HashMap<>();
	@Value("${security.user.password}")
	private String password;

	public Map<String, Object> getServices() {
		return services;
	}

	public void setServices(Map<String, Object> services) {
		this.services = services;
	}
	
	@Override
	public ServicesMap parseServicesProperties() {
		ServicesMap servicesMap = new ServicesMap();
		for (Map.Entry<String, Object> entry : services.entrySet()) {
			if (entry.getValue() instanceof Map<?, ?>) {
				Map<?, ?> serviceProperties = (Map<?, ?>) entry.getValue();
				for (ServicePropertyName servicePropertyName : ServicePropertyName.values()) {
					if (servicePropertyName.toString().contains("METADATA_")) {
						if (serviceProperties.get("METADATA") instanceof Map<?, ?>) {
							Map<?, ?> serviceMetadataProperties = (Map<?, ?>) serviceProperties.get("METADATA");
							String metadataPropertyName = servicePropertyName.toString().substring("METADATA_".length());
							Object metadataPropertyValue = serviceMetadataProperties.get(metadataPropertyName);
							if (metadataPropertyValue != null) {
								servicesMap.addServiceProperty(entry.getKey(), servicePropertyName, metadataPropertyValue.toString(), this);
							}
						}
					}
					else {
						Object propertyValue = serviceProperties.get(servicePropertyName.toString());
						if (propertyValue != null) {
							servicesMap.addServiceProperty(entry.getKey(), servicePropertyName, propertyValue.toString(), this);
						}
					}
				}
			}
		}
		servicesMap.checkServicesNameNotDuplicated();
		servicesMap.setServicesPropertiesDefaults();
		return servicesMap;
	}

	@Override
	public PlansMap parsePlansProperties(String serviceID) {
		PlansMap plansMap = new PlansMap();
		Object plansProperties = getNestedMapValue(services, serviceID, "PLAN");
		if (plansProperties instanceof Map<?, ?>) {
			for (Map.Entry<?, ?> planProperties : ((Map<?, ?>) plansProperties).entrySet()) {
				String planID = planProperties.getKey().toString();
				plansMap.addPlanWithoutProperty(planID);
				if (planProperties.getValue() instanceof Map<?, ?>) {
					Map<?, ?> planPropertiesMap = (Map<?, ?>) planProperties.getValue();
					for (PlanPropertyName planPropertyName : PlanPropertyName.values()) {
						Object planPropertyValue = planPropertiesMap.get(planPropertyName.toString());
						if (planPropertyValue != null) {
							plansMap.addPlanProperty(planID, planPropertyName, planPropertyValue.toString());
						}
					}
				}
			}
		}
		plansMap.setPlansPropertiesDefaults();
		plansMap.checkPlansNameNotDuplicated();
		return plansMap;
	}

	@Override
	public ParsingCredentialsRepository parseCredentialsProperties() {
		ParsingCredentialsRepository credentialsRepository = new ParsingCredentialsRepository();
		for (Map.Entry<String, Object> entry : services.entrySet()) {
			String serviceID = entry.getKey();
			Map<?, ?> servicesProperties = (Map<?, ?>) entry.getValue();
			if (servicesProperties instanceof Map<?, ?>) {
				Object serviceCredentials = getNestedMapValue((Map<?, ?>) servicesProperties, "CREDENTIALS");
				if (serviceCredentials instanceof Map<?, ?>) {
					for (Map.Entry<?, ?> credentialProperty : ((Map<?, ?>)serviceCredentials).entrySet() ) {
						credentialsRepository.save(serviceID, credentialProperty.getKey().toString(), credentialProperty.getValue().toString(), this);
					}
				}
				Object serviceCredentialsJson = getNestedMapValue((Map<?, ?>) servicesProperties, "CREDENTIALS_JSON");
				if (serviceCredentialsJson instanceof String) {
					credentialsRepository.save(serviceID, parseCredentialsJSON(serviceCredentialsJson.toString()), this);
				}
				Object plansProperties = getNestedMapValue((Map<?, ?>) servicesProperties, "PLAN");
				if (plansProperties instanceof Map<?, ?>) {
					for (Map.Entry<?, ?> planProperties : ((Map<?, ?>) plansProperties).entrySet()) {
						String planID = planProperties.getKey().toString();
						if (planProperties.getValue() instanceof Map<?, ?>) {
							Object plansCredentials = getNestedMapValue((Map<?, ?>) planProperties.getValue(), "CREDENTIALS");
							if (plansCredentials instanceof Map<?, ?>) {
								for (Map.Entry<?, ?> credentialProperty : ((Map<?, ?>)plansCredentials).entrySet() ) {
									credentialsRepository.save(serviceID, planID, credentialProperty.getKey().toString(), credentialProperty.getValue().toString(), this);
								}
							}
							Object plansCredentialsJson = getNestedMapValue((Map<?, ?>) planProperties.getValue(), "CREDENTIALS_JSON");
							if (plansCredentialsJson instanceof String) {
								credentialsRepository.save(serviceID, planID, parseCredentialsJSON(plansCredentialsJson.toString()), this);
							}
						}
					}
				}
			}
		}
		return credentialsRepository;
	}

	@Override
	public String getServiceName(String serviceID) {
		return getNestedMapValue(services, serviceID, "NAME").toString();
	}

	@Override
	public String getPlanName(String serviceID, String planID) {
		return getNestedMapValue(services, serviceID, "PLAN", planID, "NAME").toString();
	}

	@Override
	public void checkPasswordDefined() throws IllegalArgumentException {
		if(this.password == null){
			throw new IllegalArgumentException("Mandatory property: security.user.password missing");
		}
	}

	@Override
	public void checkServiceMandatoryPropertiesDefined(String serviceID) throws IllegalArgumentException {
		Object serviceProperties = this.services.get(serviceID);
		if (serviceProperties instanceof Map<?, ?>) {
			Map<?, ?> servicePropertiesMap = (Map<?, ?>)serviceProperties;
			if (servicePropertiesMap.get("NAME") == null) {
				throw new IllegalArgumentException("Mandatory property: service." + serviceID + ".NAME missing");
			}
			if (servicePropertiesMap.get("DESCRIPTION") == null) {
				throw new IllegalArgumentException("Mandatory property: service." + serviceID + ".DESCRIPTION missing");
			}
		}
	}
	
	public static Object getNestedMapValue(Map<?, ?> nestedMap, String... nestedKeys) {
		Map<?, ?> currentLevelMap = nestedMap;
		for (int i = 0; i < nestedKeys.length; i++) {
			String key = nestedKeys[i];
			if (i == nestedKeys.length - 1) {
				return currentLevelMap.get(key);
			} else {
				if (currentLevelMap.get(key) instanceof Map<?, ?>) {
					currentLevelMap = (Map<?, ?>) (currentLevelMap.get(key));
				} else {
					break;
				}
			}
		}
		return null;
	}
}
