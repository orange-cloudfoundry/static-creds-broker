package com.orange.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.model.*;

public abstract class ParserProperties {
	/**
	 * check whether mandatory property password are defined
	 * 
	 * @throws IllegalArgumentException
	 *             when find mandatory property not defined, error message
	 *             contains missing mandatory property name
	 */
	public abstract void checkPasswordDefined() throws IllegalArgumentException;

	/**
	 * check whether service mandatory properties(id and description) are
	 * defined
	 * 
	 * @param serviceID
	 * @throws IllegalArgumentException
	 *             when find mandatory property not defined, error message
	 *             contains missing mandatory property name
	 */
	public abstract void checkServiceMandatoryPropertiesDefined(String serviceID) throws IllegalArgumentException;

	/**
	 * get the services properties values
	 * 
	 * @return a map of service id (String) and service properties definitions
	 *         (Map<ServicePropertyName, String>)
	 */
	public abstract ServicesMap parseServicesProperties();

	/**
	 * get the plans properties values
	 * 
	 * @param serviceID
	 *            specify it will search the plans properties values for which
	 *            service
	 * @return a map of plan id (String) and plan properties definitions
	 *         (Map<PlanPropertyName, String>) for the specified serviceID
	 */
	public abstract PlansMap parsePlansProperties(String serviceID);

	/**
	 * get the services credential properties values: credential may for whole
	 * service or for specific plan :
	 * 
	 * @return a map of servicePlanID (List<String>) and credentials
	 *         (Map<String, Object>)
	 */
	public abstract ParsingCredentialsRepository parseCredentialsProperties();

	public abstract String getServiceName(String serviceID);

	public abstract String getPlanName(String serviceID, String planID);

	protected Credentials parseCredentialsJSON(String credentials_str) {
		Map<String, Object> credentials = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			credentials = mapper.readValue(credentials_str, new TypeReference<Map<String, String>>() {
			});
		} catch (IOException e) {
			throw new IllegalArgumentException("JSON parsing error: " + credentials_str);
		}
		Credentials res = new Credentials();
		credentials.entrySet().stream().forEach(e -> res.put(e.getKey(), e.getValue()));
		return res;
	}
	
	public void checkAtLeastOneServiceDefined(ServicesMap servicesMap) throws IllegalArgumentException{
		if (servicesMap.getAllServicesProperties().size() == 0) {
			throw new IllegalArgumentException("Not found any valid service defined.");
		}
	}
	
	public void checkAllServicesPlansHaveCredentialDefinition(ParsingCredentialsRepository credentialsRepository) throws IllegalArgumentException{
		//get name of all services defined
		ServicesMap servicesMap = this.parseServicesProperties();
		for (String serviceID : servicesMap.getServicesID()) {
			if (credentialsRepository.contains(serviceID, null)) {
				continue;
			}
			else {
				for (String planID : this.parsePlansProperties(serviceID).getIDs()) {
					if (!credentialsRepository.contains(serviceID, planID)) {
						throw new IllegalArgumentException("Not found any credential defined for plan " + planID + " of service " + serviceID);
					}
				}
			}
		}
	}
}
