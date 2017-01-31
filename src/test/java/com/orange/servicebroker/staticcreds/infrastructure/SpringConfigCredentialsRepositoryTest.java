package com.orange.servicebroker.staticcreds.infrastructure;

import com.orange.servicebroker.staticcreds.domain.CredentialsServicePlanBinding;
import com.orange.servicebroker.staticcreds.domain.ServicePlanBinding;
import org.junit.Test;

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
        SpringConfigServicePlanBindingRepository repository = new SpringConfigServicePlanBindingRepository(CatalogTestFactory.newInstance());

        final Optional<ServicePlanBinding> planSummary = repository.find(CatalogTestFactory.SERVICE_PLAN_PROD);

        assertThat(planSummary
                .map(CredentialsServicePlanBinding.class::cast)
                .get().getCredentials()
        ).hasSize(3).includes(entry("HOSTNAME", HOSTNAME_PLAN_PROD_VALUE), entry("URI", "http://myprod-api.org"), entry("ACCESS_KEY", "prod"));

    }

}