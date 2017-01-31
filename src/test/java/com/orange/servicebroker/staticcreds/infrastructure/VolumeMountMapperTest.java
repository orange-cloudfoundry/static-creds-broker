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

package com.orange.servicebroker.staticcreds.infrastructure;

import com.orange.servicebroker.staticcreds.domain.SharedVolumeDeviceProperties;
import com.orange.servicebroker.staticcreds.domain.VolumeMountProperties;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.springframework.cloud.servicebroker.model.SharedVolumeDevice;
import org.springframework.cloud.servicebroker.model.VolumeMount;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sebastien Bortolussi
 */
public class VolumeMountMapperTest {


    private static VolumeMountProperties volumeMountProperties() {
        Map<String, Object> mountConfig = new HashMap<>();
        mountConfig.put("source", "nfs://1.2.3.4:25840/my/share/to/mount?uid=1004&gid=1004&auto_cache&multithread&default_permissions");
        mountConfig.put("attr_timeout", "0");
        mountConfig.put("negative_timeout", "2");
        final SharedVolumeDeviceProperties sharedVolumeDevice = new SharedVolumeDeviceProperties("bc2c1eab-05b9-482d-b0cf-750ee07de311", mountConfig);
        return new VolumeMountProperties("nfsv3driver", "/data/images", VolumeMountProperties.Mode.READ_WRITE, VolumeMountProperties.DeviceType.SHARED, sharedVolumeDevice);
    }

    private static VolumeMount expected() {
        Map<String, Object> mountConfig = new HashMap<>();
        mountConfig.put("source", "nfs://1.2.3.4:25840/my/share/to/mount?uid=1004&gid=1004&auto_cache&multithread&default_permissions");
        mountConfig.put("attr_timeout", "0");
        mountConfig.put("negative_timeout", "2");
        final SharedVolumeDevice sharedVolumeDevice = new SharedVolumeDevice("bc2c1eab-05b9-482d-b0cf-750ee07de311", mountConfig);
        return new VolumeMount("nfsv3driver", "/data/images", VolumeMount.Mode.READ_WRITE, VolumeMount.DeviceType.SHARED, sharedVolumeDevice);
    }

    @Test
    public void should_map_volume_mount() throws Exception {
        VolumeMountMapper mapper = new VolumeMountMapper();

        Assertions.assertThat(mapper.map(volumeMountProperties(), VolumeMount.class)).isEqualTo(expected());
    }
}