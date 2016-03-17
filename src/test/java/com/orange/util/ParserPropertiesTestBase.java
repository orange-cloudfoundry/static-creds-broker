package com.orange.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.orange.model.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

public abstract class ParserPropertiesTestBase<T extends ParserProperties> {
	protected T parser;

	protected abstract T createInstance();

	@Before
	public void setUp() {
		parser = createInstance();
	}

	protected static final String API_DIRECTORY_SERVICE_ID = "API_DIRECTORY";
	protected static final String TRIPADVISOR_SERVICE_ID = "TRIPADVISOR";
	protected static final String API_DIRECTORY_NAME = "API_DIRECTORY_test_Service";
	protected static final String API_DIRECTORY_DESCRIPTION = "My existing API_DIRECTORY service";
	protected static final String TRIPADVISOR_NAME = "TRIPADVISOR_test_Service";
	protected static final String TRIPADVISOR_DESCRIPTION = "My existing TRIPADVISOR service";
	protected static final String TRIPADVISOR_TAGS = "tag1,tag2,tag3";
	protected static final String TRIPADVISOR_METADATA_DISPLAYNAME = "displayname for my service";
	protected static final String TRIPADVISOR_METADATA_IMAGEURL = "https://upload.wikimedia.org/wikipedia/commons/c/c8/Orange_logo.svg";
	protected static final String TRIPADVISOR_METADATA_SUPPORTURL = "https://github.com/tao-xinxiu/static-creds-broker";
	protected static final String TRIPADVISOR_METADATA_DOCUMENTATIONURL = "https://github.com/tao-xinxiu/static-creds-broker";
	protected static final String TRIPADVISOR_METADATA_PROVIDERDISPLAYNAME = "provider display name";
	protected static final String TRIPADVISOR_METADATA_LONGDESCRIPTION = "A long description for my services";
	protected static final String API_DIRECTORY_PLAN_PLAN1_ID = "PLAN1";
	protected static final String API_DIRECTORY_PLAN_PLAN1_NAME = "dev";
	protected static final String API_DIRECTORY_PLAN_PLAN1_DESCRIPTION = "The description of the plan dev";
	protected static final String API_DIRECTORY_PLAN_PLAN2_ID = "PLAN2";
	protected static final String API_DIRECTORY_PLAN_PLAN2_NAME = "preprod";
	protected static final String API_DIRECTORY_PLAN_PLAN2_FREE = "false";
	protected static final String API_DIRECTORY_PLAN_PLAN2_METADATA = "{\"bullets\":[\"20 GB of messages\",\"20 connections\"],\"costs\":[{\"amount\":{\"usd\":99.0,\"eur\":49.0},\"unit\":\"MONTHLY\"},{\"amount\":{\"usd\":0.99,\"eur\":0.49},\"unit\":\"1GB of messages over 20GB\"}],\"displayName\":\"Big Bunny\"}";
	protected static final String API_DIRECTORY_PLAN_PLAN3_ID = "PLAN3";
	protected static final String API_DIRECTORY_PLAN_PLAN3_NAME = "prod";
	protected static final String API_DIRECTORY_CREDENTIALS_HOSTNAME = "http://company.com";
	protected static final String API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_HOSTNAME = "http://dev.company.com";
	protected static final String API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_URI = "http://mydev-api.org";
	protected static final String API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_ACCESS_KEY = "devAZERT23456664DFDSFSDFDSF";
	protected static final String API_DIRECTORY_PLAN_PLAN2_CREDENTIALS_HOSTNAME = "http://preprod.company.com";
	protected static final String API_DIRECTORY_PLAN_PLAN2_CREDENTIALS_URI = "http://mypreprod-api.org";
	protected static final String API_DIRECTORY_PLAN_PLAN2_CREDENTIALS_ACCESS_KEY = "preprodAZERT23456664DFDSFSDFDSF";
	protected static final String API_DIRECTORY_PLAN_PLAN3_CREDENTIALS_HOSTNAME = "http://prod.company.com";
	protected static final String API_DIRECTORY_PLAN_PLAN3_CREDENTIALS_URI = "http://myprod-api.org";
	protected static final String API_DIRECTORY_PLAN_PLAN3_CREDENTIALS_ACCESS_KEY = "prodAZERT23456664DFDSFSDFDSF";
	protected static final String TRIPADVISOR_SERVICE_CREDENTIALS_KEY1 = "username";
	protected static final String TRIPADVISOR_SERVICE_CREDENTIALS_VALUE1 = "admin";
	protected static final String TRIPADVISOR_SERVICE_CREDENTIALS_KEY2 = "password";
	protected static final String TRIPADVISOR_SERVICE_CREDENTIALS_VALUE2 = "pa55woRD";
	protected static final String TRIPADVISOR_CREDENTIALS = "{\"" + TRIPADVISOR_SERVICE_CREDENTIALS_KEY1 + "\":\""
			+ TRIPADVISOR_SERVICE_CREDENTIALS_VALUE1 + "\",\"" + TRIPADVISOR_SERVICE_CREDENTIALS_KEY2 + "\":\""
			+ TRIPADVISOR_SERVICE_CREDENTIALS_VALUE2 + "\"}";
	protected static final String TEST_SERVICE_SERVICE_ID = "TEST_SERVICE";
	protected static final String TEST_SERVICE_NAME = "TEST_SERVICE";
	protected static final String TEST_SERVICE_DESCRIPTION = "TEST_SERVICE";
	protected static final String TEST_SERVICE_CREDENTIALS_URI = "http://mycompany.com";
	protected static final String TEST_SERVICE_PLAN_PLAN_1_ID = "PLAN_1";
	protected static final String TEST_SERVICE_PLAN_PLAN_1_CREDENTIALS_URI = "http://plan1.mycompany.com";
	protected static final String TEST_SERVICE_PLAN_PLAN_2_ID = "PLAN_2";
	protected static final String TEST_SERVICE_PLAN_PLAN_2_CREDENTIALS_URI = "http://plan2.mycompany.com";

