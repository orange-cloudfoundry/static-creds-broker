package com.orange.config;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.cloud.servicebroker.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.orange.model.PlanMetadata;
import com.orange.model.ServicePropertyName;
import com.orange.model.ServicesMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.io.IOException;

@Configuration
public class CatalogConfig {
	private ServicesMap servicesMap = new ServicesMap();
	
	@Bean
	public Catalog catalog() {
		parseServicesProperties();
		servicesMap.setServicesPropertiesDefaults();
		servicesMap.checkServicesNameNotDuplicated();
		List<ServiceDefinition> serviceDefinitions = new ArrayList<ServiceDefinition>();
		for (Map<ServicePropertyName, String> service : servicesMap.getAllServicesProperties()) {
			String service_name = service.get(ServicePropertyName.NAME);
			String service_GUID = UUID.nameUUIDFromBytes(service_name.getBytes()).toString(); // generate service_guid from service_name which is also required to be unique across cf
			Map<String, Object> service_metadata = parseServiceMetadata(service);
			List<String> tags = new ArrayList<String>();
			if (service.get(ServicePropertyName.TAGS) != null) {
				tags = Arrays.asList(service.get(ServicePropertyName.TAGS).split(","));
			}
			String service_plan_name = service_name + " PLAN"; // + TODO change to + plan_name after support multiple plans.
			String plan_GUID = UUID.nameUUIDFromBytes(service_plan_name.getBytes()).toString(); // generate service_guid from service_name + plan_name
			Map<String, Object> plan_metadata = parsePlanMetadata(service.get(ServicePropertyName.PLAN_METADATA));
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
			servicesMap.addServiceProperty(serviceID, serviceIDandPropertyName.getValue(), entry.getValue());
		}
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


	private static Map<String, Object> parseServiceMetadata(Map<ServicePropertyName, String> service) {
		Map<String, Object> service_metadata = new HashMap<String, Object>();
		service_metadata.put("displayName", service.get(ServicePropertyName.METADATA_DISPLAYNAME));
		service_metadata.put("imageUrl", service.get(ServicePropertyName.METADATA_IMAGEURL));
		service_metadata.put("longDescription", service.get(ServicePropertyName.METADATA_LONGDESCRIPTION));
		service_metadata.put("providerDisplayName", service.get(ServicePropertyName.METADATA_PROVIDERDISPLAYNAME));
		service_metadata.put("documentationUrl", service.get(ServicePropertyName.METADATA_DOCUMENTATIONURL));
		service_metadata.put("supportUrl", service.get(ServicePropertyName.METADATA_SUPPORTURL));
		return service_metadata;
	}

	private static Map<String, Object> parsePlanMetadata(String planMetadataStr) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> plan_metadata = new HashMap<>();
		try {
			plan_metadata = PropertyUtils.describe(mapper.readValue(planMetadataStr, PlanMetadata.class));
			plan_metadata.remove("class");
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | IOException e) {
			e.printStackTrace();
		}
		return plan_metadata;
	}
	
}
