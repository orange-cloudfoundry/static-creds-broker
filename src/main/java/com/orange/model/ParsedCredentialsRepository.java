package com.orange.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ParsedCredentialsRepository {
	private Map<ServicePlanName, Credentials> credentialsMap = new HashMap<>();

	/**
	 *
	 * @param servicePlan
	 * @param credentialKey
	 * @param credentialValue
	 */
	public void save(String serviceName, String planName, String credentialKey, Object credentialValue) {
		Credentials credentialsToAdd = new Credentials();
		credentialsToAdd.put(credentialKey, credentialValue);
		save(serviceName, planName, credentialsToAdd);
	}
	
	/**
	 * 
	 * @param servicePlan
	 * @param credentialsToAdd
	 */
	public void save(String serviceName, String planName, Credentials credentialsToAdd) {
		ServicePlanName servicePlan = new ServicePlanName(serviceName, planName);
		Credentials credentials = credentialsMap.get(servicePlan);
		if (credentials == null) {
			credentials = new Credentials();
		}
		credentials.putAll(credentialsToAdd);
		credentialsMap.put(servicePlan, credentials);
	}

	public Credentials findByPlan(String planId) {
		for (Entry<ServicePlanName,Credentials> entry : credentialsMap.entrySet()) {
			ServicePlanName servicePlanName = entry.getKey();
			String plan_guid = servicePlanName.getPlanUid();
			if (plan_guid.equals(planId)) {
				return entry.getValue();
			}
		}
		return null;
	}
}