	@Test
	public void should_get_services_map_with_info_have_been_set_in_property() {
		final ServicesMap servicesMap = parser.parseServicesProperties();
		Assert.assertNotNull(servicesMap);
		Assert.assertEquals(3, servicesMap.geEntrySet().size());
		Assert.assertEquals(API_DIRECTORY_NAME,
				servicesMap.get(API_DIRECTORY_SERVICE_ID).get(ServicePropertyName.NAME));
		Assert.assertEquals(API_DIRECTORY_DESCRIPTION,
				servicesMap.get(API_DIRECTORY_SERVICE_ID).get(ServicePropertyName.DESCRIPTION));
		// TODO check default values of unset property
		Assert.assertEquals(TRIPADVISOR_NAME, servicesMap.get(TRIPADVISOR_SERVICE_ID).get(ServicePropertyName.NAME));
		Assert.assertEquals(TRIPADVISOR_DESCRIPTION,
				servicesMap.get(TRIPADVISOR_SERVICE_ID).get(ServicePropertyName.DESCRIPTION));
		Assert.assertEquals(TRIPADVISOR_TAGS, servicesMap.get(TRIPADVISOR_SERVICE_ID).get(ServicePropertyName.TAGS));
		Assert.assertEquals(TRIPADVISOR_METADATA_DISPLAYNAME,
				servicesMap.get(TRIPADVISOR_SERVICE_ID).get(ServicePropertyName.METADATA_DISPLAYNAME));
		Assert.assertEquals(TRIPADVISOR_METADATA_IMAGEURL,
				servicesMap.get(TRIPADVISOR_SERVICE_ID).get(ServicePropertyName.METADATA_IMAGEURL));
		Assert.assertEquals(TRIPADVISOR_METADATA_SUPPORTURL,
				servicesMap.get(TRIPADVISOR_SERVICE_ID).get(ServicePropertyName.METADATA_SUPPORTURL));
		Assert.assertEquals(TRIPADVISOR_METADATA_DOCUMENTATIONURL,
				servicesMap.get(TRIPADVISOR_SERVICE_ID).get(ServicePropertyName.METADATA_DOCUMENTATIONURL));
		Assert.assertEquals(TRIPADVISOR_METADATA_PROVIDERDISPLAYNAME,
				servicesMap.get(TRIPADVISOR_SERVICE_ID).get(ServicePropertyName.METADATA_PROVIDERDISPLAYNAME));
		Assert.assertEquals(TRIPADVISOR_METADATA_LONGDESCRIPTION,
				servicesMap.get(TRIPADVISOR_SERVICE_ID).get(ServicePropertyName.METADATA_LONGDESCRIPTION));
		Assert.assertEquals(TEST_SERVICE_NAME, servicesMap.get(TEST_SERVICE_SERVICE_ID).get(ServicePropertyName.NAME));
		Assert.assertEquals(TEST_SERVICE_DESCRIPTION,
				servicesMap.get(TEST_SERVICE_SERVICE_ID).get(ServicePropertyName.DESCRIPTION));
	}

