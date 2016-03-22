package com.orange.util;

import java.util.HashMap;
import java.util.Map;

public class ParserApplicationPropertiesTest extends ParserPropertiesTestBase<ParserApplicationProperties>{
	@Override
	protected ParserApplicationProperties createInstance() {
		ParserApplicationProperties parserApplicationProperties = new ParserApplicationProperties();
		parserApplicationProperties.setServices(getServicesProperty());
		return parserApplicationProperties;
	}
	
	@Override
	protected ParserApplicationProperties createInstanceWithNoService() {
		ParserApplicationProperties parserApplicationProperties = new ParserApplicationProperties();
		parserApplicationProperties.setServices(new HashMap<>());
		return parserApplicationProperties;
	}
	
	@Override
	protected ParserApplicationProperties createInstanceWithServiceNoCredential() {
		ParserApplicationProperties parserApplicationProperties = new ParserApplicationProperties();
		Map<String, Object> servicesProperty = getServicesProperty();
		((Map<?, ?>)(servicesProperty.get(TRIPADVISOR_SERVICE_ID))).remove("CREDENTIALS");
		parserApplicationProperties.setServices(servicesProperty);
		return parserApplicationProperties;
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
		service_TRIPADVISOR.put("CREDENTIALS", TRIPADVISOR_CREDENTIALS);
		services.put(TRIPADVISOR_SERVICE_ID, service_TRIPADVISOR);
		Map<String, Object> service_TEST_SERVICE = new HashMap<>();
		service_TEST_SERVICE.put("NAME", TEST_SERVICE_NAME);
		service_TEST_SERVICE.put("DESCRIPTION", TEST_SERVICE_DESCRIPTION);
		service_TEST_SERVICE.put("PLAN", getTESTSERVICEPlansProperty());
		service_TEST_SERVICE.put("CREDENTIALS", getTESTSERVICEServiceCredentialsProperty());
		services.put(TEST_SERVICE_SERVICE_ID, service_TEST_SERVICE);
		return services;
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
		serviceCredentials.put("HOSTNAME", API_DIRECTORY_CREDENTIALS_HOSTNAME);
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
	
	private Map<String, Object> getTESTSERVICEPlansProperty(){
		Map<String, Object> plansProperty = new HashMap<>();
		Map<String, Object> planOneProperty = new HashMap<>();
		planOneProperty.put("CREDENTIALS", getTESTSERVICEPlanOneCredentialsProperty());
		plansProperty.put(TEST_SERVICE_PLAN_PLAN_1_ID, planOneProperty);
		Map<String, Object> planTwoProperty = new HashMap<>();
		planTwoProperty.put("CREDENTIALS", getTESTSERVICEPlanTwoCredentialsProperty());
		plansProperty.put(TEST_SERVICE_PLAN_PLAN_2_ID, planTwoProperty);
		return plansProperty;
	}
	
	private Map<String, Object> getTESTSERVICEServiceCredentialsProperty(){
		Map<String, Object> serviceCredentials = new HashMap<>();
		serviceCredentials.put("URI", TEST_SERVICE_CREDENTIALS_URI);
		return serviceCredentials;
	}
	
	private Map<String, Object> getTESTSERVICEPlanOneCredentialsProperty(){
		Map<String, Object> planOneCredentials = new HashMap<>();
		planOneCredentials.put("URI", TEST_SERVICE_PLAN_PLAN_1_CREDENTIALS_URI);
		return planOneCredentials;
	}
	
	private Map<String, Object> getTESTSERVICEPlanTwoCredentialsProperty(){
		Map<String, Object> planTwoCredentials = new HashMap<>();
		planTwoCredentials.put("URI", TEST_SERVICE_PLAN_PLAN_2_CREDENTIALS_URI);
		return planTwoCredentials;
	}
}
