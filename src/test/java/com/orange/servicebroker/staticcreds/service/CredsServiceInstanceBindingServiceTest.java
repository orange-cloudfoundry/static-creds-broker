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

package com.orange.servicebroker.staticcreds.service;

import com.orange.servicebroker.staticcreds.domain.PlanProperties;
import com.orange.servicebroker.staticcreds.domain.ServiceBrokerProperties;
import com.orange.servicebroker.staticcreds.domain.ServiceProperties;
import com.orange.servicebroker.staticcreds.infrastructure.SpringConfigServicePlanBindingRepository;
import org.junit.Test;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceAppBindingResponse;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;

import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;


/**
 * Created by sbortolussi on 07/03/2016.
 */
public class CredsServiceInstanceBindingServiceTest {

    public static final String API_DIRECTORY_SERVICE = "API_DIRECTORY";
    public static final String DEV_PLAN = "dev";
    public static final String PROD_PLAN = "prod";
    public static final String DUMMY_PLAN = "dummy";


    @Test
    public void should_bind_with_credentials_that_have_been_set_for_associated_service_plan() throws Exception {

        SpringConfigServicePlanBindingRepository repository = new SpringConfigServicePlanBindingRepository(catalog());

        //when I bind my app to a service API_DIRECTORY instance whose plan is dev
        CredsServiceInstanceBindingService serviceInstanceBindingService = new CredsServiceInstanceBindingService(repository);
        final CreateServiceInstanceAppBindingResponse response = (CreateServiceInstanceAppBindingResponse) serviceInstanceBindingService.createServiceInstanceBinding(getCreateServiceInstanceRequestWithServiceAndPlan(DEV_PLAN));

        //then I should only get credentials that have been set for dev plan of service API_DIRECTORY
        assertThat(response.getCredentials()).hasSize(2).includes(entry("URI", "http://mydev-api.org"), entry("ACCESS_KEY", "devAZERTY"));

    }

    private ServiceBrokerProperties catalog() {
        PlanProperties dev = new PlanProperties(DEV_PLAN);
        Map<String, Object> credentialsDev = new HashMap<>();
        credentialsDev.put("URI", "http://mydev-api.org");
        credentialsDev.put("ACCESS_KEY", "devAZERTY");
        dev.setCredentials(credentialsDev);

        PlanProperties prod = new PlanProperties(PROD_PLAN);
        Map<String, Object> credentialsProd = new HashMap<>();
        credentialsProd.put("URI", "http://myprod-api.org");
        credentialsProd.put("ACCESS_KEY", "prodAZERTY");
        prod.setCredentials(credentialsProd);

        ServiceProperties apiDirectoryServiceProperties = new ServiceProperties();
        apiDirectoryServiceProperties.setName(API_DIRECTORY_SERVICE);
        final Map<String, PlanProperties> apiDirectoryServicePlans = new HashMap<>();
        apiDirectoryServicePlans.put(DEV_PLAN, dev);
        apiDirectoryServicePlans.put(PROD_PLAN, prod);
        apiDirectoryServiceProperties.setPlans(apiDirectoryServicePlans);

        final Map<String, ServiceProperties> services = new HashMap<>();
        services.put(API_DIRECTORY_SERVICE, apiDirectoryServiceProperties);

        return new ServiceBrokerProperties(services);
    }
/*
    @Test
    public void should_bind_with_no_credentials_if_no_credentials_have_been_set_for_associated_service_plan() throws Exception {
        SpringConfigServicePlanBindingRepository repository = new SpringConfigServicePlanBindingRepository(catalog());

        //when I bind my app to a service API_DIRECTORY instance whose plan is dummy
        CredsServiceInstanceBindingService serviceInstanceBindingService = new CredsServiceInstanceBindingService(repository);
        final CreateServiceInstanceAppBindingResponse response = (CreateServiceInstanceAppBindingResponse) serviceInstanceBindingService.createServiceInstanceBinding(getCreateServiceInstanceRequestWithServiceAndPlan(DUMMY_PLAN));

        //then I should get no credentials
        assertThat(response.getCredentials()).isNull();


    }
    */

    private CreateServiceInstanceBindingRequest getCreateServiceInstanceRequestWithServiceAndPlan(String servicePlan) {
        return new CreateServiceInstanceBindingRequest("serviceDefinitionId", servicePlan, "appGuid", null);
    }

}