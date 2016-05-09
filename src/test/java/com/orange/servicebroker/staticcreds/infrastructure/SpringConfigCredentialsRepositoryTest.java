package com.orange.servicebroker.staticcreds.infrastructure;

import org.junit.Test;

import java.util.Map;
import java.util.Optional;

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

        final Optional<Map<String, Object>> credentials = repository.findByPlan(CatalogTestFactory.SERVICE_PLAN_DEV);

        assertThat(credentials.get()).hasSize(3).includes(entry("A_GLOBAL_KEY", "key_value"),entry("URI", "http://mydev-api.org"), entry("ACCESS_KEY", "dev"));

    }

    @Test
    public void only_plan_credentials() throws Exception {

        //GIVEN credentials have been set only at plan level
        SpringConfigCredentialsRepository repository = new SpringConfigCredentialsRepository(CatalogTestFactory.newInstance());

        final Optional<Map<String, Object>> credentials = repository.findByPlan(CatalogTestFactory.SERVICE_PLAN_DEFAULT);

        assertThat(credentials.get()).hasSize(1).includes(entry("URI", "http://my-api.org"));

    }

}