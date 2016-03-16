package com.orange.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.orange.model.CredentialsMap;
import com.orange.model.PlanPropertyName;
import com.orange.model.PlansMap;
import com.orange.model.ServicePropertyName;
import com.orange.model.ServicesMap;

public class ParserApplicationPropertiesTest {
	private ParserApplicationProperties parserApplicationProperties;
	
	private static final String API_DIRECTORY_SERVICE_ID = "API_DIRECTORY";
	private static final String TRIPADVISOR_SERVICE_ID = "TRIPADVISOR";
	private static final String API_DIRECTORY_NAME = "API_DIRECTORY_test_Service";
	private static final String API_DIRECTORY_DESCRIPTION = "My existing API_DIRECTORY service";
	private static final String TRIPADVISOR_NAME = "TRIPADVISOR_test_Service";
	private static final String TRIPADVISOR_DESCRIPTION = "My existing TRIPADVISOR service";
	private static final String TRIPADVISOR_TAGS = "tag1,tag2,tag3";
	private static final String TRIPADVISOR_METADATA_DISPLAYNAME = "displayname for my service";
	private static final String TRIPADVISOR_METADATA_IMAGEURL = "https://upload.wikimedia.org/wikipedia/commons/c/c8/Orange_logo.svg";
	private static final String TRIPADVISOR_METADATA_SUPPORTURL = "https://github.com/tao-xinxiu/static-creds-broker";
	private static final String TRIPADVISOR_METADATA_DOCUMENTATIONURL = "https://github.com/tao-xinxiu/static-creds-broker";
	private static final String TRIPADVISOR_METADATA_PROVIDERDISPLAYNAME = "provider display name";
	private static final String TRIPADVISOR_METADATA_LONGDESCRIPTION = "A long description for my services";
	private static final String API_DIRECTORY_PLAN_PLAN1_ID = "PLAN1";
	private static final String API_DIRECTORY_PLAN_PLAN1_NAME = "dev";
	private static final String API_DIRECTORY_PLAN_PLAN1_DESCRIPTION = "The description of the plan dev";
	private static final String API_DIRECTORY_PLAN_PLAN2_ID = "PLAN2";
	private static final String API_DIRECTORY_PLAN_PLAN2_NAME = "preprod";
	private static final String API_DIRECTORY_PLAN_PLAN2_FREE = "false";
	private static final String API_DIRECTORY_PLAN_PLAN2_METADATA = "{\"bullets\":[\"20 GB of messages\",\"20 connections\"],\"costs\":[{\"amount\":{\"usd\":99.0,\"eur\":49.0},\"unit\":\"MONTHLY\"},{\"amount\":{\"usd\":0.99,\"eur\":0.49},\"unit\":\"1GB of messages over 20GB\"}],\"displayName\":\"Big Bunny\"}";
	private static final String API_DIRECTORY_PLAN_PLAN3_ID = "PLAN3";
	private static final String API_DIRECTORY_PLAN_PLAN3_NAME = "prod";
	private static final String API_DIRECTORY_SERVICE_CREDENTIALS_HOSTNAME = "http://company.com";
	private static final String API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_HOSTNAME = "http://dev.company.com";
	private static final String API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_URI = "http://mydev-api.org";
	private static final String API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_ACCESS_KEY = "devAZERT23456664DFDSFSDFDSF";
	private static final String API_DIRECTORY_PLAN_PLAN2_CREDENTIALS_HOSTNAME = "http://preprod.company.com";
	private static final String API_DIRECTORY_PLAN_PLAN2_CREDENTIALS_URI = "http://mypreprod-api.org";
	private static final String API_DIRECTORY_PLAN_PLAN2_CREDENTIALS_ACCESS_KEY = "preprodAZERT23456664DFDSFSDFDSF";
	private static final String API_DIRECTORY_PLAN_PLAN3_CREDENTIALS_HOSTNAME = "http://prod.company.com";
	private static final String API_DIRECTORY_PLAN_PLAN3_CREDENTIALS_URI = "http://myprod-api.org";
	private static final String API_DIRECTORY_PLAN_PLAN3_CREDENTIALS_ACCESS_KEY = "prodAZERT23456664DFDSFSDFDSF";
	private static final String TRIPADVISOR_SERVICE_CREDENTIALS_KEY1 = "username";
	private static final String TRIPADVISOR_SERVICE_CREDENTIALS_VALUE1 = "admin";
	private static final String TRIPADVISOR_SERVICE_CREDENTIALS_KEY2 = "password";
	private static final String TRIPADVISOR_SERVICE_CREDENTIALS_VALUE2 = "pa55woRD";
	private static final String TRIPADVISOR_SERVICE_CREDENTIALS = "{\""+TRIPADVISOR_SERVICE_CREDENTIALS_KEY1+"\":\""+TRIPADVISOR_SERVICE_CREDENTIALS_VALUE1+"\",\""+TRIPADVISOR_SERVICE_CREDENTIALS_KEY2+"\":\""+TRIPADVISOR_SERVICE_CREDENTIALS_VALUE2+"\"}";
	
			
	public ParserApplicationPropertiesTest() {
		parserApplicationProperties = new ParserApplicationProperties();
		parserApplicationProperties.setServices(getServicesProperty());
	}
	
