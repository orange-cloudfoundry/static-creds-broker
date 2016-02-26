package com.orange.config;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.HashMap;
import java.util.HashSet;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.cloud.servicebroker.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.orange.Application;
import com.orange.model.PlanMetadata;
import com.orange.model.Service;
import com.orange.model.ServicePropertyName;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.io.IOException;

@Configuration
public class CatalogConfig {
	// map of service id and service
	private Map<String, Service> servicesMap = new HashMap<>();

	@Bean
	public Catalog catalog() {
		parseServicesProperties();
		setServicesPropertiesDefaults();
		checkServicesNameNotDuplicated();
		List<ServiceDefinition> serviceDefinitions = new ArrayList<ServiceDefinition>();
		for (Map.Entry<String, Service> servicesMapEntry : servicesMap.entrySet()) {
			String service_id = servicesMapEntry.getKey();
			Service service = servicesMapEntry.getValue();
			String service_GUID = UUID.nameUUIDFromBytes(service_id.getBytes()).toString(); // "000d5d66-e95b-4c19-beaf-064becbd3ada";
			Map<String, Object> service_metadata = getServiceMetadata(service);
			List<String> tags = new ArrayList<String>();
			if (service.get(ServicePropertyName.TAGS) != null) {
				tags = Arrays.asList(service.get(ServicePropertyName.TAGS).split(","));
			}
			String plan_id = service_id + " PLAN"; // + TODO change to + plan_id after support multiple plans.
			String plan_GUID = UUID.nameUUIDFromBytes(plan_id.getBytes()).toString(); // "101d240e-c36f-46e8-b35f-97d2f69bd185";
			Map<String, Object> plan_metadata = getPlanMetadata(service);
			Plan plan = new Plan(plan_GUID, service.get(ServicePropertyName.PLAN_NAME), service.get(ServicePropertyName.PLAN_DESCRIPTION), plan_metadata,
					Boolean.valueOf(service.get(ServicePropertyName.PLAN_FREE)));

			ServiceDefinition serviceDefinition = new ServiceDefinition(service_GUID, service.get(ServicePropertyName.NAME),
					service.get(ServicePropertyName.DESCRIPTION), Boolean.valueOf(service.get(ServicePropertyName.BINDEABLE)), false,
					Collections.singletonList(plan), tags, service_metadata, null, null);

			serviceDefinitions.add(serviceDefinition);
		}
		Catalog catalog = new Catalog(serviceDefinitions);
		return catalog;
	}

	/**
	 * get the services properties values from system environment variables
	 * service property name pattern: SERVICE_{serviceID}_{servicePropertyName}
	 */
	private void parseServicesProperties() {
		Map<String, String> env = System.getenv();
		for (Map.Entry<String, String> entry : env.entrySet()) {
			String key = entry.getKey();
			// no _ means it's not targeted sys env key
			if (!key.startsWith("SERVICES_") || key.contains("_CREDENTIALS"))
				continue;
			int indexNoPrefix = "SERVICES_".length();
			String noPrefix = key.substring(indexNoPrefix);
			Map.Entry<String, ServicePropertyName> serviceIDandPropertyName = splitServiceIDandPropertyName(noPrefix);
			if (serviceIDandPropertyName == null)
				continue;
			String serviceID = serviceIDandPropertyName.getKey();
			addServiceProperty(serviceID, serviceIDandPropertyName.getValue(), entry.getValue());
		}
	}

	/**
	 * add a service property, if the serviceID is not yet added to the servicesMap, its associated mandatory properties(service name and description) will be checked
	 * @param serviceID
	 * @param servicePropertyName
	 * @param servicePropertyValue
	 */
	private void addServiceProperty(String serviceID, ServicePropertyName servicePropertyName, String servicePropertyValue) {
		Service service = servicesMap.get(serviceID);
		if (service == null) {
			// when parsing a new id, check its mandatory properties
			List<String> mandatoryProperties = Arrays.asList("SERVICES_" + serviceID + "_NAME",
					"SERVICES_" + serviceID + "_DESCRIPTION");
			Application.checkMandatoryPropertiesDefined(mandatoryProperties);
			// if mandatory properties defined, add it into map
			service = new Service();
			servicesMap.put(serviceID, service);
		}
		service.setProperty(servicePropertyName, servicePropertyValue);
	}

	/**
	 * Used to get the service id and service property name from a system env variable key (without part "SERVICES_")
	 * @param noPrefix The suffix(without part "SERVICES_") of a system env variable key
	 * @return A string array with two elements: service id and service property name
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
	 * for the optional properties not defined in the system env variables, set its default values
	 */
	public void setServicesPropertiesDefaults() {
		for (Service service : servicesMap.values()) {
			for (ServicePropertyName propertyName : ServicePropertyName.values()) {
				if (service.get(propertyName) != null) {
					continue;
				}
				switch (propertyName) {
					case BINDEABLE:
					case PLAN_FREE:
						service.setProperty(propertyName, "true");
						break;
					case METADATA_DISPLAYNAME:
						service.setProperty(propertyName, service.get(ServicePropertyName.NAME));
						break;
					case METADATA_IMAGEURL:
					case METADATA_SUPPORTURL:
					case METADATA_DOCUMENTATIONURL:
					case METADATA_PROVIDERDISPLAYNAME:
					case METADATA_LONGDESCRIPTION:
						service.setProperty(propertyName, "");
						break;
					case PLAN_NAME:
						service.setProperty(propertyName, "default");
						break;
					case PLAN_DESCRIPTION:
						service.setProperty(propertyName, "Default plan");
						break;
					case PLAN_METADATA:
						service.setProperty(propertyName, "{}");
						break;
					default:
						break;
				}

			}
		}
	}

	private static Map<String, Object> getServiceMetadata(Service service) {
		Map<String, Object> service_metadata = new HashMap<String, Object>();
		service_metadata.put("displayName", service.get(ServicePropertyName.METADATA_DISPLAYNAME));
		service_metadata.put("imageUrl", service.get(ServicePropertyName.METADATA_IMAGEURL));
		service_metadata.put("longDescription", service.get(ServicePropertyName.METADATA_LONGDESCRIPTION));
		service_metadata.put("providerDisplayName", service.get(ServicePropertyName.METADATA_PROVIDERDISPLAYNAME));
		service_metadata.put("documentationUrl", service.get(ServicePropertyName.METADATA_DOCUMENTATIONURL));
		service_metadata.put("supportUrl", service.get(ServicePropertyName.METADATA_SUPPORTURL));
		return service_metadata;
	}

	private static Map<String, Object> getPlanMetadata(Service service) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> plan_metadata = new HashMap<>();
		try {
			plan_metadata = PropertyUtils.describe(mapper.readValue(service.get(ServicePropertyName.PLAN_METADATA), PlanMetadata.class));
			plan_metadata.remove("class");
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | IOException e) {
			e.printStackTrace();
		}
		return plan_metadata;
	}
	
	private void checkServicesNameNotDuplicated() throws IllegalArgumentException {
		Set<String> serviceNames = new HashSet<>();
		for (Service service : servicesMap.values()) {
			String name = service.get(ServicePropertyName.NAME);
			if (!serviceNames.add(name)) {
				throw new IllegalArgumentException("Duplicated service name is not allowed: " + name);
			}
		}
	}
}
