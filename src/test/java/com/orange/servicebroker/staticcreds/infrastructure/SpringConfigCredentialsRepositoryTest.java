package com.orange.servicebroker.staticcreds.infrastructure;

import org.junit.Test;

import java.util.Map;
import java.util.Optional;

import static com.orange.servicebroker.staticcreds.infrastructure.CatalogTestFactory.HOSTNAME_PLAN_PROD_VALUE;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

/**
 * Created by YSBU7453 on 03/05/2016.
 */
public class SpringConfigCredentialsRepositoryTest {


    @Test
    public void plan_credentials_override_service_credentials() throws Exception {

        //GIVEN credentials have been set at service and plan level
        //some overlaps
        SpringConfigCredentialsRepository repository = new SpringConfigCredentialsRepository(CatalogTestFactory.newInstance());

        final Optional<Map<String, Object>> credentials = repository.findByPlan(CatalogTestFactory.SERVICE_PLAN_PROD);

        assertThat(credentials.get()).hasSize(3).includes(entry("HOSTNAME", HOSTNAME_PLAN_PROD_VALUE),entry("URI", "http://myprod-api.org"), entry("ACCESS_KEY", "prod"));

    }

}