	@Test
	public void should_get_services_map_with_info_have_been_set_in_property() {
		final ServicesMap servicesMap = parserApplicationProperties.parseServicesProperties();
		Assert.assertNotNull(servicesMap);
		Assert.assertEquals(2, servicesMap.geEntrySet().size());
		Assert.assertEquals(API_DIRECTORY_NAME, servicesMap.get(API_DIRECTORY_SERVICE_ID).get(ServicePropertyName.NAME));
		Assert.assertEquals(API_DIRECTORY_DESCRIPTION, servicesMap.get(API_DIRECTORY_SERVICE_ID).get(ServicePropertyName.DESCRIPTION));
		//TODO check default values of unset property
		Assert.assertEquals(TRIPADVISOR_NAME, servicesMap.get(TRIPADVISOR_SERVICE_ID).get(ServicePropertyName.NAME));
		Assert.assertEquals(TRIPADVISOR_DESCRIPTION, servicesMap.get(TRIPADVISOR_SERVICE_ID).get(ServicePropertyName.DESCRIPTION));
		Assert.assertEquals(TRIPADVISOR_TAGS, servicesMap.get(TRIPADVISOR_SERVICE_ID).get(ServicePropertyName.TAGS));
		Assert.assertEquals(TRIPADVISOR_METADATA_DISPLAYNAME, servicesMap.get(TRIPADVISOR_SERVICE_ID).get(ServicePropertyName.METADATA_DISPLAYNAME));
		Assert.assertEquals(TRIPADVISOR_METADATA_IMAGEURL, servicesMap.get(TRIPADVISOR_SERVICE_ID).get(ServicePropertyName.METADATA_IMAGEURL));
		Assert.assertEquals(TRIPADVISOR_METADATA_SUPPORTURL, servicesMap.get(TRIPADVISOR_SERVICE_ID).get(ServicePropertyName.METADATA_SUPPORTURL));
		Assert.assertEquals(TRIPADVISOR_METADATA_DOCUMENTATIONURL, servicesMap.get(TRIPADVISOR_SERVICE_ID).get(ServicePropertyName.METADATA_DOCUMENTATIONURL));
		Assert.assertEquals(TRIPADVISOR_METADATA_PROVIDERDISPLAYNAME, servicesMap.get(TRIPADVISOR_SERVICE_ID).get(ServicePropertyName.METADATA_PROVIDERDISPLAYNAME));
		Assert.assertEquals(TRIPADVISOR_METADATA_LONGDESCRIPTION, servicesMap.get(TRIPADVISOR_SERVICE_ID).get(ServicePropertyName.METADATA_LONGDESCRIPTION));
	}
	
