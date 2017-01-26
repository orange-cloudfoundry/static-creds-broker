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

import com.orange.servicebroker.staticcreds.CreateServiceBinding;
import com.orange.servicebroker.staticcreds.domain.CatalogSettings;
import com.orange.servicebroker.staticcreds.domain.Plan;
import com.orange.servicebroker.staticcreds.domain.Service;
import com.tngtech.jgiven.junit.SimpleScenarioTest;
import org.junit.Test;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceAppBindingResponse;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Sebastien Bortolussi
 */
@CreateServiceBinding
public class CreateLogDrainServiceBindingTest extends SimpleScenarioTest<CreateLogDrainServiceBindingStage> {

    public static final String SYSLOG_DRAIN_REQUIRES = "syslog_drain";

    private static CatalogSettings catalog_syslog_drain_url_at_service_level_with_requires_field_set() {
        Plan dev = new Plan("dev");
        dev.setId("dev-id");
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("URI", "http://my-api.org");
        dev.setCredentials(credentials);

        Service myService = new Service();
        myService.setName("myservice");
        myService.setId("myservice-id");
        myService.setRequires(Arrays.asList(SYSLOG_DRAIN_REQUIRES));
        myService.setSyslogDrainUrl("syslog://log.example.com:5000");
        final Map<String, Plan> myServicePlans = new HashMap<>();
        myServicePlans.put("dev", dev);
        myService.setPlans(myServicePlans);

        final Map<String, Service> services = new HashMap<>();
        services.put("myservice", myService);

        return new CatalogSettings(services);
    }

    private static CatalogSettings catalog_syslog_drain_url_at_service_plan_level_with_requires_field_set() {
        Plan dev = new Plan("dev");
        dev.setId("dev-id");
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("URI", "http://my-api.org");
        dev.setCredentials(credentials);
        dev.setSyslogDrainUrl("syslog://log.dev.com:5000");

        Service myService = new Service();
        myService.setName("myservice");
        myService.setId("myservice-id");
        myService.setRequires(Arrays.asList(SYSLOG_DRAIN_REQUIRES));
        final Map<String, Plan> myServicePlans = new HashMap<>();
        myServicePlans.put("dev", dev);
        myService.setPlans(myServicePlans);

        final Map<String, Service> services = new HashMap<>();
        services.put("myservice", myService);

        return new CatalogSettings(services);
    }

    @Test
    public void create_service_binding_with_static_syslog_drain_url_set_at_service_level() throws Exception {
        given().syslog_drain_url_set_in_catalog(catalog_syslog_drain_url_at_service_level_with_requires_field_set());
        when().cloud_controller_requests_to_create_a_service_instance_binding_for_plan_id("dev-id");
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("URI", "http://my-api.org");
        then().it_should_be_returned_with_syslog_drain_url(new CreateServiceInstanceAppBindingResponse()
                .withSyslogDrainUrl("syslog://log.example.com:5000")
                .withCredentials(Collections.unmodifiableMap(Stream.of(
                        new AbstractMap.SimpleEntry<>("URI", "http://my-api.org"))
                        .collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())))));
    }

    @Test
    public void create_service_binding_with_static_syslog_drain_url_set_at_service_plan_level() throws Exception {
        given().syslog_drain_url_set_in_catalog(catalog_syslog_drain_url_at_service_plan_level_with_requires_field_set());
        when().cloud_controller_requests_to_create_a_service_instance_binding_for_plan_id("dev-id");
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("URI", "http://my-api.org");
        then().it_should_be_returned_with_syslog_drain_url(new CreateServiceInstanceAppBindingResponse()
                .withSyslogDrainUrl("syslog://log.dev.com:5000")
                .withCredentials(Collections.unmodifiableMap(Stream.of(
                        new AbstractMap.SimpleEntry<>("URI", "http://my-api.org"))
                        .collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())))));
    }


}
