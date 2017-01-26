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

package com.orange.servicebroker.staticcreds.service;

import com.orange.servicebroker.staticcreds.domain.CatalogSettings;
import com.orange.servicebroker.staticcreds.infrastructure.SpringConfigServicePlanDetailRepository;
import com.orange.servicebroker.staticcreds.stories.formatter.CatalogYAML;
import com.orange.servicebroker.staticcreds.stories.formatter.CreateServiceBindingResponseJSON;
import com.tngtech.jgiven.Stage;
import org.assertj.core.api.Assertions;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceAppBindingResponse;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingResponse;

/**
 * @author Sebastien Bortolussi
 */
public class CreateLogDrainServiceBindingStage extends Stage<CreateLogDrainServiceBindingStage> {

    private CredsServiceInstanceBindingService instanceBindingService;
    private CreateServiceInstanceBindingResponse response;

    public CreateLogDrainServiceBindingStage cloud_controller_requests_to_create_a_service_instance_binding_for_plan_id(String servicePlanId) {
        final CreateServiceInstanceBindingRequest request = new CreateServiceInstanceBindingRequest("myservice-id", servicePlanId, "app-id", null);
        response = instanceBindingService.createServiceInstanceBinding(request);
        return self();
    }

    public CreateLogDrainServiceBindingStage syslog_drain_url_set_in_catalog(@CatalogYAML CatalogSettings catalog) {
        SpringConfigServicePlanDetailRepository planSummaryRepository = new SpringConfigServicePlanDetailRepository(catalog);
        instanceBindingService = new CredsServiceInstanceBindingService(planSummaryRepository);
        return self();
    }

    public CreateLogDrainServiceBindingStage it_should_be_returned_with_syslog_drain_url(@CreateServiceBindingResponseJSON CreateServiceInstanceAppBindingResponse expected) {
        Assertions.assertThat(response).isInstanceOf(CreateServiceInstanceAppBindingResponse.class);
        Assertions.assertThat(response).isEqualTo(expected);
        return self();
    }

}
