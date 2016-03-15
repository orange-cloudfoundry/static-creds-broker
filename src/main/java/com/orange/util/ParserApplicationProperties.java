package com.orange.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.orange.model.*;

@Configuration
@ConfigurationProperties
public class ParserApplicationProperties implements ParserProperties{
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
		Object serviceProperties = services.get(serviceID);
		if (serviceProperties instanceof Map<?, ?>) {
			Map<?, ?> servicePropertiesMap = (Map<?, ?>)serviceProperties;
			Object plansProperties = servicePropertiesMap.get("PLAN");
			if (plansProperties instanceof Map<?, ?>) {
				Map<?, ?> plansPropertiesMap = (Map<?, ?>)plansProperties;
				for(Map.Entry<?, ?> entry: plansPropertiesMap.entrySet()){
					if (entry.getKey() instanceof String) {
						String planID = (String)entry.getKey();
						Object planProperties = entry.getValue();
						if (planProperties instanceof Map<?, ?>) {
							Map<?, ?> planPropertiesMap = (Map<?, ?>)planProperties;
							for (PlanPropertyName planPropertyName : PlanPropertyName.values()) {
								Object planPropertyValue = planPropertiesMap.get(planPropertyName.toString());
								if (planPropertyValue != null) {
									plansMap.addPlanProperty(planID, planPropertyName, planPropertyValue.toString());
								}
							}
						}
					}
				}
			}
		}
		plansMap.checkPlansNameNotDuplicated();
		plansMap.setPlansPropertiesDefaults();
		return plansMap;
	}

	@Override
	public CredentialsMap parseCredentialsProperties() {
		CredentialsMap credentialsMap = new CredentialsMap();
		for (Map.Entry<String, Object> entry : services.entrySet()) {
			if (entry.getValue() instanceof Map<?, ?>) {
				Map<?, ?> servicesProperties = (Map<?, ?>) entry.getValue();
				for (Map.Entry<?, ?> serviceProperties : servicesProperties.entrySet()) {
					if ("CREDENTIALS".equals(serviceProperties.getKey())) {
						//TODO yaml could add both value and map ?
						if (serviceProperties.getValue() instanceof Map<?, ?>) {
							for (Map.Entry<?, ?> credentialProperty : ((Map<?, ?>)serviceProperties.getValue()).entrySet() ) {
								credentialsMap.addCredential(entry.getKey(), null, credentialProperty.getKey().toString(), credentialProperty.getValue().toString());
							}
						}
					}
					if ("PLAN".equals(serviceProperties.getKey())) {
						if (serviceProperties.getValue() instanceof Map<?, ?>) {
							for (Map.Entry<?, ?> planProperties : ((Map<?, ?>)serviceProperties.getValue()).entrySet()) {
								if (planProperties.getValue() instanceof Map<?, ?>) {
									for (Map.Entry<?, ?> planProperty : ((Map<?, ?>)planProperties.getValue()).entrySet()) {
										if ("CREDENTIALS".equals(planProperty.getKey())) {
											if (planProperty.getValue() instanceof Map<?, ?>) {
												for (Map.Entry<?, ?> credentialProperty : ((Map<?, ?>)planProperty.getValue()).entrySet() ) {
													credentialsMap.addCredential(entry.getKey(), planProperties.getKey().toString(), credentialProperty.getKey().toString(), credentialProperty.getValue().toString());
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return credentialsMap;
	}

	@Override
	public String getServiceName(String serviceID) {
		Object serviceProperties = this.services.get(serviceID);
		if (serviceProperties instanceof Map<?, ?>) {
			return ((Map<?, ?>)serviceProperties).get("NAME").toString();
		}
		return null;
	}

	@Override
	public String getPlanName(String serviceID, String planID) {
		Object serviceProperties = this.services.get(serviceID);
		if (serviceProperties instanceof Map<?, ?>) {
			Object plansProperties = ((Map<?, ?>)serviceProperties).get("PLAN");
			if (plansProperties instanceof Map<?, ?>) {
				Object planProperties = ((Map<?, ?>)plansProperties).get(planID);
				if (planProperties instanceof Map<?, ?>) {
					return ((Map<?, ?>)planProperties).get("NAME").toString();
				}
			}
		}
		return null;
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
}
