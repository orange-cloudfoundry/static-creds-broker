package com.orange;

import java.util.Map;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.orange.util.ParserApplicationProperties;
import com.orange.util.ParserProperties;

/**
 * Created by YSBU7453 on 21/03/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
//"native" profile in the Config Server will not use Git, but just loads the config files from the local classpath or file system
@WebIntegrationTest({"security.user.password=pass","spring.profiles.active=native","spring.cloud.config.server.native.searchLocations=classpath:/remote-config"})
public class RemoteConfigTest {

    @Value("${services.API_DIRECTORY.NAME}")
    String serviceApiDirectoryName;
    
    @Autowired
    @Qualifier("parserProperties")
	private ParserProperties parserProperties;
    
    @Test
    public void should_get_remote_config() {
        Assertions.assertThat(serviceApiDirectoryName).isEqualTo("API_DIRECTORY_test_Service");
    }

    @Test
    public void should_get_services_injected() {
    	 Assertions.assertThat(parserProperties).isInstanceOf(ParserApplicationProperties.class);
    	 Map<String, Object> services = ((ParserApplicationProperties) parserProperties).getServices();
    	 Assertions.assertThat(services).isNotNull();
    	 Assertions.assertThat(services.get("API_DIRECTORY")).isNotNull();
    	 Assertions.assertThat(services.get("ID")).isNotNull();
    }
}

