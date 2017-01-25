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

import com.orange.servicebroker.staticcreds.YAML;
import com.tngtech.jgiven.Stage;
import org.assertj.core.api.Assertions;

/**
 * @author Sebastien Bortolussi
 */
public class ConfigureSyslogDrainUrlStage extends Stage<ConfigureSyslogDrainUrlStage> {

    private CatalogSettings.InvalidSyslogDrainUrlException invalidSyslogDrainUrlException;

    public ConfigureSyslogDrainUrlStage paas_ops_configures_catalog(@YAML CatalogSettings catalogSettings) {
        try {
            catalogSettings.init();
        } catch (CatalogSettings.InvalidSyslogDrainUrlException e) {
            invalidSyslogDrainUrlException = e;
        }
        return self();
    }

    public ConfigureSyslogDrainUrlStage it_should_succeed() {
        Assertions.assertThat(invalidSyslogDrainUrlException).isNull();
        return self();
    }

    public ConfigureSyslogDrainUrlStage it_should_fail() {
        Assertions.assertThat(invalidSyslogDrainUrlException).isNotNull();
        return self();
    }
}
