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

import lombok.*;

import java.util.Map;
import java.util.Optional;

/**
 * Cloud Foundry plan summary
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class PlanSummary {

    /**
     * The URL to which Cloud Foundry should drain logs for the bound application.
     */
    private Optional<String> syslogDrainUrl;

    /**
     * A free-form hash of credentials that the bound application can use to access the service.
     */
    @Singular
    private Map<String, Object> credentials;

}