	@Test
	public void shoud_get_plans_map_with_default_info_for_the_services_have_not_set_plan_info_in_property() {
		final PlansMap plansMap = parser.parsePlansProperties(TRIPADVISOR_SERVICE_ID);
		Assert.assertNotNull(plansMap);
		Assert.assertEquals(1, plansMap.getIDs().size());
		Assert.assertNotNull(plansMap.get(PlansMap.defaultPlanID));
		Assert.assertEquals(PlansMap.defaultPlanName, plansMap.get(PlansMap.defaultPlanID).get(PlanPropertyName.NAME));
		Assert.assertEquals(PlansMap.defaultPlanDescription,
				plansMap.get(PlansMap.defaultPlanID).get(PlanPropertyName.DESCRIPTION));
		Assert.assertEquals(PlansMap.defaultFree, plansMap.get(PlansMap.defaultPlanID).get(PlanPropertyName.FREE));
		Assert.assertEquals(PlansMap.defaultMetadata,
				plansMap.get(PlansMap.defaultPlanID).get(PlanPropertyName.METADATA));
	}

	@Test
	public void shoud_get_plans_map_with_default_info_for_the_services_have_set_plan_with_not_complete_info_in_property() {
		final PlansMap plansMap = parser.parsePlansProperties(TEST_SERVICE_SERVICE_ID);
		Assert.assertNotNull(plansMap);
		Assert.assertEquals(2, plansMap.getIDs().size());
		Assert.assertNotNull(plansMap.get(TEST_SERVICE_PLAN_PLAN_1_ID));
		Assert.assertEquals(plansMap.getDefaultName(TEST_SERVICE_PLAN_PLAN_1_ID),
				plansMap.get(TEST_SERVICE_PLAN_PLAN_1_ID).get(PlanPropertyName.NAME));
		Assert.assertEquals(plansMap.getDefaultDescription(TEST_SERVICE_PLAN_PLAN_1_ID), plansMap.get(TEST_SERVICE_PLAN_PLAN_1_ID).get(PlanPropertyName.DESCRIPTION));
		Assert.assertEquals(PlansMap.defaultFree, plansMap.get(TEST_SERVICE_PLAN_PLAN_1_ID).get(PlanPropertyName.FREE));
		Assert.assertEquals(PlansMap.defaultMetadata,
				plansMap.get(TEST_SERVICE_PLAN_PLAN_1_ID).get(PlanPropertyName.METADATA));
		Assert.assertNotNull(plansMap.get(TEST_SERVICE_PLAN_PLAN_2_ID));
		Assert.assertEquals(plansMap.getDefaultName(TEST_SERVICE_PLAN_PLAN_2_ID),
				plansMap.get(TEST_SERVICE_PLAN_PLAN_2_ID).get(PlanPropertyName.NAME));
		Assert.assertEquals(plansMap.getDefaultDescription(TEST_SERVICE_PLAN_PLAN_2_ID), plansMap.get(TEST_SERVICE_PLAN_PLAN_2_ID).get(PlanPropertyName.DESCRIPTION));
		Assert.assertEquals(PlansMap.defaultFree, plansMap.get(TEST_SERVICE_PLAN_PLAN_2_ID).get(PlanPropertyName.FREE));
		Assert.assertEquals(PlansMap.defaultMetadata,
				plansMap.get(TEST_SERVICE_PLAN_PLAN_2_ID).get(PlanPropertyName.METADATA));
	}

