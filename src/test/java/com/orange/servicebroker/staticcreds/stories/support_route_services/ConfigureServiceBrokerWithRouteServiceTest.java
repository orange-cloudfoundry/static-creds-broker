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
import org.springframework.cloud.servicebroker.model.ServiceDefinitionRequires;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sebastien Bortolussi
 */
@AddRouteService
@Issue_32
public class ConfigureServiceBrokerWithRouteServiceTest extends SimpleScenarioTest<ConfigureServiceBrokerStage> {

    private static ServiceBrokerProperties service_broker_properties_with_route_service_url_at_service_level_and_requires_field_set() {
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

    private static ServiceBrokerProperties service_broker_properties_with_route_service_url_at_service_plan_level_and_requires_field_set() {
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

    private static ServiceBrokerProperties catalog_with_route_service_url_at_service_level_but_without_requires_field_set() {
        PlanProperties dev = new PlanProperties("dev");
        dev.setId("dev-id");
        dev.setCredentials(uriCredentials());

        ServiceProperties myServiceProperties = new ServiceProperties();
        myServiceProperties.setName("myservice");
        myServiceProperties.setId("myservice-id");
        myServiceProperties.setRouteServiceUrl("https://myloggingservice.org/path");
        final Map<String, PlanProperties> myServicePlans = new HashMap<>();
        myServicePlans.put("dev", dev);
        myServiceProperties.setPlans(myServicePlans);

        final Map<String, ServiceProperties> services = new HashMap<>();
        services.put("myservice", myServiceProperties);

        return new ServiceBrokerProperties(services);
    }

    private static ServiceBrokerProperties service_broker_properties_with_route_service_url_at_service_plan_level_but_without_requires_field_set() {
        PlanProperties dev = new PlanProperties("dev");
        dev.setId("dev-id");
        dev.setCredentials(uriCredentials());
        dev.setRouteServiceUrl("https://myloggingservice.org/path");

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
    public void configure_service_broker_with_same_route_service_url_for_all_service_plans_and_requires_field_set() throws Exception {
        when().paas_ops_configures_service_broker_with_following_config(service_broker_properties_with_route_service_url_at_service_level_and_requires_field_set());
        then().it_should_succeed();
    }

    @Test
    public void configure_service_broker_with_same_route_service_url_for_all_service_plans_but_omits_requires_field() throws Exception {
        when().paas_ops_configures_service_broker_with_following_config(catalog_with_route_service_url_at_service_level_but_without_requires_field_set());
        then().it_should_fail();
    }

    @Test
    public void configure_service_broker_with_route_service_url_set_for_a_service_plan_and_requires_field_set() throws Exception {
        when().paas_ops_configures_service_broker_with_following_config(service_broker_properties_with_route_service_url_at_service_plan_level_and_requires_field_set());
        then().it_should_succeed();
    }

    @Test
    public void configure_service_broker_with_route_service_url_set_for_a_service_plan_but_omits_requires_field() throws Exception {
        when().paas_ops_configures_service_broker_with_following_config(service_broker_properties_with_route_service_url_at_service_plan_level_but_without_requires_field_set());
        then().it_should_fail();
    }
}
