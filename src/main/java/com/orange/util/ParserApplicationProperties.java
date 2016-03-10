package com.orange.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.orange.model.CredentialsMap;
import com.orange.model.PlansMap;
import com.orange.model.ServicePropertyName;
import com.orange.model.ServicesMap;

@Configuration
@ConfigurationProperties
public class ParserApplicationProperties implements ParserProperties{
	private Map<String, Object> servicesConfig = new HashMap<>();

	public Map<String, Object> getServices() {
		return servicesConfig;
	}

	public void setServices(Map<String, Object> services) {
		this.servicesConfig = services;
	}
	
	@Override
	public ServicesMap parseServicesProperties() {
		ServicesMap servicesMap = new ServicesMap();
		for (Map.Entry<String, Object> entry : servicesConfig.entrySet()) {
			Map<String, Object> serviceProperty = (Map<String, Object>) entry.getValue();
			for (ServicePropertyName servicePropertyName : ServicePropertyName.values()) {
				Object servicePropertyValue = serviceProperty.get(servicePropertyName.toString());
				if (servicePropertyValue != null) {
					servicesMap.addServiceProperty(entry.getKey(), servicePropertyName, servicePropertyValue.toString(), this);
				}
			}
		}
		return servicesMap;
	}

	@Override
	public PlansMap parsePlansProperties(String serviceID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CredentialsMap parseCredentialsProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServiceName(String serviceID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlanName(String serviceID, String planID) {
		// TODO Auto-generated method stub
		return null;
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
