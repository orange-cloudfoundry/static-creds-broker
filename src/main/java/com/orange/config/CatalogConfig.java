package com.orange.config;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.servicebroker.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.orange.model.PlanMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.PropertyUtils;
import java.lang.reflect.InvocationTargetException;
import java.io.IOException;

@Configuration
public class CatalogConfig {
	@Value("#{ systemEnvironment['SERVICES_ID_NAME'] }")
	private String name;
	@Value("#{ systemEnvironment['SERVICES_ID_DESCRIPTION'] }")
	private String description;
	@Value("#{ systemEnvironment['SERVICES_ID_BINDEABLE'] ?: true}")
	private boolean bindable;
	@Value("#{ systemEnvironment['SERVICES_ID_TAGS'] }")
	private String tagstr;

	@Value("#{ systemEnvironment['SERVICES_ID_METADATA_DISPLAYNAME'] ?: systemEnvironment['SERVICES_ID_NAME']}")
	private String meta_displayname;
	@Value("#{ systemEnvironment['SERVICES_ID_METADATA_IMAGEURL'] ?: ''}")
	private String meta_imageurl;
	@Value("#{ systemEnvironment['SERVICES_ID_METADATA_SUPPORTURL'] ?: ''}")
	private String meta_supporturl;
	@Value("#{ systemEnvironment['SERVICES_ID_METADATA_DOCUMENTATIONURL'] ?: ''}")
	private String meta_documentationurl;
	@Value("#{ systemEnvironment['SERVICES_ID_METADATA_PROVIDERDISPLAYNAME'] ?: ''}")
	private String meta_providerdisplayname;
	@Value("#{ systemEnvironment['SERVICES_ID_METADATA_LONGDESCRIPTION'] ?: ''}")
	private String meta_longdescription;

	@Value("#{ systemEnvironment['PLAN_NAME'] ?: 'default' }")
	private String plan_name;
	@Value("#{ systemEnvironment['PLAN_DESCRIPTION'] ?: 'Default plan' }")
	private String plan_description;
	@Value("#{ systemEnvironment['PLAN_FREE'] ?: true}")
	private boolean plan_free;
	@Value("#{ systemEnvironment['PLAN_METADATA'] ?: '{}'}")
	private String plan_metadata_str;

	@Bean
	public Catalog catalog(){
		List<ServiceDefinition> serviceDefinitions = new ArrayList<ServiceDefinition>();
		String service_id = UUID.nameUUIDFromBytes(name.getBytes()).toString(); // "000d5d66-e95b-4c19-beaf-064becbd3ada";
		Map<String, Object> service_metadata = getServiceMetadata();
		List<String> tags = new ArrayList<String>();
		if (tagstr != null) {
			tags = Arrays.asList(tagstr.split(","));
		}

		String plan_id = UUID.randomUUID().toString(); //"101d240e-c36f-46e8-b35f-97d2f69bd185";
		Map<String, Object> plan_metadata = getPlanMetadata();
		Plan plan = new Plan(plan_id, plan_name, plan_description, plan_metadata, plan_free);
		
		ServiceDefinition service = new ServiceDefinition(service_id, name, description, bindable, false, Collections.singletonList(plan), tags, service_metadata, null, null);
		serviceDefinitions.add(service);
		Catalog catalog = new Catalog(serviceDefinitions);
		return catalog;
	}
	
	private Map<String, Object> getServiceMetadata(){
		Map<String, Object> service_metadata = new HashMap<String, Object>();
		service_metadata.put("displayName", meta_displayname);
		service_metadata.put("imageUrl", meta_imageurl);
		service_metadata.put("longDescription", meta_longdescription);
		service_metadata.put("providerDisplayName", meta_providerdisplayname);
		service_metadata.put("documentationUrl", meta_documentationurl);
		service_metadata.put("supportUrl", meta_supporturl);
		return service_metadata;
	}
	
	private Map<String, Object> getPlanMetadata(){
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> plan_metadata = new HashMap<>();
		try {
			plan_metadata = PropertyUtils.describe(mapper.readValue(plan_metadata_str, PlanMetadata.class));
			plan_metadata.remove("class");
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | IOException e) {
			e.printStackTrace();
		}
		return plan_metadata;
	}
}
