package com.orange.util;

import java.util.HashMap;
import java.util.Map;

public class ParserSystemEnvironmentTest extends ParserPropertiesTestBase<ParserSystemEnvironment>{
	@Override
	protected ParserSystemEnvironment createInstance() {
		return new ParserSystemEnvironment(new MockEnvironment());
	}
	
	@Override
	protected ParserSystemEnvironment createInstanceWithNoService() {
		return new ParserSystemEnvironment(new MockEnvironment(new HashMap<>()));
	}
	
	@Override
	protected ParserSystemEnvironment createInstanceWithServiceNoCredential() {
		Map<String, String> env = getTestEnvironment();
		env.remove("SERVICES_TRIPADVISOR_CREDENTIALS");
		return new ParserSystemEnvironment(new MockEnvironment(env));
	}
	
	@Override
	protected ParserSystemEnvironment createInstanceWithOnePlanNoCredential() {
		Map<String, String> env = getTestEnvironment();
		env.remove("SERVICES_API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_HOSTNAME");
		env.remove("SERVICES_API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_URI");
		env.remove("SERVICES_API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_ACCESS_KEY");
		env.remove("SERVICES_API_DIRECTORY_CREDENTIALS_HOSTNAME");
		return new ParserSystemEnvironment(new MockEnvironment(env));
	}
	
	@Override
	protected ParserSystemEnvironment createInstanceWithServiceNoName() {
		Map<String, String> env = getTestEnvironment();
		env.remove("SERVICES_DUMMY_NAME");
		return new ParserSystemEnvironment(new MockEnvironment(env));
	}
	
	@Override
	protected ParserSystemEnvironment createInstanceWithServiceNoDescription() {
		Map<String, String> env = getTestEnvironment();
		env.remove("SERVICES_DUMMY_DESCRIPTION");
		return new ParserSystemEnvironment(new MockEnvironment(env));
	}
	
	@Override
	protected ParserSystemEnvironment createInstanceWithServiceNoNameAndDescription() {
		Map<String, String> env = getTestEnvironment();
		env.remove("SERVICES_DUMMY_NAME");
		env.remove("SERVICES_DUMMY_DESCRIPTION");
		return new ParserSystemEnvironment(new MockEnvironment(env));
	}
	
	class MockEnvironment extends Environment {
		private Map<String, String> env;

		MockEnvironment(){
			this.env = getTestEnvironment();
		}

		MockEnvironment(Map<String, String> env) {
			this.env = env;
		}

		@Override
		public Map<String, String> get() {
			return this.env;
		}
	}
	
