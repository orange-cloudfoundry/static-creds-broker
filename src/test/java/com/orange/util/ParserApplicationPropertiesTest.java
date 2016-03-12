package com.orange.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.orange.model.ServicePropertyName;
import com.orange.model.ServicesMap;

public class ParserApplicationPropertiesTest {
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
			
	@Test
	public void should_get_services_map_with_info_have_been_set_in_property() {
		ParserApplicationProperties parserApplicationProperties = new ParserApplicationProperties();
		parserApplicationProperties.setServices(getServicesProperty());
		final ServicesMap servicesMap = parserApplicationProperties.parseServicesProperties();
		Assert.assertNotNull(servicesMap);
		Assert.assertEquals(2, servicesMap.geEntrySet().size());
		Assert.assertEquals(API_DIRECTORY_NAME, servicesMap.get(API_DIRECTORY_SERVICE_ID).get(ServicePropertyName.NAME));
		Assert.assertEquals(API_DIRECTORY_DESCRIPTION, servicesMap.get(API_DIRECTORY_SERVICE_ID).get(ServicePropertyName.DESCRIPTION));
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
		services.put(API_DIRECTORY_SERVICE_ID, service_API_DIRECTORY);
		Map<String, Object> service_TRIPADVISOR = new HashMap<>();
		service_TRIPADVISOR.put("NAME", TRIPADVISOR_NAME);
		service_TRIPADVISOR.put("DESCRIPTION", TRIPADVISOR_DESCRIPTION);
		service_TRIPADVISOR.put("TAGS", TRIPADVISOR_TAGS);
		Map<String, Object> service_TRIPADVISOR_METADATA = new HashMap<>();
		service_TRIPADVISOR_METADATA.put("DISPLAYNAME", TRIPADVISOR_METADATA_DISPLAYNAME);
		service_TRIPADVISOR_METADATA.put("IMAGEURL", TRIPADVISOR_METADATA_IMAGEURL);
		service_TRIPADVISOR_METADATA.put("SUPPORTURL", TRIPADVISOR_METADATA_SUPPORTURL);
		service_TRIPADVISOR_METADATA.put("DOCUMENTATIONURL", TRIPADVISOR_METADATA_DOCUMENTATIONURL);
		service_TRIPADVISOR_METADATA.put("PROVIDERDISPLAYNAME", TRIPADVISOR_METADATA_PROVIDERDISPLAYNAME);
		service_TRIPADVISOR_METADATA.put("LONGDESCRIPTION", TRIPADVISOR_METADATA_LONGDESCRIPTION);
		service_TRIPADVISOR.put("METADATA", service_TRIPADVISOR_METADATA);
		services.put(TRIPADVISOR_SERVICE_ID, service_TRIPADVISOR);
		return services;
	}

}
