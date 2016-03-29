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
	private Map<ServicePlanID, Credentials> plansCredentialsMap = new HashMap<>();
	private Map<String, Credentials> servicesCredentialsMap = new HashMap<>();

	public void save(String serviceID, String planID, String credentialKey, Object credentialValue, ParserProperties parserProperties) {
		Credentials credentialsToAdd = new Credentials();
		credentialsToAdd.put(credentialKey, credentialValue);
		save(serviceID, planID, credentialsToAdd, parserProperties);
	}
	
	public void save(String serviceID, String planID, Credentials credentialsToAdd, ParserProperties parserProperties) {
		if (planID == null) {
			save(serviceID, credentialsToAdd, parserProperties);
			return;
		}
		parserProperties.checkServiceMandatoryPropertiesDefined(serviceID); 
		ServicePlanID servicePlan = new ServicePlanID(serviceID, planID);
		Credentials credentials = plansCredentialsMap.get(servicePlan);
		if (credentials == null) {
			credentials = new Credentials();
		}
		credentials.putAll(credentialsToAdd);
		plansCredentialsMap.put(servicePlan, credentials);
	}
	
	public void save(String serviceID, String credentialKey, Object credentialValue, ParserProperties parserProperties) {
		Credentials credentialsToAdd = new Credentials();
		credentialsToAdd.put(credentialKey, credentialValue);
		save(serviceID, credentialsToAdd, parserProperties);
	}
	
	public void save(String serviceID, Credentials credentialsToAdd, ParserProperties parserProperties) {
		parserProperties.checkServiceMandatoryPropertiesDefined(serviceID); 
		Credentials credentials = servicesCredentialsMap.get(serviceID);
		if (credentials == null) {
			credentials = new Credentials();
		}
		credentials.putAll(credentialsToAdd);
		servicesCredentialsMap.put(serviceID, credentials);
	}

	/**
	 * get all credentials defined for a plan
	 * @return
	 */
	public Set<Entry<ServicePlanID, Credentials>> findAllPlansCredentials() {
		return plansCredentialsMap.entrySet();
	}

	/**
	 * get all credentials defined for a service (i.e. for all plans of the service)
	 * @return
	 */
	public Set<Entry<String, Credentials>> findAllServicesCredentials() {
		return servicesCredentialsMap.entrySet();
	}
	
	public boolean contains(String serviceID, String planID, String credentialKey, Object credentialValue){
		if (planID == null) {
			return contains(serviceID, credentialKey, credentialValue);
		}
		ServicePlanID servicePlan = new ServicePlanBuilder().withServiceID(serviceID).withPlanID(planID).build();
		Credentials credentials = plansCredentialsMap.get(servicePlan);
		if (credentials != null && credentials.toMap().get(credentialKey).equals(credentialValue)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean contains(String serviceID, String planID){
		if (planID == null) {
			return contains(serviceID);
		}
		ServicePlanID servicePlan = new ServicePlanBuilder().withServiceID(serviceID).withPlanID(planID).build();
		return plansCredentialsMap.containsKey(servicePlan);
	}
	
	public boolean contains(String serviceID, String credentialKey, Object credentialValue){
		Credentials credentials = servicesCredentialsMap.get(serviceID);
		if (credentials != null && credentials.toMap().get(credentialKey).equals(credentialValue)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean contains(String serviceID){
		return servicesCredentialsMap.containsKey(serviceID);
	}
}
