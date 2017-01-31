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
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

/**
 * Created by YSBU7453 on 28/04/2016.
 */
public class VolumeMountMapper extends ConfigurableMapper {

    protected void configure(MapperFactory factory) {
        factory.classMap(VolumeMountProperties.class, org.springframework.cloud.servicebroker.model.VolumeMount.class)
                .byDefault()
                .register();
        factory.classMap(SharedVolumeDeviceProperties.class, org.springframework.cloud.servicebroker.model.SharedVolumeDevice.class)
                .byDefault()
                .register();
    }

}
