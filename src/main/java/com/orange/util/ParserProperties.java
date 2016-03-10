package com.orange.util;

import com.orange.model.CredentialsMap;
import com.orange.model.PlansMap;
import com.orange.model.ServicesMap;

public interface ParserProperties {
	/**
	 * check whether mandatory property password are defined
	 * 
	 * @throws IllegalArgumentException
	 *             when find mandatory property not defined, error message
	 *             contains missing mandatory property name
	 */
	public void checkPasswordDefined() throws IllegalArgumentException;

	/**
	 * check whether service mandatory properties(id and description) are
	 * defined
	 * 
	 * @param serviceID
	 * @throws IllegalArgumentException
	 *             when find mandatory property not defined, error message
	 *             contains missing mandatory property name
	 */
	public void checkServiceMandatoryPropertiesDefined(String serviceID) throws IllegalArgumentException;

	/**
	 * get the services properties values
	 * 
	 * @return a map of service id (String) and service properties definitions
	 *         (Map<ServicePropertyName, String>)
	 */
	public ServicesMap parseServicesProperties();

	/**
	 * get the plans properties values
	 * 
	 * @param serviceID
	 *            specify it will search the plans properties values for which
	 *            service
	 * @return a map of plan id (String) and plan properties definitions
	 *         (Map<PlanPropertyName, String>) for the specified serviceID
	 */
	public PlansMap parsePlansProperties(String serviceID);

	/**
	 * get the services credential properties values: credential may for whole
	 * service or for specific plan :
	 * 
	 * @return a map of servicePlanID (List<String>) and credentials
	 *         (Map<String, Object>)
	 */
	public CredentialsMap parseCredentialsProperties();

	public String getServiceName(String serviceID);

	public String getPlanName(String serviceID, String planID);
}