	private Map<String, Object> getServicesProperty(){
		Map<String, Object> services = new HashMap<>();
		Map<String, Object> service_API_DIRECTORY = new HashMap<>();
		service_API_DIRECTORY.put("NAME", API_DIRECTORY_NAME);
		service_API_DIRECTORY.put("DESCRIPTION", API_DIRECTORY_DESCRIPTION);
		service_API_DIRECTORY.put("PLAN", getAPIDIRECTORYPlansProperty());
		service_API_DIRECTORY.put("CREDENTIALS", getAPIDIRECTORYServiceCredentialsProperty());
		services.put(API_DIRECTORY_SERVICE_ID, service_API_DIRECTORY);
		Map<String, Object> service_TRIPADVISOR = new HashMap<>();
		service_TRIPADVISOR.put("NAME", TRIPADVISOR_NAME);
		service_TRIPADVISOR.put("DESCRIPTION", TRIPADVISOR_DESCRIPTION);
		service_TRIPADVISOR.put("TAGS", TRIPADVISOR_TAGS);
		service_TRIPADVISOR.put("METADATA", getTRIPADVISORServiceMetadataProperty());
		service_TRIPADVISOR.put("CREDENTIALS", TRIPADVISOR_SERVICE_CREDENTIALS);
		services.put(TRIPADVISOR_SERVICE_ID, service_TRIPADVISOR);
		return services;
	}

	@Test
	public void shoud_get_plans_map_with_default_info_for_the_services_have_not_set_plan_info_in_property(){
		final PlansMap plansMap = parserApplicationProperties.parsePlansProperties(TRIPADVISOR_SERVICE_ID);
		Assert.assertNotNull(plansMap);
		Assert.assertEquals(1, plansMap.getIDs().size());
		Assert.assertNotNull(plansMap.get(PlansMap.defaultPlanID));
		Assert.assertEquals("default", plansMap.get(PlansMap.defaultPlanID).get(PlanPropertyName.NAME));
		Assert.assertEquals("Default plan", plansMap.get(PlansMap.defaultPlanID).get(PlanPropertyName.DESCRIPTION));
		Assert.assertEquals("true", plansMap.get(PlansMap.defaultPlanID).get(PlanPropertyName.FREE));
		Assert.assertEquals("{}", plansMap.get(PlansMap.defaultPlanID).get(PlanPropertyName.METADATA));
	}
	
	@Test
	public void shoud_get_plans_map_of_specific_service_which_have_been_set_in_property(){
		final PlansMap plansMap = parserApplicationProperties.parsePlansProperties(API_DIRECTORY_SERVICE_ID);
		Assert.assertNotNull(plansMap);
		Assert.assertEquals(3, plansMap.getIDs().size());
		Assert.assertNotNull(plansMap.get(API_DIRECTORY_PLAN_PLAN1_ID));
		Assert.assertEquals(API_DIRECTORY_PLAN_PLAN1_NAME, plansMap.get(API_DIRECTORY_PLAN_PLAN1_ID).get(PlanPropertyName.NAME));
		Assert.assertEquals(API_DIRECTORY_PLAN_PLAN1_DESCRIPTION, plansMap.get(API_DIRECTORY_PLAN_PLAN1_ID).get(PlanPropertyName.DESCRIPTION));
		Assert.assertEquals("true", plansMap.get(API_DIRECTORY_PLAN_PLAN1_ID).get(PlanPropertyName.FREE));
		Assert.assertEquals("{}", plansMap.get(API_DIRECTORY_PLAN_PLAN1_ID).get(PlanPropertyName.METADATA));
		Assert.assertNotNull(plansMap.get(API_DIRECTORY_PLAN_PLAN2_ID));
		Assert.assertEquals(API_DIRECTORY_PLAN_PLAN2_NAME, plansMap.get(API_DIRECTORY_PLAN_PLAN2_ID).get(PlanPropertyName.NAME));
		Assert.assertEquals("Default plan", plansMap.get(API_DIRECTORY_PLAN_PLAN2_ID).get(PlanPropertyName.DESCRIPTION));
		Assert.assertEquals(API_DIRECTORY_PLAN_PLAN2_FREE, plansMap.get(API_DIRECTORY_PLAN_PLAN2_ID).get(PlanPropertyName.FREE));
		Assert.assertEquals(API_DIRECTORY_PLAN_PLAN2_METADATA, plansMap.get(API_DIRECTORY_PLAN_PLAN2_ID).get(PlanPropertyName.METADATA));
		Assert.assertNotNull(plansMap.get(API_DIRECTORY_PLAN_PLAN3_ID));
		Assert.assertEquals(API_DIRECTORY_PLAN_PLAN3_NAME, plansMap.get(API_DIRECTORY_PLAN_PLAN3_ID).get(PlanPropertyName.NAME));
		Assert.assertEquals("Default plan", plansMap.get(API_DIRECTORY_PLAN_PLAN3_ID).get(PlanPropertyName.DESCRIPTION));
		Assert.assertEquals("true", plansMap.get(API_DIRECTORY_PLAN_PLAN3_ID).get(PlanPropertyName.FREE));
		Assert.assertEquals("{}", plansMap.get(API_DIRECTORY_PLAN_PLAN3_ID).get(PlanPropertyName.METADATA));
	}
	
