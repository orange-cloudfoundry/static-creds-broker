package com.orange.util;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.model.CredentialsMap;
import com.orange.model.PlanPropertyName;
import com.orange.model.PlansMap;
import com.orange.model.ServicePropertyName;
import com.orange.model.ServicesMap;

public class ParserSystemEnvironment {
	/**
	 * Gets the value of the specified environment variable.
	 * 
	 * @param key
	 *            the name of the environment variable
	 * @return
	 */
	public static String get(String key) {
		return System.getenv(key);
	}

	/**
	 * check whether mandatory properties are defined in the system environment
	 * 
	 * @param mandatoryProperties
	 *            List<String> contains environment variables which should be
	 *            defined
	 * @throws IllegalArgumentException
	 *             when find mandatory property not defined in the system
	 *             environment , error message contains missing mandatory
	 *             property name
	 */
	public static void checkMandatoryPropertiesDefined(List<String> mandatoryProperties)
			throws IllegalArgumentException {
		Map<String, String> env = System.getenv();
		for (String mandatoryProperty : mandatoryProperties) {
			if (env.get(mandatoryProperty) == null) {
				throw new IllegalArgumentException("Mandatory property: " + mandatoryProperty + " missing");
			}
		}
	}

	/**
	 * get the services properties values from system environment variables
	 * service property name pattern: SERVICES_{serviceID}_{servicePropertyName}
	 * ex. SERVICES_TRIPADVISOR_NAME
	 * 
	 * @return a map of service id (String) and service properties definitions
	 *         (Map<ServicePropertyName, String>)
	 */
	public static ServicesMap parseServicesProperties() {
		ServicesMap servicesMap = new ServicesMap();
		Map<String, String> env = System.getenv();
		for (Map.Entry<String, String> entry : env.entrySet()) {
			String key = entry.getKey();
			// not targeted sys env key
			if (!key.startsWith("SERVICES_") || key.contains("_CREDENTIALS") || key.contains("_PLAN_"))
				continue;
			int indexNoPrefix = "SERVICES_".length();
			String noPrefix = key.substring(indexNoPrefix);
			Map.Entry<String, ServicePropertyName> serviceIDandPropertyName = splitServiceIDandPropertyName(noPrefix);
			if (serviceIDandPropertyName == null)
				continue;
			String serviceID = serviceIDandPropertyName.getKey();
			servicesMap.addServiceProperty(serviceID, serviceIDandPropertyName.getValue(), entry.getValue());
		}
		servicesMap.checkServicesNameNotDuplicated();
		servicesMap.setServicesPropertiesDefaults();
		return servicesMap;
	}

	/**
	 * Used to get the service id and service property name from a system env
	 * variable key (without part "SERVICES_")
	 * 
	 * @param noPrefix
	 *            The suffix(without part "SERVICES_") of a system env variable
	 *            key ex.TRIPADVISOR_NAME
	 * @return A Map.Entry with two elements: a service id and a service
	 *         property name ex. <"TRIPADVISOR", ServicePropertyName.NAME>
	 */
	private static Map.Entry<String, ServicePropertyName> splitServiceIDandPropertyName(String noPrefix) {
		for (ServicePropertyName propertyName : ServicePropertyName.values()) {
			String suffix = "_" + propertyName.toString();
			if (noPrefix.endsWith(suffix)) {
				String serviceID = noPrefix.substring(0, noPrefix.lastIndexOf(suffix));
				return new AbstractMap.SimpleEntry<String, ServicePropertyName>(serviceID, propertyName);
			}
		}
		return null;
	}

	/**
	 * get the plans properties values from system environment variables plan
	 * property name pattern:
	 * SERVICES_{SERVICE_ID}_PLAN_{PLAN_ID}_{planPropertyName} ex.
	 * SERVICES_API_DIRECTORY_PLAN_1_NAME
	 * 
	 * @return a map of plan id (String) and plan properties definitions
	 *         (Map<PlanPropertyName, String>)
	 */
	public static PlansMap parsePlansProperties(String serviceID) {
		PlansMap plansMap = new PlansMap();
		Map<String, String> env = System.getenv();
		for (Map.Entry<String, String> entry : env.entrySet()) {
			String key = entry.getKey();
			String prefix = "SERVICES_" + serviceID + "_PLAN_";
			if (!key.startsWith(prefix))
				continue;
			int indexNoPrefix = prefix.length();
			String noPrefix = key.substring(indexNoPrefix);
			Map.Entry<String, PlanPropertyName> planIDandPropertyName = splitPlanIDandPropertyName(noPrefix);
			if (planIDandPropertyName == null)
				continue;
			String planID = planIDandPropertyName.getKey();
			plansMap.addPlanProperty(planID, planIDandPropertyName.getValue(), entry.getValue());
		}
		plansMap.checkPlansNameNotDuplicated();
		plansMap.setPlansPropertiesDefaults();
		return plansMap;
	}

