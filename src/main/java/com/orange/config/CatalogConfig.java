package com.orange.config;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.servicebroker.model.Catalog;
import org.springframework.cloud.servicebroker.model.Plan;
import org.springframework.cloud.servicebroker.model.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.model.PlanMetadata;
import com.orange.model.PlanPropertyName;
import com.orange.model.PlansMap;
import com.orange.model.ServicePropertyName;
import com.orange.model.ServicesMap;
import com.orange.util.Environment;
import com.orange.util.ParserApplicationProperties;
import com.orange.util.ParserProperties;
import com.orange.util.ParserSystemEnvironment;

@Configuration
public class CatalogConfig {
	private ServicesMap servicesMap = new ServicesMap();
	@Value("${enable:false}")
	private boolean useApplicationProperties;
	@Autowired
	private ParserApplicationProperties parserApplicationProperties;
	@Bean
	public Catalog catalog() {
		ParserProperties parserProperties = useApplicationProperties ? parserApplicationProperties : new ParserSystemEnvironment(new Environment());
		parserProperties.checkPasswordDefined();
		servicesMap = parserProperties.parseServicesProperties();
		parserProperties.checkAtLeastOneServiceDefined(servicesMap);
		List<ServiceDefinition> serviceDefinitions = new ArrayList<ServiceDefinition>();
		for (Map.Entry<String, Map<ServicePropertyName, String>> entry : servicesMap.geEntrySet()) {
			String serviceID = entry.getKey();
			Map<ServicePropertyName, String> service = entry.getValue();
			String service_name = service.get(ServicePropertyName.NAME);
			String service_GUID = UUID.nameUUIDFromBytes(service_name.getBytes()).toString(); // generate service_guid from service_name which is also required to be unique across cf
			Map<String, Object> service_metadata = parseServiceMetadata(service);
			List<String> tags = new ArrayList<String>();
			if (service.get(ServicePropertyName.TAGS) != null) {
				tags = Arrays.asList(service.get(ServicePropertyName.TAGS).split(","));
			}
			PlansMap plansMap = parserProperties.parsePlansProperties(serviceID);
			List<Plan> plans = new ArrayList<>();
			for (Map<PlanPropertyName, String> planProperties : plansMap.getAllPlansProperties()) {
				String plan_name = planProperties.get(PlanPropertyName.NAME);
				List<String> service_plan = Arrays.asList(service_name, plan_name);
				String plan_GUID = UUID.nameUUIDFromBytes(service_plan.toString().getBytes()).toString(); // generate service_guid from service_name + plan_name
				Map<String, Object> plan_metadata = parsePlanMetadata(planProperties.get(PlanPropertyName.METADATA));
				Plan plan = new Plan(plan_GUID, plan_name, planProperties.get(PlanPropertyName.DESCRIPTION), plan_metadata,
						Boolean.valueOf(planProperties.get(PlanPropertyName.FREE)));
				plans.add(plan);
			}
			ServiceDefinition serviceDefinition = new ServiceDefinition(service_GUID, service.get(ServicePropertyName.NAME),
					service.get(ServicePropertyName.DESCRIPTION), Boolean.valueOf(service.get(ServicePropertyName.BINDEABLE)), false,
					plans, tags, service_metadata, null, null);
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
