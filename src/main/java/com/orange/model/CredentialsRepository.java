package com.orange.model;

import java.util.*;
import java.util.Map.Entry;

/**
 * map of servicePlan (String) and credentials (Map<String, Object>)
 * servicePlan is the combination identification of service and plan
 * identification could be "id" named in env. variables or name
 */
public class CredentialsRepository {

	private Map<ServicePlan, Credentials> credentialsMap = new HashMap<>();

	/**
	 *
	 * @param servicePlan
	 * @param credentialName
	 * @param credentialValue
	 */
	public void save(ServicePlan servicePlan, String credentialName, Object credentialValue) {
		Credentials credentialsToAdd = new Credentials();
		credentialsToAdd.put(credentialName, credentialValue);
		save(servicePlan, credentialsToAdd);
	}
	
	/**
	 * 
	 * @param servicePlan
	 * @param credentialsToAdd
	 */
	public void save(ServicePlan servicePlan, Credentials credentialsToAdd) {
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
	public Set<Entry<ServicePlan,Credentials>> findAll(){
		return credentialsMap.entrySet();
	}
	
	public boolean contains(String serviceID, String planID, String credentialName, Object credentialValue){
		ServicePlan servicePlan = new ServicePlanBuilder().withServiceID(serviceID).withPlanID(planID).build();
		Credentials credentials = credentialsMap.get(servicePlan);
		if (credentials != null && credentials.toMap().get(credentialName).equals(credentialValue)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean contains(String serviceID, String planID){
		ServicePlan servicePlan = new ServicePlanBuilder().withServiceID(serviceID).withPlanID(planID).build();
		return credentialsMap.containsKey(servicePlan);
	}

	public Credentials findByPlan(String planId) {
		for (Entry<ServicePlan,Credentials> entry : credentialsMap.entrySet()) {
			ServicePlan servicePlanName = entry.getKey();
			String plan_guid = servicePlanName.getPlanUid();
			if (plan_guid.equals(planId)) {
				return entry.getValue();
			}
		}
		return null;
	}

}
