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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Details of a volume mount in a binding response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VolumeMountProperties {
    /**
     * The name of the volume driver plugin which manages the device.
     */
    private String driver;
    /**
     * The directory to mount inside the application container.
     */
    private String containerDir;
    /**
     * Indicates whether the volume can be mounted in read-only or read-write mode.
     */
    private Mode mode;
    /**
     * The type of the volume device to mount.
     */
    private DeviceType deviceType;
    /**
     * Details of the volume device to mount, specific to the device type.
     */
    private SharedVolumeDeviceProperties device;

    public enum Mode {
        READ_ONLY("r"),
        READ_WRITE("rw");

        private final String value;

        Mode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum DeviceType {
        /**
         * A shared volume mount represents a distributed file system which can be mounted on all app instances
         * simultaneously.
         */
        SHARED("shared");

        private final String value;

        DeviceType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
