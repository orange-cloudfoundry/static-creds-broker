package com.orange.model;

import java.util.HashMap;
import java.util.Map;

public class Service {

	private Map<ServicePropertyName, String> serviceProperties = new HashMap<>();
	
	public String get(ServicePropertyName propertyName) {
		return serviceProperties.get(propertyName);
	}
	
	public void setProperty(ServicePropertyName propertyName, String propertyValue) {
		this.serviceProperties.put(propertyName, propertyValue);
	}

}
