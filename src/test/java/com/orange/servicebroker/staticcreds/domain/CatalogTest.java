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

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by YSBU7453 on 04/04/2016.
 */
public class CatalogTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

    @After
    public void closeContext() {
        this.context.close();
    }

    @Test
    public void fail_to_set_config_with_no_service() {
        this.context.register(TestConfiguration.class);
        this.thrown.expect(BeanCreationException.class);
        this.thrown.expectMessage(ServiceBrokerProperties.NO_SERVICE_ERROR);
        this.context.refresh();
    }


    @Configuration
    @ComponentScan(basePackages = "com.orange.servicebroker.staticcreds.domain")
    protected static class TestConfiguration {
    }

}