	@Test
	public void should_get_credentials_map_which_have_been_set_in_property(){
		final CredentialsMap credentialsMap = parserApplicationProperties.parseCredentialsProperties();
		Assert.assertNotNull(credentialsMap);
		Assert.assertEquals(5, credentialsMap.getEntrySet().size());
		Assert.assertTrue(credentialsMap.contains(TRIPADVISOR_SERVICE_ID, null, TRIPADVISOR_SERVICE_CREDENTIALS_KEY1, TRIPADVISOR_SERVICE_CREDENTIALS_VALUE1));
		Assert.assertTrue(credentialsMap.contains(TRIPADVISOR_SERVICE_ID, null, TRIPADVISOR_SERVICE_CREDENTIALS_KEY2, TRIPADVISOR_SERVICE_CREDENTIALS_VALUE2));
		Assert.assertTrue(credentialsMap.contains(API_DIRECTORY_SERVICE_ID, null, "HOSTNAME", API_DIRECTORY_SERVICE_CREDENTIALS_HOSTNAME));
		Assert.assertTrue(credentialsMap.contains(API_DIRECTORY_SERVICE_ID, API_DIRECTORY_PLAN_PLAN1_ID, "ACCESS_KEY", API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_ACCESS_KEY));
		Assert.assertTrue(credentialsMap.contains(API_DIRECTORY_SERVICE_ID, API_DIRECTORY_PLAN_PLAN1_ID, "URI", API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_URI));
		Assert.assertTrue(credentialsMap.contains(API_DIRECTORY_SERVICE_ID, API_DIRECTORY_PLAN_PLAN2_ID, "ACCESS_KEY", API_DIRECTORY_PLAN_PLAN2_CREDENTIALS_ACCESS_KEY));
		Assert.assertTrue(credentialsMap.contains(API_DIRECTORY_SERVICE_ID, API_DIRECTORY_PLAN_PLAN2_ID, "URI", API_DIRECTORY_PLAN_PLAN2_CREDENTIALS_URI));
		Assert.assertTrue(credentialsMap.contains(API_DIRECTORY_SERVICE_ID, API_DIRECTORY_PLAN_PLAN3_ID, "ACCESS_KEY", API_DIRECTORY_PLAN_PLAN3_CREDENTIALS_ACCESS_KEY));
		Assert.assertTrue(credentialsMap.contains(API_DIRECTORY_SERVICE_ID, API_DIRECTORY_PLAN_PLAN3_ID, "URI", API_DIRECTORY_PLAN_PLAN3_CREDENTIALS_URI));
	}
	
