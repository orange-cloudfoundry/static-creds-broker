package com.orange;

import com.orange.servicebroker.staticcreds.domain.Plan;
import com.orange.servicebroker.staticcreds.domain.PlanRepository;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.cloud.servicebroker.model.Catalog;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;
import java.util.UUID;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

/**
 * Created by YSBU7453 on 21/03/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
//"native" profile in the Config Server will not use Git, but just loads the config files from the local classpath or file system
@WebIntegrationTest({"security.user.password=pass","spring.profiles.active=native","spring.cloud.config.server.native.searchLocations=classpath:/remote-config"})
public class RemoteConfigTest {

    public static final String API_DIRECTORY_SERVICE_PLAN_DEV_ID = "f7ae3ff9-85ed-3515-bef9-2e4d2f572422";

    @Value("${services.API_DIRECTORY.NAME}")
    String serviceApiDirectoryName;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    Catalog catalog;

    @Test
    public void should_get_remote_config() {
        Assertions.assertThat(serviceApiDirectoryName).isEqualTo("API_DIRECTORY_test_Service");
    }

   @Test
    public void should_find_a_service_plan() {

        //service plan id for plan dev of service API_DIRECTORY, see static-creds-broker.yml
        UUID servicePlanId = UUID.fromString(API_DIRECTORY_SERVICE_PLAN_DEV_ID);

        final Optional<Plan> plan = planRepository.find(servicePlanId);

        assertThat(plan.get().getId().toString()).isEqualTo(API_DIRECTORY_SERVICE_PLAN_DEV_ID);
        assertThat(plan.get().getFullCredentials()).hasSize(2).includes(entry("URI", "http://mydev-api.org"), entry("ACCESS_KEY", "devAZERT23456664DFDSFSDFDSF"));

    }

    @Test
    public void should_find_all_services() {

        //service plan id for plan dev of service API_DIRECTORY, see static-creds-broker.yml
        UUID servicePlanId = UUID.fromString(API_DIRECTORY_SERVICE_PLAN_DEV_ID);

        final Optional<Plan> plan = planRepository.find(servicePlanId);

        assertThat(plan.get().getId().toString()).isEqualTo(API_DIRECTORY_SERVICE_PLAN_DEV_ID);
        assertThat(plan.get().getFullCredentials()).hasSize(2).includes(entry("URI", "http://mydev-api.org"), entry("ACCESS_KEY", "devAZERT23456664DFDSFSDFDSF"));

    }
}

