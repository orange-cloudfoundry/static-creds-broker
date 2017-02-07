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
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceAppBindingResponse;
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
public class CreateServiceInstanceVolumeBindingTest extends SimpleScenarioTest<CreateServiceInstanceVolumeBindingStage> {

    private static ServiceBrokerProperties catalog_with_same_volume_mount_for_all_service_plans() {
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

    private static ServiceBrokerProperties catalog_with_volume_mount_set_for_a_service_plan() {
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

    private static VolumeMount expectedNfsv3SharedVolumeMount() {
        Map<String, Object> mountConfig = new HashMap<>();
        mountConfig.put("source", "nfs://1.2.3.4:25840/my/share/to/mount?uid=1004&gid=1004&auto_cache&multithread&default_permissions");
        mountConfig.put("attr_timeout", "0");
        mountConfig.put("negative_timeout", "2");
        final SharedVolumeDevice sharedVolumeDevice = new SharedVolumeDevice("bc2c1eab-05b9-482d-b0cf-750ee07de311", mountConfig);
        return new VolumeMount("nfsv3driver", "/data/images", VolumeMount.Mode.READ_WRITE, VolumeMount.DeviceType.SHARED, sharedVolumeDevice);
    }

    @Test
    public void create_volume_service_binding_with_volume_mount_set_for_all_service_plans() throws Exception {
        given().catalog_with_volume_mount(catalog_with_same_volume_mount_for_all_service_plans());
        when().cloud_controller_requests_to_create_a_service_instance_binding_for_plan_id("dev-id");
        then().it_should_be_returned_with_volume_mount(new CreateServiceInstanceAppBindingResponse()
                .withCredentials(uriCredentials())
                .withVolumeMounts(Arrays.asList(expectedNfsv3SharedVolumeMount())));
    }

    @Test
    public void create_volume_service_binding_with_volume_mount_set_for_a_service_plan() throws Exception {
        given().catalog_with_volume_mount(catalog_with_volume_mount_set_for_a_service_plan());
        when().cloud_controller_requests_to_create_a_service_instance_binding_for_plan_id("dev-id");
        then().it_should_be_returned_with_volume_mount(new CreateServiceInstanceAppBindingResponse()
                .withCredentials(uriCredentials())
                .withVolumeMounts(Arrays.asList(expectedNfsv3SharedVolumeMount())));
    }

}
