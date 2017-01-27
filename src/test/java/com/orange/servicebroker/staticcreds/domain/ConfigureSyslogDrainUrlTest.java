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

import com.orange.servicebroker.staticcreds.stories.tags.ConfigureCatalog;
import com.tngtech.jgiven.junit.SimpleScenarioTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sebastien Bortolussi
 */
@ConfigureCatalog
public class ConfigureSyslogDrainUrlTest extends SimpleScenarioTest<ConfigureSyslogDrainUrlStage> {

    public static final String SYSLOG_DRAIN_REQUIRES = "syslog_drain";

    private static CatalogSettings catalog_with_syslog_drain_url_at_service_level_and_requires_field_set() {
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

    private static CatalogSettings catalog_with_syslog_drain_url_at_service_plan_level_and_requires_field_set() {
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

    private static CatalogSettings catalog_with_syslog_drain_url_at_service_level_but_without_requires_field_set() {
        Plan dev = new Plan("dev");
        dev.setId("dev-id");
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("URI", "http://my-api.org");
        dev.setCredentials(credentials);

        Service myService = new Service();
        myService.setName("myservice");
        myService.setId("myservice-id");
        myService.setSyslogDrainUrl("syslog://log.example.com:5000");
        final Map<String, Plan> myServicePlans = new HashMap<>();
        myServicePlans.put("dev", dev);
        myService.setPlans(myServicePlans);

        final Map<String, Service> services = new HashMap<>();
        services.put("myservice", myService);

        return new CatalogSettings(services);
    }

    private static CatalogSettings catalog_with_syslog_drain_url_at_service_plan_level_but_without_requires_field_set() {
        Plan dev = new Plan("dev");
        dev.setId("dev-id");
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("URI", "http://my-api.org");
        dev.setCredentials(credentials);
        dev.setSyslogDrainUrl("syslog://log.dev.com:5000");

        Service myService = new Service();
        myService.setName("myservice");
        myService.setId("myservice-id");
        final Map<String, Plan> myServicePlans = new HashMap<>();
        myServicePlans.put("dev", dev);
        myService.setPlans(myServicePlans);

        final Map<String, Service> services = new HashMap<>();
        services.put("myservice", myService);

        return new CatalogSettings(services);
    }


    @Test
    public void service_broker_configures_catalog_with_syslog_drain_url_set_at_service_level_and_requires_field_set() throws Exception {
        when().paas_ops_configures_catalog(catalog_with_syslog_drain_url_at_service_level_and_requires_field_set());
        then().it_should_succeed();
    }

    @Test
    public void service_broker_configures_catalog_with_syslog_drain_url_set_at_service_level_but_omits_requires_field() throws Exception {
        when().paas_ops_configures_catalog(catalog_with_syslog_drain_url_at_service_level_but_without_requires_field_set());
        then().it_should_fail();
    }

    @Test
    public void service_broker_configures_catalog_with_syslog_drain_url_set_at_service_plan_level_and_requires_field_set() throws Exception {
        when().paas_ops_configures_catalog(catalog_with_syslog_drain_url_at_service_plan_level_and_requires_field_set());
        then().it_should_succeed();
    }

    @Test
    public void service_broker_configures_catalog_with_syslog_drain_url_set_at_service_plan_level_but_omits_requires_field() throws Exception {
        when().paas_ops_configures_catalog(catalog_with_syslog_drain_url_at_service_plan_level_but_without_requires_field_set());
        then().it_should_fail();
    }
}