	private static Map.Entry<String, PlanPropertyName> splitPlanIDandPropertyName(String noPrefix) {
		for (PlanPropertyName propertyName : PlanPropertyName.values()) {
			String suffix = "_" + propertyName.toString();
			if (noPrefix.endsWith(suffix)) {
				String planID = noPrefix.substring(0, noPrefix.lastIndexOf(suffix));
				return new AbstractMap.SimpleEntry<String, PlanPropertyName>(planID, propertyName);
			}
		}
		return null;
	}

	/**
	 * get the services credential properties values from system environment variables
	 * - credential for whole service 
	 * 	 property name pattern: SERVICES_{serviceID}_CREDENTIALS 
	 *    						or SERVICES_{serviceID}_CREDENTIALS_{credentialPropertyName} 
	 * 	 ex. SERVICES_TRIPADVISOR_CREDENTIALS, SERVICES_TRIPADVISOR_CREDENTIALS_URI
	 * - credential for specific plan
	 *   property name pattern: SERVICES_{serviceID}_PLAN_{planID}_CREDENTIALS 
	 *   						or SERVICES_{serviceID}_PLAN_{planID}_CREDENTIALS_{credentialPropertyName} 
	 * 	 ex. SERVICES_TRIPADVISOR_PLAN_1_CREDENTIALS, SERVICES_TRIPADVISOR_PLAN_1_CREDENTIALS_URI
	 * @return a map of service id (String) and credentials (Map<String, Object>)
	 */
	public static CredentialsMap parseCredentialsProperties() {
		CredentialsMap credentialsMap = new CredentialsMap();
		Map<String, String> env = System.getenv();
		for (Map.Entry<String, String> entry : env.entrySet()) {
			String[] serviceIDplanIDandCredentialName = getServiceIDPlanIDandCredentialName(entry.getKey());
			if (serviceIDplanIDandCredentialName == null) {
				continue;
			}
			String serviceID = serviceIDplanIDandCredentialName[0];
			checkMandatoryPropertiesDefined(Arrays.asList("SERVICES_" + serviceID + "_NAME", "SERVICES_" + serviceID + "_DESCRIPTION"));
			// credentialName is null means credential value is a json hash string which contains multiple credentials
			if (serviceIDplanIDandCredentialName[2] == null) {
				credentialsMap.addCredentials(serviceID, serviceIDplanIDandCredentialName[1],
						parseCredentialsJSON(entry.getValue()));
			} else {
				credentialsMap.addCredential(serviceID, serviceIDplanIDandCredentialName[1], serviceIDplanIDandCredentialName[2],
						entry.getValue());
			}
		}
		return credentialsMap;
	}

	/**
	 * get the service id, plan id and service property name from a system env variable
	 * key which starts with "SERVICES_" and contains "_CREDENTIALS"
	 * 
	 * @param key
	 *        ex. SERVICES_TRIPADVISOR_CREDENTIALS_URI, SERVICES_TRIPADVISOR_CREDENTIALS,
	 *        	  SERVICES_TRIPADVISOR_PLAN_1_CREDENTIALS, SERVICES_TRIPADVISOR_PLAN_1_CREDENTIALS_URI
	 * @return returns a string array {serviceID, planID, credentialPropertyName}. 
	 * 			planID is null, if it's credentials for all plans of this service
	 *          credentialPropertyName is null, if it ends with "_CREDENTIALS"
	 */
	private static String[] getServiceIDPlanIDandCredentialName(String key) {
		if (!key.startsWith("SERVICES_") || !key.contains("_CREDENTIALS"))
			return null;
		int indexIDStart = "SERVICES_".length();
		int indexIDEnd = key.indexOf("_CREDENTIALS");
		String servicePlanID = key.substring(indexIDStart, indexIDEnd);
		String serviceID = null;
		String planID = null;
		if (servicePlanID.contains("_PLAN_")) { //ex. serviceID: "TRIPADVISOR_PLAN_1"
			int indexServiceIDEnd = servicePlanID.indexOf("_PLAN_");
			serviceID = servicePlanID.substring(0, indexServiceIDEnd);
			int indexPlanIDStart = serviceID.length() + "_PLAN_".length();
			planID = servicePlanID.substring(indexPlanIDStart);
		}
		else {
			serviceID = servicePlanID;
		}
		if (key.endsWith("_CREDENTIALS")) {
			return new String[] { serviceID, planID, null };
		} else if (key.contains("_CREDENTIALS_")) {
			int indexCredential = indexIDEnd + "_CREDENTIALS_".length();
			String credentialName = key.substring(indexCredential);
			return new String[] { serviceID, planID, credentialName };
		}
		return null;
	}

	private static Map<String, Object> parseCredentialsJSON(String credentials_str) {
		Map<String, Object> credentials = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			credentials = mapper.readValue(credentials_str, new TypeReference<Map<String, String>>() {
			});
		} catch (IOException e) {
			throw new IllegalArgumentException("JSON parsing error: " + credentials_str);
		}
		return credentials;
	}

	public static String getServiceName(String serviceID) {
		return System.getenv("SERVICES_" + serviceID + "_NAME");
	}

	public static String getPlanName(String serviceID, String planID) {
		return System.getenv("SERVICES_" + serviceID + "_PLAN_" + planID + "_NAME");
	}
}
