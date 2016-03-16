package com.orange.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.model.CredentialsMap;
import com.orange.model.PlanPropertyName;
import com.orange.model.PlansMap;
import com.orange.model.ServicePropertyName;
import com.orange.model.ServicesMap;

public class ParserSystemEnvironment extends ParserProperties {
	/**
	 * check whether mandatory property password are defined
	 * 
	 * @throws IllegalArgumentException
	 *             when find mandatory property not defined, error message
	 *             contains missing mandatory property name
	 */
	@Override
	public void checkPasswordDefined() throws IllegalArgumentException {
		checkMandatoryPropertiesDefined(Arrays.asList("SECURITY_PASSWORD"));
	}

	/**
	 * check whether service mandatory properties(id and description) are
	 * defined
	 * 
	 * @param serviceID
	 * @throws IllegalArgumentException
	 *             when find mandatory property not defined, error message
	 *             contains missing mandatory property name
	 */
	@Override
	public void checkServiceMandatoryPropertiesDefined(String serviceID) throws IllegalArgumentException {
		checkMandatoryPropertiesDefined(
				Arrays.asList("SERVICES_" + serviceID + "_NAME", "SERVICES_" + serviceID + "_DESCRIPTION"));
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
	private void checkMandatoryPropertiesDefined(List<String> mandatoryProperties) throws IllegalArgumentException {
		Map<String, String> env = System.getenv();
		for (String mandatoryProperty : mandatoryProperties) {
			if (env.get(mandatoryProperty) == null) {
				throw new IllegalArgumentException("Mandatory property: " + mandatoryProperty + " missing");
			}
		}
	}

	/**
	 * get the services properties values from system environment variables
	 * service property name pattern:
	 * SERVICES_{SERVICE_ID}_{servicePropertyName} {SERVICE_ID} should not
	 * contain "_PLAN_" or "_CREDENTIALS" ex. SERVICES_TRIPADVISOR_NAME
	 * 
	 * @return a map of service id (String) and service properties definitions
	 *         (Map<ServicePropertyName, String>)
	 */
	@Override
	public ServicesMap parseServicesProperties() {
		ServicesMap servicesMap = new ServicesMap();
		Map<String, String> env = System.getenv();
		for (Map.Entry<String, String> entry : env.entrySet()) {
			for (ServicePropertyName propertyName : ServicePropertyName.values()) {
				String serviceIDRegex = "((?!_PLAN_)(?!_CREDENTIALS).)+";
				String servicePropertyRegex = "^SERVICES_(?<serviceid>" + serviceIDRegex + ")_" + propertyName + "$";
				Pattern pattern = Pattern.compile(servicePropertyRegex);
				Matcher matcher = pattern.matcher(entry.getKey());
				if (matcher.find()) {
					String serviceID = matcher.group("serviceid");
					servicesMap.addServiceProperty(serviceID, propertyName, entry.getValue(), this);
					break;
				}
			}
		}
		servicesMap.checkServicesNameNotDuplicated();
		servicesMap.setServicesPropertiesDefaults();
		return servicesMap;
	}

	/**
	 * get the plans properties values from system environment variables plan
	 * property name pattern:
	 * SERVICES_{SERVICE_ID}_PLAN_{PLAN_ID}_{planPropertyName} {PLAN_ID} should
	 * not contain "_CREDENTIALS" ex. SERVICES_API_DIRECTORY_PLAN_1_NAME
	 * 
	 * @param serviceID
	 *            specify it will search the plans properties values for which
	 *            service
	 * @return a map of plan id (String) and plan properties definitions
	 *         (Map<PlanPropertyName, String>) for the specified serviceID
	 */
	@Override
	public PlansMap parsePlansProperties(String serviceID) {
		PlansMap plansMap = new PlansMap();
		Map<String, String> env = System.getenv();
		for (Map.Entry<String, String> entry : env.entrySet()) {
			for (PlanPropertyName propertyName : PlanPropertyName.values()) {
				String planIDRegex = "((?!_CREDENTIALS).)+";
				String planPropertyRegex = "^SERVICES_(" + serviceID + ")_PLAN_(?<planid>" + planIDRegex + ")_"
						+ propertyName + "$";
				Pattern pattern = Pattern.compile(planPropertyRegex);
				Matcher matcher = pattern.matcher(entry.getKey());
				if (matcher.find()) {
					String planID = matcher.group("planid");
					plansMap.addPlanProperty(planID, propertyName, entry.getValue());
					break;
				}
			}
		}
		plansMap.checkPlansNameNotDuplicated();
		plansMap.setPlansPropertiesDefaults();
		return plansMap;
	}

	/**
	 * get the services credential properties values from system environment
	 * variables - credential for whole service property name pattern:
	 * SERVICES_{SERVICE_ID}_CREDENTIALS or
	 * SERVICES_{SERVICE_ID}_CREDENTIALS_{credentialPropertyName} ex.
	 * SERVICES_TRIPADVISOR_CREDENTIALS, SERVICES_TRIPADVISOR_CREDENTIALS_URI -
	 * credential for specific plan property name pattern:
	 * SERVICES_{SERVICE_ID}_PLAN_{PLAN_ID}_CREDENTIALS or
	 * SERVICES_{SERVICE_ID}_PLAN_{PLAN_ID}_CREDENTIALS_{credentialPropertyName}
	 * ex. SERVICES_TRIPADVISOR_PLAN_1_CREDENTIALS,
	 * SERVICES_TRIPADVISOR_PLAN_1_CREDENTIALS_URI
	 * 
	 * @return a map of service id (String) and credentials (Map<String,
	 *         Object>)
	 */
	@Override
	public CredentialsMap parseCredentialsProperties() {
		CredentialsMap credentialsMap = new CredentialsMap();
		Map<String, String> env = System.getenv();
		for (Map.Entry<String, String> entry : env.entrySet()) {
			String key = entry.getKey();

			String serviceIDRegex = "((?!_PLAN_)(?!_CREDENTIALS).)+";
			String serviceCredentialJsonRegex = "^SERVICES_(?<serviceid>" + serviceIDRegex + ")_CREDENTIALS$";
			Pattern serviceCredentialJsonPattern = Pattern.compile(serviceCredentialJsonRegex);
			Matcher serviceCredentialJsonMatcher = serviceCredentialJsonPattern.matcher(key);
			if (serviceCredentialJsonMatcher.find()) {
				String serviceID = serviceCredentialJsonMatcher.group("serviceid");
				checkServiceMandatoryPropertiesDefined(serviceID);
				credentialsMap.addCredentials(serviceID, null, parseCredentialsJSON(entry.getValue()));
				continue;
			}

			String serviceCredentialPropertyRegex = "^SERVICES_(?<serviceid>" + serviceIDRegex
					+ ")_CREDENTIALS_(?<credentialProperty>.+)$";
			Pattern serviceCredentialPropertyPattern = Pattern.compile(serviceCredentialPropertyRegex);
			Matcher serviceCredentialPropertyMatcher = serviceCredentialPropertyPattern.matcher(key);
			if (serviceCredentialPropertyMatcher.find()) {
				String serviceID = serviceCredentialPropertyMatcher.group("serviceid");
				String credentialProperty = serviceCredentialPropertyMatcher.group("credentialProperty");
				checkServiceMandatoryPropertiesDefined(serviceID);
				credentialsMap.addCredential(serviceID, null, credentialProperty, entry.getValue());
				continue;
			}

			String planIDRegex = "((?!_CREDENTIALS).)+";
			String planCredentialJsonRegex = "^SERVICES_(?<serviceid>" + serviceIDRegex + ")_PLAN_(?<planid>"
					+ planIDRegex + ")_CREDENTIALS$";
			Pattern planCredentialJsonPattern = Pattern.compile(planCredentialJsonRegex);
			Matcher planCredentialJsonMatcher = planCredentialJsonPattern.matcher(key);
			if (planCredentialJsonMatcher.find()) {
				String serviceID = planCredentialJsonMatcher.group("serviceid");
				String planID = planCredentialJsonMatcher.group("planid");
				checkServiceMandatoryPropertiesDefined(serviceID);
				credentialsMap.addCredentials(serviceID, planID, parseCredentialsJSON(entry.getValue()));
				continue;
			}

			String planCredentialPropertyRegex = "^SERVICES_(?<serviceid>" + serviceIDRegex + ")_PLAN_(?<planid>"
					+ planIDRegex + ")_CREDENTIALS_(?<credentialProperty>.+)$";
			Pattern planCredentialPropertyPattern = Pattern.compile(planCredentialPropertyRegex);
			Matcher planCredentialPropertyMatcher = planCredentialPropertyPattern.matcher(key);
			if (planCredentialPropertyMatcher.find()) {
				String serviceID = planCredentialPropertyMatcher.group("serviceid");
				String planID = planCredentialPropertyMatcher.group("planid");
				String credentialProperty = planCredentialPropertyMatcher.group("credentialProperty");
				checkServiceMandatoryPropertiesDefined(serviceID);
				credentialsMap.addCredential(serviceID, planID, credentialProperty, entry.getValue());
				continue;
			}
		}
		return credentialsMap;
	}

	

	@Override
	public String getServiceName(String serviceID) {
		return System.getenv("SERVICES_" + serviceID + "_NAME");
	}

	@Override
	public String getPlanName(String serviceID, String planID) {
		return System.getenv("SERVICES_" + serviceID + "_PLAN_" + planID + "_NAME");
	}
}
