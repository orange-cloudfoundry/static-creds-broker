package com.orange.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.orange.Application;

/**
 * map of service id (String) and credentials (Map<String, Object>)
 */
public class CredentialsMap {
	private Map<String, Map<String, Object>> credentialsMap = new HashMap<>();

	public void addCredential(String serviceID, String credentialName, Object credentialValue) {
		Map<String, Object> credentialsToAdd = new HashMap<>();
		credentialsToAdd.put(credentialName, credentialValue);
		addCredentials(serviceID, credentialsToAdd);
	}
	
	public void addCredentials(String serviceID, Map<String, Object> credentialsToAdd) {
		Map<String, Object> credentials = credentialsMap.get(serviceID);
		if (credentials == null) {
			Application.checkMandatoryPropertiesDefined(
					Arrays.asList("SERVICES_" + serviceID + "_NAME", "SERVICES_" + serviceID + "_DESCRIPTION"));
			credentials = new HashMap<>();
		}
		credentials.putAll(credentialsToAdd);
		credentialsMap.put(serviceID, credentials);
	}

	public Map<String, Object> getCredentials(String serviceID) {
		return credentialsMap.get(serviceID);
	}
	
	/**
	 * get all service ids which has credentials defined
	 * @return
	 */
	public Set<String> getServiceIds(){
		return credentialsMap.keySet();
	}
}
