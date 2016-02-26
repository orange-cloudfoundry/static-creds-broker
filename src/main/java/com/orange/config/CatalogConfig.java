package com.orange.config;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.cloud.servicebroker.model.Catalog;
import org.springframework.cloud.servicebroker.model.Plan;
import org.springframework.cloud.servicebroker.model.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.model.PlanMetadata;
import com.orange.model.ServicePropertyName;
import com.orange.model.ServicesMap;
import com.orange.util.ParserSystemEnvironment;

@Configuration
public class CatalogConfig {
	private ServicesMap servicesMap = new ServicesMap();
	
	@Bean
	public Catalog catalog() {
		ParserSystemEnvironment.parseServicesProperties();
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