	private Map<String, Object> getAPIDIRECTORYPlansProperty(){
		Map<String, Object> plansProperty = new HashMap<>();
		Map<String, Object> planOneProperty = new HashMap<>();
		planOneProperty.put("NAME", API_DIRECTORY_PLAN_PLAN1_NAME);
		planOneProperty.put("DESCRIPTION", API_DIRECTORY_PLAN_PLAN1_DESCRIPTION);
		planOneProperty.put("CREDENTIALS", getAPIDIRECTORYPlanOneCredentialsProperty());
		plansProperty.put(API_DIRECTORY_PLAN_PLAN1_ID, planOneProperty);
		Map<String, Object> planTwoProperty = new HashMap<>();
		planTwoProperty.put("NAME", API_DIRECTORY_PLAN_PLAN2_NAME);
		planTwoProperty.put("FREE", API_DIRECTORY_PLAN_PLAN2_FREE);
		planTwoProperty.put("METADATA", API_DIRECTORY_PLAN_PLAN2_METADATA);
		planTwoProperty.put("CREDENTIALS", getAPIDIRECTORYPlanTwoCredentialsProperty());
		plansProperty.put(API_DIRECTORY_PLAN_PLAN2_ID, planTwoProperty);
		Map<String, Object> planThreeProperty = new HashMap<>();
		planThreeProperty.put("NAME", API_DIRECTORY_PLAN_PLAN3_NAME);
		planThreeProperty.put("CREDENTIALS", getAPIDIRECTORYPlanThreeCredentialsProperty());
		plansProperty.put(API_DIRECTORY_PLAN_PLAN3_ID, planThreeProperty);
		return plansProperty;
	}
	
	private Map<String, Object> getTRIPADVISORServiceMetadataProperty(){
		Map<String, Object> service_TRIPADVISOR_METADATA = new HashMap<>();
		service_TRIPADVISOR_METADATA.put("DISPLAYNAME", TRIPADVISOR_METADATA_DISPLAYNAME);
		service_TRIPADVISOR_METADATA.put("IMAGEURL", TRIPADVISOR_METADATA_IMAGEURL);
		service_TRIPADVISOR_METADATA.put("SUPPORTURL", TRIPADVISOR_METADATA_SUPPORTURL);
		service_TRIPADVISOR_METADATA.put("DOCUMENTATIONURL", TRIPADVISOR_METADATA_DOCUMENTATIONURL);
		service_TRIPADVISOR_METADATA.put("PROVIDERDISPLAYNAME", TRIPADVISOR_METADATA_PROVIDERDISPLAYNAME);
		service_TRIPADVISOR_METADATA.put("LONGDESCRIPTION", TRIPADVISOR_METADATA_LONGDESCRIPTION);
		return service_TRIPADVISOR_METADATA;
	}
	
	private Map<String, Object> getAPIDIRECTORYServiceCredentialsProperty(){
		Map<String, Object> serviceCredentials = new HashMap<>();
		serviceCredentials.put("HOSTNAME", API_DIRECTORY_SERVICE_CREDENTIALS_HOSTNAME);
		return serviceCredentials;
	}
	
	private Map<String, Object> getAPIDIRECTORYPlanOneCredentialsProperty(){
		Map<String, Object> planOneCredentials = new HashMap<>();
		planOneCredentials.put("URI", API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_URI);
		planOneCredentials.put("ACCESS_KEY", API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_ACCESS_KEY);
		planOneCredentials.put("HOSTNAME", API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_HOSTNAME);
		return planOneCredentials;
	}
	
	private Map<String, Object> getAPIDIRECTORYPlanTwoCredentialsProperty(){
		Map<String, Object> planTwoCredentials = new HashMap<>();
		planTwoCredentials.put("URI", API_DIRECTORY_PLAN_PLAN2_CREDENTIALS_URI);
		planTwoCredentials.put("ACCESS_KEY", API_DIRECTORY_PLAN_PLAN2_CREDENTIALS_ACCESS_KEY);
		planTwoCredentials.put("HOSTNAME", API_DIRECTORY_PLAN_PLAN2_CREDENTIALS_HOSTNAME);
		return planTwoCredentials;
	}
	
	private Map<String, Object> getAPIDIRECTORYPlanThreeCredentialsProperty(){
		Map<String, Object> planThreeCredentials = new HashMap<>();
		planThreeCredentials.put("URI", API_DIRECTORY_PLAN_PLAN3_CREDENTIALS_URI);
		planThreeCredentials.put("ACCESS_KEY", API_DIRECTORY_PLAN_PLAN3_CREDENTIALS_ACCESS_KEY);
		planThreeCredentials.put("HOSTNAME", API_DIRECTORY_PLAN_PLAN3_CREDENTIALS_HOSTNAME);
		return planThreeCredentials;
	}
}
