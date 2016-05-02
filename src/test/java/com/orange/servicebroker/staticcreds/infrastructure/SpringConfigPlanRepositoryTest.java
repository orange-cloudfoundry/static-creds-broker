package com.orange.servicebroker.staticcreds.infrastructure;

import com.orange.servicebroker.staticcreds.domain.Plan;
import org.junit.Test;

import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

/**
 * Created by YSBU7453 on 04/04/2016.
 */
public class SpringConfigPlanRepositoryTest {

    @Test
    public void should_find_service_plan_by_its_id() throws Exception {

        SpringConfigPlanRepository repository = new SpringConfigPlanRepository(CatalogTestFactory.newInstance());

        final Optional<Plan> plan = repository.find(CatalogTestFactory.SERVICE_PLAN_DEV);

        assertThat(plan.get().getId()).isEqualTo(CatalogTestFactory.SERVICE_PLAN_DEV);
        assertThat(plan.get().getFullCredentials()).hasSize(2).includes(entry("URI", "http://mydev-api.org"), entry("ACCESS_KEY", "dev"));

    }


}