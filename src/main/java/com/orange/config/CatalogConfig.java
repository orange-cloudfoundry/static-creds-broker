package com.orange.config;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.cloud.servicebroker.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.orange.Application;
import com.orange.model.PlanMetadata;
import com.orange.model.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.io.IOException;

@Configuration
public class CatalogConfig {
	// service properties names (which is also the suffix of the
	// service properties defined in the system environment variables)
	// NOTE: order of the list is important
	private static final List<String> propertiesNames = Arrays.asList("METADATA_DISPLAYNAME", "METADATA_IMAGEURL",
			"METADATA_SUPPORTURL", "METADATA_DOCUMENTATIONURL", "METADATA_PROVIDERDISPLAYNAME",
			"METADATA_LONGDESCRIPTION", "PLAN_NAME", "PLAN_DESCRIPTION", "PLAN_FREE", "PLAN_METADATA", "NAME",
			"DESCRIPTION", "BINDEABLE", "TAGS");

	// map of service id and service
	private Map<String, Service> servicesMap = new HashMap<>();

	@Bean
	public Catalog catalog() {
		parseServicesProperties();
		setServicesPropertiesDefaults();
		checkServicesNameNotDuplicated();
		List<ServiceDefinition> serviceDefinitions = new ArrayList<ServiceDefinition>();
		for (Service service : servicesMap.values()) {
			String service_GUID = UUID.nameUUIDFromBytes(service.get("NAME").getBytes()).toString(); // "000d5d66-e95b-4c19-beaf-064becbd3ada";
			Map<String, Object> service_metadata = getServiceMetadata(service);
			List<String> tags = new ArrayList<String>();
			if (service.get("TAGS") != null) {
				tags = Arrays.asList(service.get("TAGS").split(","));
			}
			String plan_id = UUID.randomUUID().toString(); // "101d240e-c36f-46e8-b35f-97d2f69bd185";
			Map<String, Object> plan_metadata = getPlanMetadata(service);
			Plan plan = new Plan(plan_id, service.get("PLAN_NAME"), service.get("PLAN_DESCRIPTION"), plan_metadata,
					Boolean.valueOf(service.get("PLAN_FREE")));

			ServiceDefinition serviceDefinition = new ServiceDefinition(service_GUID, service.get("NAME"),
					service.get("DESCRIPTION"), Boolean.valueOf(service.get("BINDEABLE")), false,
					Collections.singletonList(plan), tags, service_metadata, null, null);

			serviceDefinitions.add(serviceDefinition);
		}
		Catalog catalog = new Catalog(serviceDefinitions);
		return catalog;
	}

	/**
	 * get the services properties values from system environment variables
	 * service property name pattern: SERVICE_{serviceID}_{suffix}
	 */
	private void parseServicesProperties() {
		Map<String, String> env = System.getenv();
		for (Map.Entry<String, String> entry : env.entrySet()) {
			String key = entry.getKey();
			// no _ means it's not targeted sys env key
			if (!key.startsWith("SERVICES_"))
				continue;
			int indexNoPrefix = "SERVICES_".length();
			String noPrefix = key.substring(indexNoPrefix);
			String[] serviceIDandPropertyName = splitServiceIDandPropertyName(noPrefix);
			if (serviceIDandPropertyName == null)
				continue;
			String serviceID = serviceIDandPropertyName[0];
			addServiceProperty(serviceID, serviceIDandPropertyName[1], entry.getValue());
		}
	}

	private void addServiceProperty(String serviceID, String servicePropertyName, String servicePropertyValue) {
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

	private static String[] splitServiceIDandPropertyName(String noPrefix) {
		for (String propertiesName : propertiesNames) {
			String suffix = "_" + propertiesName;
			if (noPrefix.endsWith(suffix)) {
				String serviceID = noPrefix.substring(0, noPrefix.lastIndexOf(suffix));
				return new String[] { serviceID, propertiesName };
			}
		}
		return null;
	}

	public void setServicesPropertiesDefaults() {
		for (Service service : servicesMap.values()) {
			for (String propertyName : propertiesNames) {
				if (service.get(propertyName) != null) {
					continue;
				}
				switch (propertyName) {
					case "BINDEABLE":
					case "PLAN_FREE":
						service.setProperty(propertyName, "true");
						break;
					case "METADATA_DISPLAYNAME":
						service.setProperty(propertyName, service.get("NAME"));
						break;
					case "METADATA_IMAGEURL":
					case "METADATA_SUPPORTURL":
					case "METADATA_DOCUMENTATIONURL":
					case "METADATA_PROVIDERDISPLAYNAME":
					case "METADATA_LONGDESCRIPTION":
						service.setProperty(propertyName, "");
						break;
					case "PLAN_NAME":
						service.setProperty(propertyName, "default");
						break;
					case "PLAN_DESCRIPTION":
						service.setProperty(propertyName, "Default plan");
						break;
					case "PLAN_METADATA":
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
		service_metadata.put("displayName", service.get("METADATA_DISPLAYNAME"));
		service_metadata.put("imageUrl", service.get("METADATA_IMAGEURL"));
		service_metadata.put("longDescription", service.get("METADATA_LONGDESCRIPTION"));
		service_metadata.put("providerDisplayName", service.get("METADATA_PROVIDERDISPLAYNAME"));
		service_metadata.put("documentationUrl", service.get("METADATA_DOCUMENTATIONURL"));
		service_metadata.put("supportUrl", service.get("METADATA_SUPPORTURL"));
		return service_metadata;
	}

	private static Map<String, Object> getPlanMetadata(Service service) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> plan_metadata = new HashMap<>();
		try {
			plan_metadata = PropertyUtils.describe(mapper.readValue(service.get("PLAN_METADATA"), PlanMetadata.class));
			plan_metadata.remove("class");
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | IOException e) {
			e.printStackTrace();
		}
		return plan_metadata;
	}
	
	private void checkServicesNameNotDuplicated() throws IllegalArgumentException {
		Set<String> serviceNames = new HashSet<>();
		for (Service service : servicesMap.values()) {
			String name = service.get("NAME");
			if (!serviceNames.add(name)) {
				throw new IllegalArgumentException("Duplicated service name is not allowed: " + name);
			}
		}
	}
}
