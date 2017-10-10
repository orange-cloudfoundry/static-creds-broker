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

import com.orange.servicebroker.staticcreds.domain.PlanProperties;
import com.orange.servicebroker.staticcreds.domain.ServiceBrokerProperties;
import com.orange.servicebroker.staticcreds.domain.ServiceProperties;
import com.tngtech.jgiven.junit.SimpleScenarioTest;
import org.junit.Test;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceRouteBindingResponse;
import org.springframework.cloud.servicebroker.model.ServiceDefinitionRequires;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sebastien Bortolussi
 */
@AddRouteService
@Issue_32
public class CreateServiceInstanceRouteBindingTest extends SimpleScenarioTest<CreateServiceInstanceRouteBindingStage> {

    private static ServiceBrokerProperties catalog_with_same_route_service_url_for_all_service_plans() {
        PlanProperties dev = new PlanProperties("dev");
        dev.setId("dev-id");
        dev.setCredentials(uriCredentials());

        ServiceProperties myServiceProperties = new ServiceProperties();
        myServiceProperties.setName("myservice");
        myServiceProperties.setId("myservice-id");
        myServiceProperties.setRequires(Arrays.asList(ServiceDefinitionRequires.SERVICE_REQUIRES_ROUTE_FORWARDING.toString()));
        myServiceProperties.setRouteServiceUrl("https://myloggingservice.org/path");
        final Map<String, PlanProperties> myServicePlans = new HashMap<>();
        myServicePlans.put("dev", dev);
        myServiceProperties.setPlans(myServicePlans);

        final Map<String, ServiceProperties> services = new HashMap<>();
        services.put("myservice", myServiceProperties);

        return new ServiceBrokerProperties(services);
    }

    private static Map<String, Object> uriCredentials() {
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("URI", "http://my-api.org");
        return credentials;
    }


    private static ServiceBrokerProperties catalog_with_route_servie_url_set_for_a_service_plan() {
        PlanProperties dev = new PlanProperties("dev");
        dev.setId("dev-id");
        dev.setCredentials(uriCredentials());
        dev.setRouteServiceUrl("https://myloggingservice.org/path");

        ServiceProperties myServiceProperties = new ServiceProperties();
        myServiceProperties.setName("myservice");
        myServiceProperties.setId("myservice-id");
        myServiceProperties.setRequires(Arrays.asList(ServiceDefinitionRequires.SERVICE_REQUIRES_ROUTE_FORWARDING.toString()));
        final Map<String, PlanProperties> myServicePlans = new HashMap<>();
        myServicePlans.put("dev", dev);
        myServiceProperties.setPlans(myServicePlans);

        final Map<String, ServiceProperties> services = new HashMap<>();
        services.put("myservice", myServiceProperties);

        return new ServiceBrokerProperties(services);
    }

    @Test
    public void create_volume_service_binding_with_volume_mount_set_for_all_service_plans() throws Exception {
        given().catalog_with_route_service_url(catalog_with_same_route_service_url_for_all_service_plans());
        when().cloud_controller_requests_to_create_a_route_service_instance_binding_for_plan_id("dev-id");
        then().it_should_be_returned_with_route_service_url(new CreateServiceInstanceRouteBindingResponse()
                .withRouteServiceUrl("https://myloggingservice.org/path"));
    }

    @Test
    public void create_volume_service_binding_with_volume_mount_set_for_a_service_plan() throws Exception {
        given().catalog_with_route_service_url(catalog_with_route_servie_url_set_for_a_service_plan());
        when().cloud_controller_requests_to_create_a_route_service_instance_binding_for_plan_id("dev-id");
        then().it_should_be_returned_with_route_service_url(new CreateServiceInstanceRouteBindingResponse()
                .withRouteServiceUrl("https://myloggingservice.org/path"));
    }

}
