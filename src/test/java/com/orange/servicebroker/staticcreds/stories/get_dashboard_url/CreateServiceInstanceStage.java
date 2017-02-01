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

package com.orange.servicebroker.staticcreds.stories.get_dashboard_url;

import com.orange.servicebroker.staticcreds.domain.ServiceBrokerProperties;
import com.orange.servicebroker.staticcreds.infrastructure.SpringConfigServicePlanBindingRepository;
import com.orange.servicebroker.staticcreds.service.CredsServiceInstanceService;
import com.orange.servicebroker.staticcreds.stories.formatter.CatalogYAML;
import com.orange.servicebroker.staticcreds.stories.formatter.CreateServiceInstanceResponseJSON;
import com.tngtech.jgiven.Stage;
import org.assertj.core.api.Assertions;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceResponse;

/**
 * @author Sebastien Bortolussi
 */
public class CreateServiceInstanceStage extends Stage<CreateServiceInstanceStage> {

    private CredsServiceInstanceService instanceService;
    private CreateServiceInstanceResponse response;

    public CreateServiceInstanceStage catalog(@CatalogYAML ServiceBrokerProperties catalog) {
        SpringConfigServicePlanBindingRepository planSummaryRepository = new SpringConfigServicePlanBindingRepository(catalog);
        instanceService = new CredsServiceInstanceService(planSummaryRepository);
        return self();
    }


    public CreateServiceInstanceStage cloud_controller_requests_to_create_a_service_instance_for_plan_id(String servicePlanId) {
        final CreateServiceInstanceRequest request = new CreateServiceInstanceRequest("myservice-id", servicePlanId, "org-id", "space-id");
        response = instanceService.createServiceInstance(request);
        return self();
    }

    public CreateServiceInstanceStage it_should_be_returned_with_dashboard_url(@CreateServiceInstanceResponseJSON CreateServiceInstanceResponse expected) {
        Assertions.assertThat(response).isEqualTo(expected);
        return self();
    }

}

