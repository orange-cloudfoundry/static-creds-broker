package com.orange.model;

import java.util.*;
import java.util.Map.Entry;

import com.orange.util.ParserProperties;

/**
 * map of servicePlan (String) and credentials (Map<String, Object>)
 * servicePlan is the combination identification of service and plan
 * identification could be "id" named in env. variables or name
 */
public class ParsingCredentialsRepository {

	private Map<ServicePlanID, Credentials> credentialsMap = new HashMap<>();

	/**
	 *
	 * @param servicePlan
	 * @param credentialKey
	 * @param credentialValue
	 */
	public void save(String serviceID, String planID, String credentialKey, Object credentialValue, ParserProperties parserProperties) {
		Credentials credentialsToAdd = new Credentials();
		credentialsToAdd.put(credentialKey, credentialValue);
		save(serviceID, planID, credentialsToAdd, parserProperties);
	}
	
	/**
	 * 
	 * @param servicePlan
	 * @param credentialsToAdd
	 */
	public void save(String serviceID, String planID, Credentials credentialsToAdd, ParserProperties parserProperties) {
		parserProperties.checkServiceMandatoryPropertiesDefined(serviceID); 
		ServicePlanID servicePlan = new ServicePlanID(serviceID, planID);
		Credentials credentials = credentialsMap.get(servicePlan);
		if (credentials == null) {
			credentials = new Credentials();
		}
		credentials.putAll(credentialsToAdd);
		credentialsMap.put(servicePlan, credentials);
	}

	/**
	 * get all keys which has credentials defined 
	 * @return
	 */
	public Set<Entry<ServicePlanID,Credentials>> findAll(){
		return credentialsMap.entrySet();
	}
	
	public boolean contains(String serviceID, String planID, String credentialKey, Object credentialValue){
		ServicePlanID servicePlan = new ServicePlanBuilder().withServiceID(serviceID).withPlanID(planID).build();
		Credentials credentials = credentialsMap.get(servicePlan);
		if (credentials != null && credentials.toMap().get(credentialKey).equals(credentialValue)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean contains(String serviceID, String planID){
		ServicePlanID servicePlan = new ServicePlanBuilder().withServiceID(serviceID).withPlanID(planID).build();
		return credentialsMap.containsKey(servicePlan);
	}
}
