package com.orange;

import com.orange.servicebroker.staticcreds.domain.CredentialsServicePlanBinding;
import com.orange.servicebroker.staticcreds.domain.ServicePlanBinding;
import com.orange.servicebroker.staticcreds.domain.ServicePlanBindingRepository;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.servicebroker.model.Catalog;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

/**
 * Created by YSBU7453 on 21/03/2016.
 */
@RunWith(SpringRunner.class)
//"native" profile in the Config Server will not use Git, but just loads the config files from the local classpath or file system
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {"security.user.password=pass", "spring.profiles.active=native", "spring.cloud.config.server.native.searchLocations=classpath:/remote-config"})
public class RemoteConfigTest {

    public static final String API_DIRECTORY_SERVICE_PLAN_DEV_ID = "f7ae3ff9-85ed-3515-bef9-2e4d2f572422";

    @Value("${services.API_DIRECTORY.NAME}")
    String serviceApiDirectoryName;

    @Autowired
    ServicePlanBindingRepository servicePlanBindingRepository;

    @Autowired
    Catalog catalog;

    @Test
    public void should_get_remote_config() {
        Assertions.assertThat(serviceApiDirectoryName).isEqualTo("API_DIRECTORY_test_Service");
    }

    @Test
    public void should_find_a_service_plan() {

        //service plan id for plan dev of service API_DIRECTORY, see static-creds-broker.yml

        final Optional<ServicePlanBinding> servicePlanBinding = servicePlanBindingRepository.find(API_DIRECTORY_SERVICE_PLAN_DEV_ID);

        assertThat(servicePlanBinding
                .map(CredentialsServicePlanBinding.class::cast)
                .get().getCredentials()
        )
                .hasSize(3).includes(entry("HOSTNAME", "http://company.com"), entry("URI", "http://mydev-api.org"), entry("ACCESS_KEY", "devAZERT23456664DFDSFSDFDSF"));

    }

}

