/*
 *
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
 *  *
 *
 */

package com.orange.service;

import com.orange.model.CredentialsMap;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingResponse;

import java.util.Arrays;
import java.util.UUID;


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

        CredentialsMap credentialsMap = new CredentialsMap();
        //given credentials have been set for dev plan of service API_DIRECTORY
        credentialsMap.addCredential(API_DIRECTORY_SERVICE, DEV_PLAN,"CREDENTIALS_URI","http://mydev-api.org");
        credentialsMap.addCredential(API_DIRECTORY_SERVICE,DEV_PLAN,"CREDENTIALS_ACCESS_KEY","devAZERTY");
        //given credentials have been set for prod plan of service API_DIRECTORY
        credentialsMap.addCredential(API_DIRECTORY_SERVICE, PROD_PLAN,"CREDENTIALS_URI","http://myprod-api.org");
        credentialsMap.addCredential(API_DIRECTORY_SERVICE, PROD_PLAN,"CREDENTIALS_ACCESS_KEY","prodAZERTY");

        //when I bind my app to a service API_DIRECTORY instance whose plan is dev
        CredsServiceInstanceBindingService serviceInstanceBindingService = new CredsServiceInstanceBindingService(credentialsMap);
        final CreateServiceInstanceBindingResponse response = serviceInstanceBindingService.createServiceInstanceBinding(getCreateServiceInstanceRequestWithServiceAndPlan(API_DIRECTORY_SERVICE,DEV_PLAN));

        //then I should only get credentials that have been set for dev plan of service API_DIRECTORY
        Assert.assertNotNull(response.getCredentials());
        Assert.assertEquals(2,response.getCredentials().size());
        Assert.assertEquals("http://mydev-api.org",response.getCredentials().get("CREDENTIALS_URI"));
        Assert.assertEquals("devAZERTY",response.getCredentials().get("CREDENTIALS_ACCESS_KEY"));

    }

    @Test
    public void should_bind_with_no_credentials_if_no_credentials_have_been_set_for_associated_service_plan() throws Exception {

        CredentialsMap credentialsMap = new CredentialsMap();
        //given credentials have been set for dev plan of service API_DIRECTORY
        credentialsMap.addCredential(API_DIRECTORY_SERVICE, DEV_PLAN,"CREDENTIALS_URI","http://mydev-api.org");
        credentialsMap.addCredential(API_DIRECTORY_SERVICE,DEV_PLAN,"CREDENTIALS_ACCESS_KEY","devAZERTY");
        //given credentials have been set for prod plan of service API_DIRECTORY
        credentialsMap.addCredential(API_DIRECTORY_SERVICE, PROD_PLAN,"CREDENTIALS_URI","http://myprod-api.org");
        credentialsMap.addCredential(API_DIRECTORY_SERVICE, PROD_PLAN,"CREDENTIALS_ACCESS_KEY","prodAZERTY");

        //when I bind my app to a service API_DIRECTORY instance whose plan is dummy
        CredsServiceInstanceBindingService serviceInstanceBindingService = new CredsServiceInstanceBindingService(credentialsMap);
        final CreateServiceInstanceBindingResponse response = serviceInstanceBindingService.createServiceInstanceBinding(getCreateServiceInstanceRequestWithServiceAndPlan(API_DIRECTORY_SERVICE,DUMMY_PLAN));

        //then I should get no credentials
        Assert.assertNull(response.getCredentials());

    }

    private CreateServiceInstanceBindingRequest getCreateServiceInstanceRequestWithServiceAndPlan(String serviceName, String plan) {
        return new CreateServiceInstanceBindingRequest("serviceDefinitionId",getPlanIdFromServiceAndPlan(serviceName,plan),"appGuid",null);
    }

    private String getPlanIdFromServiceAndPlan(String serviceName, String plan){
        return UUID.nameUUIDFromBytes(Arrays.asList(serviceName, plan).toString().getBytes()).toString();
    }
}