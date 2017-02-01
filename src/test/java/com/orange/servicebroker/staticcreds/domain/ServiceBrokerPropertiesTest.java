/*
 * *
 *  * Copyright (C) 2015 Orange
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.orange.servicebroker.staticcreds.domain;

import com.orange.servicebroker.staticcreds.infrastructure.CatalogTestFactory;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YSBU7453 on 10/05/2016.
 */
public class ServiceBrokerPropertiesTest {


    @Test
    public void default_service_displayname_is_equal_to_service_name() {
        //given service with no metadata
        final ServiceBrokerProperties serviceBrokerProperties = CatalogTestFactory.newInstance();
        final ServiceProperties tripAdvisorServiceProperties = serviceBrokerProperties.getServices().get(CatalogTestFactory.TRIPADVISOR_TEST_SERVICE);
        Assertions.assertThat(tripAdvisorServiceProperties.getMetadata().getDisplayName()).isEmpty();

        serviceBrokerProperties.init();

        Assertions.assertThat(tripAdvisorServiceProperties.getMetadata()).isNotNull();
        Assertions.assertThat(tripAdvisorServiceProperties.getMetadata().getDisplayName()).isEqualTo(tripAdvisorServiceProperties.getName());

    }

    @Test
    public void default_service_id_is_equal_to_service_name() {
        //given service with no metadata
        final ServiceBrokerProperties serviceBrokerProperties = CatalogTestFactory.newInstance();
        final ServiceProperties tripAdvisorServiceProperties = serviceBrokerProperties.getServices().get(CatalogTestFactory.TRIPADVISOR_TEST_SERVICE);
        Assertions.assertThat(tripAdvisorServiceProperties.getId()).isNull();

        serviceBrokerProperties.init();

        Assertions.assertThat(tripAdvisorServiceProperties.getId()).isEqualTo(tripAdvisorServiceProperties.getName());
    }

    @Test
    public void default_plan_id_is_equal_to_plan_name() {
        //given service with id
        final ServiceBrokerProperties serviceBrokerProperties = CatalogTestFactory.newInstance();
        final ServiceProperties tripAdvisorServiceProperties = serviceBrokerProperties.getServices().get(CatalogTestFactory.API_DIRECTORY_TEST_SERVICE);
        final PlanProperties defaultPlanProperties = tripAdvisorServiceProperties.getPlans().get(CatalogTestFactory.SERVICE_PLAN_PREPROD);
        Assertions.assertThat(defaultPlanProperties.getId()).isNull();

        serviceBrokerProperties.init();

        Assertions.assertThat(defaultPlanProperties.getId()).isEqualTo(tripAdvisorServiceProperties.getName() + defaultPlanProperties.getName());
    }

    @Test(expected = ServiceBrokerProperties.NoCredentialException.class)
    public void fail_if_no_plan_and_service_credentials() {
        //given plan with no credentials
        final ServiceBrokerProperties serviceBrokerProperties = CatalogTestFactory.newInstance();

        final ServiceProperties servicePropertiesWithNoCredentials = new ServiceProperties("no_credential_service");
        final PlanProperties planPropertiesWithNoCredentials = new PlanProperties("no_credential_plan");
        Map<String, PlanProperties> plans = new HashMap();
        plans.put("no_credential_plan", planPropertiesWithNoCredentials);
        servicePropertiesWithNoCredentials.setPlans(plans);

        serviceBrokerProperties.getServices().put("no_credential_service", servicePropertiesWithNoCredentials);

        final ServiceProperties tripAdvisorServiceProperties = serviceBrokerProperties.getServices().get(CatalogTestFactory.API_DIRECTORY_TEST_SERVICE);
        final PlanProperties defaultPlanProperties = tripAdvisorServiceProperties.getPlans().get(CatalogTestFactory.SERVICE_PLAN_PREPROD);
        Assertions.assertThat(defaultPlanProperties.getId()).isNull();

        serviceBrokerProperties.init();

        //Assertions.assertThat(defaultPlan.getId()).isEqualTo(tripAdvisorServiceProperties.getName() + defaultPlan.getName());
    }

    @Test
    public void service_has_a_default_plan() {
        //given service with no plan
        final ServiceBrokerProperties serviceBrokerProperties = CatalogTestFactory.newInstance();
        final ServiceProperties tripAdvisorServiceProperties = serviceBrokerProperties.getServices().get(CatalogTestFactory.TRIPADVISOR_TEST_SERVICE);
        Assertions.assertThat(tripAdvisorServiceProperties.getPlans()).isEmpty();

        serviceBrokerProperties.init();

        //then a plan should have been created
        Assertions.assertThat(tripAdvisorServiceProperties.getPlans()).hasSize(1);
        //with name default
        Assertions.assertThat(tripAdvisorServiceProperties.getPlans().entrySet().stream().map(planEntry -> planEntry.getValue().getName()).findFirst().get()).isEqualTo(PlanProperties.PLAN_NAME_DEFAULT);
    }

}