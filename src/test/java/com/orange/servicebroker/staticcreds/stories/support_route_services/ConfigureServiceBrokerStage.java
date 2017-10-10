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

import com.orange.servicebroker.staticcreds.domain.ServiceBrokerProperties;
import com.orange.servicebroker.staticcreds.stories.formatter.CatalogYAML;
import com.tngtech.jgiven.Stage;
import org.assertj.core.api.Assertions;

/**
 * @author Sebastien Bortolussi
 */
public class ConfigureServiceBrokerStage extends Stage<ConfigureServiceBrokerStage> {

    private ServiceBrokerProperties.InvalidRouteServiceException invalidRouteServiceException;

    public ConfigureServiceBrokerStage paas_ops_configures_service_broker_with_following_config(@CatalogYAML ServiceBrokerProperties serviceBrokerProperties) {
        try {
            serviceBrokerProperties.init();
        } catch (ServiceBrokerProperties.InvalidRouteServiceException e) {
            invalidRouteServiceException = e;
        }
        return self();
    }

    public ConfigureServiceBrokerStage it_should_succeed() {
        Assertions.assertThat(invalidRouteServiceException).isNull();
        return self();
    }

    public ConfigureServiceBrokerStage it_should_fail() {
        Assertions.assertThat(invalidRouteServiceException).isNotNull();
        return self();
    }
}
