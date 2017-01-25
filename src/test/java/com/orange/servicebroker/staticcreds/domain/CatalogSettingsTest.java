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
public class CatalogSettingsTest {


    @Test
    public void default_service_displayname_is_equal_to_service_name() {
        //given service with no metadata
        final CatalogSettings catalogSettings = CatalogTestFactory.newInstance();
        final Service tripAdvisorService = catalogSettings.getServices().get(CatalogTestFactory.TRIPADVISOR_TEST_SERVICE);
        Assertions.assertThat(tripAdvisorService.getMetadata().getDisplayName()).isEmpty();

        catalogSettings.init();

        Assertions.assertThat(tripAdvisorService.getMetadata()).isNotNull();
        Assertions.assertThat(tripAdvisorService.getMetadata().getDisplayName()).isEqualTo(tripAdvisorService.getName());

    }

    @Test
    public void default_service_id_is_equal_to_service_name() {
        //given service with no metadata
        final CatalogSettings catalogSettings = CatalogTestFactory.newInstance();
        final Service tripAdvisorService = catalogSettings.getServices().get(CatalogTestFactory.TRIPADVISOR_TEST_SERVICE);
        Assertions.assertThat(tripAdvisorService.getId()).isNull();

        catalogSettings.init();

        Assertions.assertThat(tripAdvisorService.getId()).isEqualTo(tripAdvisorService.getName());
    }

    @Test
    public void default_plan_id_is_equal_to_plan_name() {
        //given service with id
        final CatalogSettings catalogSettings = CatalogTestFactory.newInstance();
        final Service tripAdvisorService = catalogSettings.getServices().get(CatalogTestFactory.API_DIRECTORY_TEST_SERVICE);
        final Plan defaultPlan = tripAdvisorService.getPlans().get(CatalogTestFactory.SERVICE_PLAN_PREPROD);
        Assertions.assertThat(defaultPlan.getId()).isNull();

        catalogSettings.init();

        Assertions.assertThat(defaultPlan.getId()).isEqualTo(tripAdvisorService.getName() + defaultPlan.getName());
    }

    @Test(expected = CatalogSettings.NoCredentialException.class)
    public void fail_if_no_plan_and_service_credentials() {
        //given plan with no credentials
        final CatalogSettings catalogSettings = CatalogTestFactory.newInstance();

        final Service serviceWithNoCredentials = new Service("no_credential_service");
        final Plan planWithNoCredentials = new Plan("no_credential_plan");
        Map<String, Plan> plans = new HashMap();
        plans.put("no_credential_plan", planWithNoCredentials);
        serviceWithNoCredentials.setPlans(plans);

        catalogSettings.getServices().put("no_credential_service", serviceWithNoCredentials);

        final Service tripAdvisorService = catalogSettings.getServices().get(CatalogTestFactory.API_DIRECTORY_TEST_SERVICE);
        final Plan defaultPlan = tripAdvisorService.getPlans().get(CatalogTestFactory.SERVICE_PLAN_PREPROD);
        Assertions.assertThat(defaultPlan.getId()).isNull();

        catalogSettings.init();

        //Assertions.assertThat(defaultPlan.getId()).isEqualTo(tripAdvisorService.getName() + defaultPlan.getName());
    }

    @Test
    public void service_has_a_default_plan() {
        //given service with no plan
        final CatalogSettings catalogSettings = CatalogTestFactory.newInstance();
        final Service tripAdvisorService = catalogSettings.getServices().get(CatalogTestFactory.TRIPADVISOR_TEST_SERVICE);
        Assertions.assertThat(tripAdvisorService.getPlans()).isEmpty();

        catalogSettings.init();

        //then a plan should have been created
        Assertions.assertThat(tripAdvisorService.getPlans()).hasSize(1);
        //with name default
        Assertions.assertThat(tripAdvisorService.getPlans().entrySet().stream().map(planEntry -> planEntry.getValue().getName()).findFirst().get()).isEqualTo(Plan.PLAN_NAME_DEFAULT);
    }

}