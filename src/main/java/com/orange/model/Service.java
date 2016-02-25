package com.orange.model;

import java.util.HashMap;
import java.util.Map;

public class Service {

	private Map<String, String> serviceProperties = new HashMap<>();
	
	public String get(String propertyName) {
		return serviceProperties.get(propertyName);
	}
	
	public void setProperty(String propertyName, String propertyValue) {
		this.serviceProperties.put(propertyName, propertyValue);
	}

}
