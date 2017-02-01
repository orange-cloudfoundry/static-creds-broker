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

import com.orange.servicebroker.staticcreds.domain.PlanProperties;
import com.orange.servicebroker.staticcreds.domain.ServiceBrokerProperties;
import com.orange.servicebroker.staticcreds.domain.ServiceProperties;
import com.tngtech.jgiven.junit.SimpleScenarioTest;
import org.junit.Test;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sebastien Bortolussi
 */
@GetServiceInstanceDashboardUrl
@Issue_29
public class CreateServiceInstanceInstanceWithDashboardUrlTest extends SimpleScenarioTest<CreateServiceInstanceStage> {

    private static ServiceBrokerProperties apply_dashboard_url_for_all_plans_of_a_service() {
        PlanProperties dev = new PlanProperties("dev");
        dev.setId("dev-id");
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("URI", "http://my-api.org");
        dev.setCredentials(credentials);

        ServiceProperties myServiceProperties = new ServiceProperties();
        myServiceProperties.setName("myservice");
        myServiceProperties.setId("myservice-id");
        myServiceProperties.setDashboardUrl("http://example-dashboard.example.com/9189kdfsk0vfnku");
        final Map<String, PlanProperties> myServicePlans = new HashMap<>();
        myServicePlans.put("dev", dev);
        myServiceProperties.setPlans(myServicePlans);

        final Map<String, ServiceProperties> services = new HashMap<>();
        services.put("myservice", myServiceProperties);

        return new ServiceBrokerProperties(services);
    }

    private static ServiceBrokerProperties apply_dashboard_url_for_a_service_plan() {
        PlanProperties dev = new PlanProperties("dev");
        dev.setId("dev-id");
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("URI", "http://my-api.org");
        dev.setCredentials(credentials);
        dev.setDashboardUrl("http://example-dashboard.dev.com/9189kdfsk0vfnku");


        ServiceProperties myServiceProperties = new ServiceProperties();
        myServiceProperties.setName("myservice");
        myServiceProperties.setId("myservice-id");
        final Map<String, PlanProperties> myServicePlans = new HashMap<>();
        myServicePlans.put("dev", dev);
        myServiceProperties.setPlans(myServicePlans);

        final Map<String, ServiceProperties> services = new HashMap<>();
        services.put("myservice", myServiceProperties);

        return new ServiceBrokerProperties(services);
    }

    @Test
    public void create_service_instance_with_dashboard_url_set_for_all_plans_of_a_service() throws Exception {
        given().catalog(apply_dashboard_url_for_all_plans_of_a_service());
        when().cloud_controller_requests_to_create_a_service_instance_for_plan_id("dev-id");
        then().it_should_be_returned_with_dashboard_url(new CreateServiceInstanceResponse()
                .withDashboardUrl("http://example-dashboard.example.com/9189kdfsk0vfnku"));
    }

    @Test
    public void create_service_instance_with_dashboard_url_set_for_a_service_plan() throws Exception {
        given().catalog(apply_dashboard_url_for_a_service_plan());
        when().cloud_controller_requests_to_create_a_service_instance_for_plan_id("dev-id");
        then().it_should_be_returned_with_dashboard_url(new CreateServiceInstanceResponse()
                .withDashboardUrl("http://example-dashboard.dev.com/9189kdfsk0vfnku"));
    }


}