	private Map<String, String> getTestEnvironment(){
		Map<String, String> env = new HashMap<>();
		env.put("SERVICES_API_DIRECTORY_NAME", API_DIRECTORY_NAME);
		env.put("SERVICES_API_DIRECTORY_DESCRIPTION", API_DIRECTORY_DESCRIPTION);
		env.put("SERVICES_TRIPADVISOR_NAME", TRIPADVISOR_NAME);
		env.put("SERVICES_TRIPADVISOR_DESCRIPTION", TRIPADVISOR_DESCRIPTION);
		env.put("SERVICES_TRIPADVISOR_TAGS", TRIPADVISOR_TAGS);
		env.put("SERVICES_TRIPADVISOR_METADATA_DISPLAYNAME", TRIPADVISOR_METADATA_DISPLAYNAME);
		env.put("SERVICES_TRIPADVISOR_METADATA_IMAGEURL", TRIPADVISOR_METADATA_IMAGEURL);
		env.put("SERVICES_TRIPADVISOR_METADATA_SUPPORTURL", TRIPADVISOR_METADATA_SUPPORTURL);
		env.put("SERVICES_TRIPADVISOR_METADATA_DOCUMENTATIONURL", TRIPADVISOR_METADATA_DOCUMENTATIONURL);
		env.put("SERVICES_TRIPADVISOR_METADATA_PROVIDERDISPLAYNAME", TRIPADVISOR_METADATA_PROVIDERDISPLAYNAME);
		env.put("SERVICES_TRIPADVISOR_METADATA_LONGDESCRIPTION", TRIPADVISOR_METADATA_LONGDESCRIPTION);
		env.put("SERVICES_TRIPADVISOR_CREDENTIALS", TRIPADVISOR_CREDENTIALS_JSON);
		env.put("SERVICES_API_DIRECTORY_PLAN_PLAN1_NAME", API_DIRECTORY_PLAN_PLAN1_NAME);
		env.put("SERVICES_API_DIRECTORY_PLAN_PLAN1_DESCRIPTION", API_DIRECTORY_PLAN_PLAN1_DESCRIPTION);
		env.put("SERVICES_API_DIRECTORY_PLAN_PLAN2_NAME", API_DIRECTORY_PLAN_PLAN2_NAME);
		env.put("SERVICES_API_DIRECTORY_PLAN_PLAN2_FREE", API_DIRECTORY_PLAN_PLAN2_FREE);
		env.put("SERVICES_API_DIRECTORY_PLAN_PLAN2_METADATA", API_DIRECTORY_PLAN_PLAN2_METADATA);
		env.put("SERVICES_API_DIRECTORY_PLAN_PLAN3_NAME", API_DIRECTORY_PLAN_PLAN3_NAME);
		env.put("SERVICES_API_DIRECTORY_CREDENTIALS_HOSTNAME", API_DIRECTORY_CREDENTIALS_HOSTNAME);
		env.put("SERVICES_API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_HOSTNAME",
				API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_HOSTNAME);
		env.put("SERVICES_API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_URI", API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_URI);
		env.put("SERVICES_API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_ACCESS_KEY",
				API_DIRECTORY_PLAN_PLAN1_CREDENTIALS_ACCESS_KEY);
		env.put("SERVICES_API_DIRECTORY_PLAN_PLAN2_CREDENTIALS_HOSTNAME",
				API_DIRECTORY_PLAN_PLAN2_CREDENTIALS_HOSTNAME);
		env.put("SERVICES_API_DIRECTORY_PLAN_PLAN2_CREDENTIALS_URI", API_DIRECTORY_PLAN_PLAN2_CREDENTIALS_URI);
		env.put("SERVICES_API_DIRECTORY_PLAN_PLAN2_CREDENTIALS_ACCESS_KEY",
				API_DIRECTORY_PLAN_PLAN2_CREDENTIALS_ACCESS_KEY);
		env.put("SERVICES_API_DIRECTORY_PLAN_PLAN3_CREDENTIALS_HOSTNAME",
				API_DIRECTORY_PLAN_PLAN3_CREDENTIALS_HOSTNAME);
		env.put("SERVICES_API_DIRECTORY_PLAN_PLAN3_CREDENTIALS_URI", API_DIRECTORY_PLAN_PLAN3_CREDENTIALS_URI);
		env.put("SERVICES_API_DIRECTORY_PLAN_PLAN3_CREDENTIALS_ACCESS_KEY",
				API_DIRECTORY_PLAN_PLAN3_CREDENTIALS_ACCESS_KEY);
		env.put("SERVICES_TEST_SERVICE_NAME", TEST_SERVICE_NAME);
		env.put("SERVICES_TEST_SERVICE_DESCRIPTION", TEST_SERVICE_DESCRIPTION);
		env.put("SERVICES_TEST_SERVICE_CREDENTIALS_URI", TEST_SERVICE_CREDENTIALS_URI);
		env.put("SERVICES_TEST_SERVICE_PLAN_PLAN_1_CREDENTIALS_URI", TEST_SERVICE_PLAN_PLAN_1_CREDENTIALS_URI);
		env.put("SERVICES_TEST_SERVICE_PLAN_PLAN_2_CREDENTIALS_URI", TEST_SERVICE_PLAN_PLAN_2_CREDENTIALS_URI);
		env.put("SERVICES_DUMMY_NAME", DUMMY_NAME);
		env.put("SERVICES_DUMMY_DESCRIPTION", DUMMY_DESCRIPTION);
		env.put("SERVICES_DUMMY_PLAN_0_CREDENTIALS_KEY", DUMMY_PLAN_0_CREDENTIALS_KEY);
		return env;
	}
}
