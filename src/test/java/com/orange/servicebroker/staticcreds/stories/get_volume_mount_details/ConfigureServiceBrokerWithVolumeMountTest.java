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

package com.orange.servicebroker.staticcreds.stories.get_volume_mount_details;

import com.orange.servicebroker.staticcreds.domain.*;
import com.tngtech.jgiven.junit.SimpleScenarioTest;
import org.junit.Test;
import org.springframework.cloud.servicebroker.model.ServiceDefinitionRequires;
import org.springframework.cloud.servicebroker.model.SharedVolumeDevice;
import org.springframework.cloud.servicebroker.model.VolumeMount;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sebastien Bortolussi
 */
@GetVolumeMountDetails
@Issue_31
public class ConfigureServiceBrokerWithVolumeMountTest extends SimpleScenarioTest<ConfigureServiceBrokerStage> {

    private static ServiceBrokerProperties service_broker_properties_with_volume_mount_at_service_level_and_requires_field_set() {
        PlanProperties dev = new PlanProperties("dev");
        dev.setId("dev-id");
        dev.setCredentials(uriCredentials());

        ServiceProperties myServiceProperties = new ServiceProperties();
        myServiceProperties.setName("myservice");
        myServiceProperties.setId("myservice-id");
        myServiceProperties.setRequires(Arrays.asList(ServiceDefinitionRequires.SERVICE_REQUIRES_VOLUME_MOUNT.toString()));
        myServiceProperties.setVolumeMounts(Arrays.asList(nfsv3SharedVolumeMount()));
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

    private static VolumeMountProperties nfsv3SharedVolumeMount() {
        Map<String, Object> mountConfig = new HashMap<>();
        mountConfig.put("source", "nfs://1.2.3.4:25840/my/share/to/mount?uid=1004&gid=1004&auto_cache&multithread&default_permissions");
        mountConfig.put("attr_timeout", "0");
        mountConfig.put("negative_timeout", "2");
        final SharedVolumeDeviceProperties sharedVolumeDevice = new SharedVolumeDeviceProperties("bc2c1eab-05b9-482d-b0cf-750ee07de311", mountConfig);
        return new VolumeMountProperties("nfsv3driver", "/data/images", VolumeMountProperties.Mode.READ_WRITE, VolumeMountProperties.DeviceType.SHARED, sharedVolumeDevice);
    }

    private static VolumeMount expectedVolumeMount() {
        Map<String, Object> mountConfig = new HashMap<>();
        mountConfig.put("source", "nfs://1.2.3.4:25840/my/share/to/mount?uid=1004&gid=1004&auto_cache&multithread&default_permissions");
        mountConfig.put("attr_timeout", "0");
        mountConfig.put("negative_timeout", "2");
        final SharedVolumeDevice sharedVolumeDevice = new SharedVolumeDevice("bc2c1eab-05b9-482d-b0cf-750ee07de311", mountConfig);
        return new VolumeMount("nfsv3driver", "/data/images", VolumeMount.Mode.READ_WRITE, VolumeMount.DeviceType.SHARED, sharedVolumeDevice);
    }

    private static ServiceBrokerProperties service_broker_properties_with_volume_mount_at_service_plan_level_and_requires_field_set() {
        PlanProperties dev = new PlanProperties("dev");
        dev.setId("dev-id");
        dev.setCredentials(uriCredentials());
        dev.setVolumeMounts(Arrays.asList(nfsv3SharedVolumeMount()));

        ServiceProperties myServiceProperties = new ServiceProperties();
        myServiceProperties.setName("myservice");
        myServiceProperties.setId("myservice-id");
        myServiceProperties.setRequires(Arrays.asList(ServiceDefinitionRequires.SERVICE_REQUIRES_VOLUME_MOUNT.toString()));
        final Map<String, PlanProperties> myServicePlans = new HashMap<>();
        myServicePlans.put("dev", dev);
        myServiceProperties.setPlans(myServicePlans);

        final Map<String, ServiceProperties> services = new HashMap<>();
        services.put("myservice", myServiceProperties);

        return new ServiceBrokerProperties(services);
    }

    private static ServiceBrokerProperties catalog_with_volume_mount_at_service_level_but_without_requires_field_set() {
        PlanProperties dev = new PlanProperties("dev");
        dev.setId("dev-id");
        dev.setCredentials(uriCredentials());

        ServiceProperties myServiceProperties = new ServiceProperties();
        myServiceProperties.setName("myservice");
        myServiceProperties.setId("myservice-id");
        myServiceProperties.setVolumeMounts(Arrays.asList(nfsv3SharedVolumeMount()));
        final Map<String, PlanProperties> myServicePlans = new HashMap<>();
        myServicePlans.put("dev", dev);
        myServiceProperties.setPlans(myServicePlans);

        final Map<String, ServiceProperties> services = new HashMap<>();
        services.put("myservice", myServiceProperties);

        return new ServiceBrokerProperties(services);
    }

    private static ServiceBrokerProperties service_broker_properties_with_volume_mount_at_service_plan_level_but_without_requires_field_set() {
        PlanProperties dev = new PlanProperties("dev");
        dev.setId("dev-id");
        dev.setCredentials(uriCredentials());
        dev.setVolumeMounts(Arrays.asList(nfsv3SharedVolumeMount()));

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
    public void configure_service_broker_with_same_volume_mount_for_all_service_plans_and_requires_field_set() throws Exception {
        when().paas_ops_configures_service_broker_with_following_config(service_broker_properties_with_volume_mount_at_service_level_and_requires_field_set());
        then().it_should_succeed();
    }

    @Test
    public void configure_service_broker_with_same_volume_mount_for_all_service_plans_but_omits_requires_field() throws Exception {
        when().paas_ops_configures_service_broker_with_following_config(catalog_with_volume_mount_at_service_level_but_without_requires_field_set());
        then().it_should_fail();
    }

    @Test
    public void configure_service_broker_with_volume_mount_set_for_a_service_plan_and_requires_field_set() throws Exception {
        when().paas_ops_configures_service_broker_with_following_config(service_broker_properties_with_volume_mount_at_service_plan_level_and_requires_field_set());
        then().it_should_succeed();
    }

    @Test
    public void configure_service_broker_with_volume_mount_set_for_a_service_plan_but_omits_requires_field() throws Exception {
        when().paas_ops_configures_service_broker_with_following_config(service_broker_properties_with_volume_mount_at_service_plan_level_but_without_requires_field_set());
        then().it_should_fail();
    }
}