	@Test
	public void shoud_get_plans_map_of_specific_service_which_have_been_set_in_property() {
		final PlansMap plansMap = parser.parsePlansProperties(API_DIRECTORY_SERVICE_ID);
		Assert.assertNotNull(plansMap);
		Assert.assertEquals(3, plansMap.getIDs().size());
		Assert.assertNotNull(plansMap.get(API_DIRECTORY_PLAN_PLAN1_ID));
		Assert.assertEquals(API_DIRECTORY_PLAN_PLAN1_NAME,
				plansMap.get(API_DIRECTORY_PLAN_PLAN1_ID).get(PlanPropertyName.NAME));
		Assert.assertEquals(API_DIRECTORY_PLAN_PLAN1_DESCRIPTION,
				plansMap.get(API_DIRECTORY_PLAN_PLAN1_ID).get(PlanPropertyName.DESCRIPTION));
		Assert.assertEquals(PlansMap.defaultFree, plansMap.get(API_DIRECTORY_PLAN_PLAN1_ID).get(PlanPropertyName.FREE));
		Assert.assertEquals(PlansMap.defaultMetadata,
				plansMap.get(API_DIRECTORY_PLAN_PLAN1_ID).get(PlanPropertyName.METADATA));
		Assert.assertNotNull(plansMap.get(API_DIRECTORY_PLAN_PLAN2_ID));
		Assert.assertEquals(API_DIRECTORY_PLAN_PLAN2_NAME,
				plansMap.get(API_DIRECTORY_PLAN_PLAN2_ID).get(PlanPropertyName.NAME));
		Assert.assertEquals(plansMap.getDefaultDescription(API_DIRECTORY_PLAN_PLAN2_ID), plansMap.get(API_DIRECTORY_PLAN_PLAN2_ID).get(PlanPropertyName.DESCRIPTION));
		Assert.assertEquals(API_DIRECTORY_PLAN_PLAN2_FREE,
				plansMap.get(API_DIRECTORY_PLAN_PLAN2_ID).get(PlanPropertyName.FREE));
		Assert.assertEquals(API_DIRECTORY_PLAN_PLAN2_METADATA,
				plansMap.get(API_DIRECTORY_PLAN_PLAN2_ID).get(PlanPropertyName.METADATA));
		Assert.assertNotNull(plansMap.get(API_DIRECTORY_PLAN_PLAN3_ID));
		Assert.assertEquals(API_DIRECTORY_PLAN_PLAN3_NAME,
				plansMap.get(API_DIRECTORY_PLAN_PLAN3_ID).get(PlanPropertyName.NAME));
		Assert.assertEquals(plansMap.getDefaultDescription(API_DIRECTORY_PLAN_PLAN3_ID), plansMap.get(API_DIRECTORY_PLAN_PLAN3_ID).get(PlanPropertyName.DESCRIPTION));
		Assert.assertEquals(PlansMap.defaultFree, plansMap.get(API_DIRECTORY_PLAN_PLAN3_ID).get(PlanPropertyName.FREE));
		Assert.assertEquals(PlansMap.defaultMetadata,
				plansMap.get(API_DIRECTORY_PLAN_PLAN3_ID).get(PlanPropertyName.METADATA));
	}

