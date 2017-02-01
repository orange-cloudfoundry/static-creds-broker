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

package com.orange.servicebroker.staticcreds.stories.get_syslog_drain_url;

import com.orange.servicebroker.staticcreds.domain.PlanProperties;
import com.orange.servicebroker.staticcreds.domain.ServiceBrokerProperties;
import com.orange.servicebroker.staticcreds.domain.ServiceProperties;
import com.tngtech.jgiven.junit.SimpleScenarioTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sebastien Bortolussi
 */
@GetSyslogLogDrainUrl
@Issue_28
public class ConfigureSyslogDrainUrlTest extends SimpleScenarioTest<ConfigureSyslogDrainUrlStage> {

    public static final String SYSLOG_DRAIN_REQUIRES = "syslog_drain";

    private static ServiceBrokerProperties service_broker_properties_with_syslog_drain_url_at_service_level_and_requires_field_set() {
        PlanProperties dev = new PlanProperties("dev");
        dev.setId("dev-id");
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("URI", "http://my-api.org");
        dev.setCredentials(credentials);

        ServiceProperties myServiceProperties = new ServiceProperties();
        myServiceProperties.setName("myservice");
        myServiceProperties.setId("myservice-id");
        myServiceProperties.setRequires(Arrays.asList(SYSLOG_DRAIN_REQUIRES));
        myServiceProperties.setSyslogDrainUrl("syslog://log.example.com:5000");
        final Map<String, PlanProperties> myServicePlans = new HashMap<>();
        myServicePlans.put("dev", dev);
        myServiceProperties.setPlans(myServicePlans);

        final Map<String, ServiceProperties> services = new HashMap<>();
        services.put("myservice", myServiceProperties);

        return new ServiceBrokerProperties(services);
    }

    private static ServiceBrokerProperties service_broker_properties_with_syslog_drain_url_at_service_plan_level_and_requires_field_set() {
        PlanProperties dev = new PlanProperties("dev");
        dev.setId("dev-id");
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("URI", "http://my-api.org");
        dev.setCredentials(credentials);
        dev.setSyslogDrainUrl("syslog://log.dev.com:5000");

        ServiceProperties myServiceProperties = new ServiceProperties();
        myServiceProperties.setName("myservice");
        myServiceProperties.setId("myservice-id");
        myServiceProperties.setRequires(Arrays.asList(SYSLOG_DRAIN_REQUIRES));
        final Map<String, PlanProperties> myServicePlans = new HashMap<>();
        myServicePlans.put("dev", dev);
        myServiceProperties.setPlans(myServicePlans);

        final Map<String, ServiceProperties> services = new HashMap<>();
        services.put("myservice", myServiceProperties);

        return new ServiceBrokerProperties(services);
    }

    private static ServiceBrokerProperties service_broker_properties_with_syslog_drain_url_at_service_level_but_without_requires_field_set() {
        PlanProperties dev = new PlanProperties("dev");
        dev.setId("dev-id");
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("URI", "http://my-api.org");
        dev.setCredentials(credentials);

        ServiceProperties myServiceProperties = new ServiceProperties();
        myServiceProperties.setName("myservice");
        myServiceProperties.setId("myservice-id");
        myServiceProperties.setSyslogDrainUrl("syslog://log.example.com:5000");
        final Map<String, PlanProperties> myServicePlans = new HashMap<>();
        myServicePlans.put("dev", dev);
        myServiceProperties.setPlans(myServicePlans);

        final Map<String, ServiceProperties> services = new HashMap<>();
        services.put("myservice", myServiceProperties);

        return new ServiceBrokerProperties(services);
    }

    private static ServiceBrokerProperties service_broker_properties_with_syslog_drain_url_at_service_plan_level_but_without_requires_field_set() {
        PlanProperties dev = new PlanProperties("dev");
        dev.setId("dev-id");
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("URI", "http://my-api.org");
        dev.setCredentials(credentials);
        dev.setSyslogDrainUrl("syslog://log.dev.com:5000");

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
    public void configure_service_broker_with_syslog_drain_url_set_at_service_level_and_requires_field_set() throws Exception {
        when().paas_ops_configures_service_broker_with_following_properties(service_broker_properties_with_syslog_drain_url_at_service_level_and_requires_field_set());
        then().it_should_succeed();
    }

    @Test
    public void configure_service_broker_with_syslog_drain_url_set_at_service_level_but_omits_requires_field() throws Exception {
        when().paas_ops_configures_service_broker_with_following_properties(service_broker_properties_with_syslog_drain_url_at_service_level_but_without_requires_field_set());
        then().it_should_fail();
    }

    @Test
    public void configure_service_broker_with_syslog_drain_url_set_at_service_plan_level_and_requires_field_set() throws Exception {
        when().paas_ops_configures_service_broker_with_following_properties(service_broker_properties_with_syslog_drain_url_at_service_plan_level_and_requires_field_set());
        then().it_should_succeed();
    }

    @Test
    public void configure_service_broker_with_syslog_drain_url_set_at_service_plan_level_but_omits_requires_field() throws Exception {
        when().paas_ops_configures_service_broker_with_following_properties(service_broker_properties_with_syslog_drain_url_at_service_plan_level_but_without_requires_field_set());
        then().it_should_fail();
    }
}
