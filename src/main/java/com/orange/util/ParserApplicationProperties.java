package com.orange.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.orange.model.*;

@Configuration
@ConfigurationProperties
public class ParserApplicationProperties implements ParserProperties{
	private Map<String, Object> services = new HashMap<>();

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
//		Object serviceProperties = services.get(serviceID);
//		if (serviceProperties instanceof Map<?, ?>) {
////			Map<?, ?> servicePropertiesMap = 
//		}
		plansMap.checkPlansNameNotDuplicated();
		plansMap.setPlansPropertiesDefaults();
		return plansMap;
	}

	@Override
	public CredentialsMap parseCredentialsProperties() {
		// TODO Auto-generated method stub
		return new CredentialsMap();
	}

	@Override
	public String getServiceName(String serviceID) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getPlanName(String serviceID, String planID) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void checkPasswordDefined() throws IllegalArgumentException {
		// TODO Auto-generated method stub
	}

	@Override
	public void checkServiceMandatoryPropertiesDefined(String serviceID) throws IllegalArgumentException {
		// TODO Auto-generated method stub
	}
}
