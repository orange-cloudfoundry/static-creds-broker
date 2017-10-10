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

package com.orange.servicebroker.staticcreds.stories.support_route_services;

import com.orange.servicebroker.staticcreds.domain.ServiceBrokerProperties;
import com.orange.servicebroker.staticcreds.infrastructure.SpringConfigServicePlanBindingRepository;
import com.orange.servicebroker.staticcreds.service.CredsServiceInstanceBindingService;
import com.orange.servicebroker.staticcreds.stories.formatter.CatalogYAML;
import com.orange.servicebroker.staticcreds.stories.formatter.CreateServiceBindingResponseJSON;
import com.tngtech.jgiven.Stage;
import org.assertj.core.api.Assertions;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceRouteBindingResponse;

/**
 * @author Sebastien Bortolussi
 */
public class CreateServiceInstanceRouteBindingStage extends Stage<CreateServiceInstanceRouteBindingStage> {

    private CredsServiceInstanceBindingService instanceBindingService;
    private CreateServiceInstanceBindingResponse response;

    public CreateServiceInstanceRouteBindingStage cloud_controller_requests_to_create_a_route_service_instance_binding_for_plan_id(String servicePlanId) {
        final CreateServiceInstanceBindingRequest request = new CreateServiceInstanceBindingRequest("myservice-id", servicePlanId, "app-id", null);
        response = instanceBindingService.createServiceInstanceBinding(request);
        return self();
    }

    public CreateServiceInstanceRouteBindingStage catalog_with_route_service_url(@CatalogYAML ServiceBrokerProperties catalog) {
        SpringConfigServicePlanBindingRepository planSummaryRepository = new SpringConfigServicePlanBindingRepository(catalog);
        instanceBindingService = new CredsServiceInstanceBindingService(planSummaryRepository);
        return self();
    }

    public CreateServiceInstanceRouteBindingStage it_should_be_returned_with_route_service_url(@CreateServiceBindingResponseJSON CreateServiceInstanceBindingResponse expected) {
        Assertions.assertThat(response).isInstanceOf(CreateServiceInstanceRouteBindingResponse.class);
        Assertions.assertThat(response).isEqualTo(expected);
        return self();
    }

}
