package com.orange.config;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.servicebroker.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogConfig {
	@Value("#{ systemEnvironment['SERVICES_ID_NAME'] }")
	private String name;
	@Value("#{ systemEnvironment['SERVICES_ID_DESCRIPTION'] }")
	private String description = "";
	@Value("#{ systemEnvironment['SERVICES_ID_BINDEABLE'] ?: true}")
	private boolean bindable;
	@Value("#{ systemEnvironment['PLAN_NAME'] ?: 'default' }")
	private String plan_name;
	@Value("#{ systemEnvironment['PLAN_DESCRIPTION'] ?: 'Default plan' }")
	private String plan_description;
	@Value("#{ systemEnvironment['PLAN_FREE'] ?: true}")
	private boolean plan_free;
	@Value("#{ systemEnvironment['SERVICES_ID_TAGS'] }")
	private String tagstr;
	
	@Bean
	public Catalog catalog(){
		List<ServiceDefinition> serviceDefinitions = new ArrayList<ServiceDefinition>();
		String service_id = "000d5d66-e95b-4c19-beaf-064becbd3ada";
		Map<String, Object> service_metadata = new HashMap<String, Object>();
		
		String plan_id = "101d240e-c36f-46e8-b35f-97d2f69bd185";
		Map<String, Object> plan_metadata = new HashMap<String, Object>();
		List<String> tags = new ArrayList<String>();
		if (tagstr != null) {
			tags = Arrays.asList(tagstr.split(","));
		}
		
		Plan plan = new Plan(plan_id, plan_name, plan_description, plan_metadata, plan_free);
		ServiceDefinition service = new ServiceDefinition(service_id, name, description, bindable, false, Collections.singletonList(plan), tags, service_metadata, null, null);
		serviceDefinitions.add(service);
		Catalog catalog = new Catalog(serviceDefinitions);
		return catalog;
	}

}
