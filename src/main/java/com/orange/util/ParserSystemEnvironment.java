package com.orange.util;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.orange.model.ServicesMap;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.model.CredentialsMap;
import com.orange.model.ServicePropertyName;

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
			if (!key.startsWith("SERVICES_") || key.contains("_CREDENTIALS"))
				continue;
			int indexNoPrefix = "SERVICES_".length();
			String noPrefix = key.substring(indexNoPrefix);
			Map.Entry<String, ServicePropertyName> serviceIDandPropertyName = splitServiceIDandPropertyName(noPrefix);
			if (serviceIDandPropertyName == null)
				continue;
			String serviceID = serviceIDandPropertyName.getKey();
			servicesMap.addServiceProperty(serviceID, serviceIDandPropertyName.getValue(), entry.getValue());
		}
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
	 * get the services credential properties values from system environment
	 * variables service credential property name pattern:
	 * SERVICES_{serviceID}_CREDENTIALS or
	 * SERVICES_{serviceID}_CREDENTIALS_{credentialPropertyName} ex.
	 * SERVICES_TRIPADVISOR_CREDENTIALS_URI, SERVICES_TRIPADVISOR_CREDENTIALS
	 * 
	 * @return a map of service id (String) and credentials (Map<String,
	 *         Object>)
	 */
	public static CredentialsMap parseCredentialsProperties() {
		CredentialsMap credentialsMap = new CredentialsMap();
		Map<String, String> env = System.getenv();
		for (Map.Entry<String, String> entry : env.entrySet()) {
			String[] serviceIDandCredentialName = getServiceIDandCredentialName(entry.getKey());
			if (serviceIDandCredentialName == null) {
				continue;
			}
			// no credential name means its value is a json hash string which
			// contains multiple credentials
			if (serviceIDandCredentialName.length == 1) {
				credentialsMap.addCredentials(serviceIDandCredentialName[0], parseCredentialsJSON(entry.getValue()));
			} else {
				credentialsMap.addCredential(serviceIDandCredentialName[0], serviceIDandCredentialName[1],
						entry.getValue());
			}
		}
		return credentialsMap;
	}

	/**
	 * get the service id and service property name from a system env variable
	 * key which starts with "SERVICES_" and contains "_CREDENTIALS"
	 * 
	 * @param key ex. SERVICES_TRIPADVISOR_CREDENTIALS_URI, SERVICES_TRIPADVISOR_CREDENTIALS
	 * @return for format SERVICES_{serviceID}_CREDENTIALS_{credentialPropertyName}, returns a string array with two 
	 * 			elements: a service id and a service credential property name ex. {"TRIPADVISOR", "URI"}.
	 * 			for format SERVICES_{serviceID}_CREDENTIALS, returns a string array with single element: service id. ex {"TRIPADVISOR"}
	 */
	private static String[] getServiceIDandCredentialName(String key) {
		if (!key.startsWith("SERVICES_") || !key.contains("_CREDENTIALS"))
			return null;
		int indexIDStart = "SERVICES_".length();
		int indexIDEnd = key.indexOf("_CREDENTIALS");
		String serviceID = key.substring(indexIDStart, indexIDEnd);
		if (key.endsWith("_CREDENTIALS")) {
			return new String[] { serviceID };
		} else if (key.contains("_CREDENTIALS_")) {
			int indexCredential = indexIDEnd + "_CREDENTIALS_".length();
			String credentialName = key.substring(indexCredential);
			return new String[] { serviceID, credentialName };
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
}