	@Test
	public void should_get_credentials_map_which_have_been_set_in_property() {
		final CredentialsRepository credentialsRepository = parser.parseCredentialsProperties();
		Assert.assertNotNull(credentialsRepository);
		Assert.assertEquals(8, credentialsRepository.findAll().size());
		Assert.assertTrue(credentialsRepository.contains(TRIPADVISOR_SERVICE_ID, null, TRIPADVISOR_SERVICE_CREDENTIALS_KEY1,
				TRIPADVISOR_SERVICE_CREDENTIALS_VALUE1));
		Assert.assertTrue(credentialsRepository.contains(TRIPADVISOR_SERVICE_ID, null, TRIPADVISOR_SERVICE_CREDENTIALS_KEY2,
				TRIPADVISOR_SERVICE_CREDENTIALS_VALUE2));
		Assert.assertTrue(credentialsRepository.contains(API_DIRECTORY_SERVICE_ID, null, "HOSTNAME",
				API_DIRECTORY_CREDENTIALS_HOSTNAME));
		Assert.assertTrue(credentialsRepository.contains(API_DIRECTORY_SERVICE_ID, API_DIRECTORY_PLAN_PLAN1_ID, "ACCESS_KEY",
				API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_ACCESS_KEY));
		Assert.assertTrue(credentialsRepository.contains(API_DIRECTORY_SERVICE_ID, API_DIRECTORY_PLAN_PLAN1_ID, "URI",
				API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_URI));
		Assert.assertTrue(credentialsRepository.contains(API_DIRECTORY_SERVICE_ID, API_DIRECTORY_PLAN_PLAN2_ID, "ACCESS_KEY",
				API_DIRECTORY_PLAN_PLAN2_CREDENTIALS_ACCESS_KEY));
		Assert.assertTrue(credentialsRepository.contains(API_DIRECTORY_SERVICE_ID, API_DIRECTORY_PLAN_PLAN2_ID, "URI",
				API_DIRECTORY_PLAN_PLAN2_CREDENTIALS_URI));
		Assert.assertTrue(credentialsRepository.contains(API_DIRECTORY_SERVICE_ID, API_DIRECTORY_PLAN_PLAN3_ID, "ACCESS_KEY",
				API_DIRECTORY_PLAN_PLAN3_CREDENTIALS_ACCESS_KEY));
		Assert.assertTrue(credentialsRepository.contains(API_DIRECTORY_SERVICE_ID, API_DIRECTORY_PLAN_PLAN3_ID, "URI",
				API_DIRECTORY_PLAN_PLAN3_CREDENTIALS_URI));
		Assert.assertTrue(credentialsRepository.contains(TEST_SERVICE_SERVICE_ID, null, "URI", TEST_SERVICE_CREDENTIALS_URI));
		Assert.assertTrue(credentialsRepository.contains(TEST_SERVICE_SERVICE_ID, TEST_SERVICE_PLAN_PLAN_1_ID, "URI",
				TEST_SERVICE_PLAN_PLAN_1_CREDENTIALS_URI));
		Assert.assertTrue(credentialsRepository.contains(TEST_SERVICE_SERVICE_ID, TEST_SERVICE_PLAN_PLAN_2_ID, "URI",
				TEST_SERVICE_PLAN_PLAN_2_CREDENTIALS_URI));
	}

	@Test
	public void should_parse_credentials_JSON() throws Exception {

		String json = " {\"username\":\"admin\",\"password\":\"pa55woRD\"}";

		Credentials credentials = parser.parseCredentialsJSON(json);

		assertThat(credentials.toMap()).hasSize(2).includes(entry("username", "admin"), entry("password", "pa55woRD"));


	}
}