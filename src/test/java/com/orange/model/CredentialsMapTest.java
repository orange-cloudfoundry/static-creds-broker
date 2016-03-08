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

package com.orange.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by sbortolussi on 08/03/2016.
 */
public class CredentialsMapTest {

    public static final String API_DIRECTORY_SERVICE = "API_DIRECTORY";
    public static final String DEV_PLAN = "dev";
    public static final String PROD_PLAN = "prod";
    public static final String DUMMY_PLAN = "dummy";

    @Test
    public void should_get_credentials_that_have_been_set_for_associated_service_plan() throws Exception {
        CredentialsMap credentialsMap = new CredentialsMap();
        //given credentials have been set for dev plan of service API_DIRECTORY
        credentialsMap.addCredential(API_DIRECTORY_SERVICE, DEV_PLAN,"CREDENTIALS_URI","http://mydev-api.org");
        credentialsMap.addCredential(API_DIRECTORY_SERVICE,DEV_PLAN,"CREDENTIALS_ACCESS_KEY","devAZERTY");
        //given credentials have been set for prod plan of service API_DIRECTORY
        credentialsMap.addCredential(API_DIRECTORY_SERVICE, PROD_PLAN,"CREDENTIALS_URI","http://myprod-api.org");
        credentialsMap.addCredential(API_DIRECTORY_SERVICE, PROD_PLAN,"CREDENTIALS_ACCESS_KEY","prodAZERTY");

        //when I get credentials that have been set for a service API_DIRECTORY instance whose plan is dev
        final Map<String, Object> credentialsForPlan = credentialsMap.getCredentialsForPlan(getPlanIdFromServiceAndPlan(API_DIRECTORY_SERVICE, DEV_PLAN));

        //then I should only get credentials that have been set for dev plan of service API_DIRECTORY
        Assert.assertNotNull(credentialsForPlan);
        Assert.assertEquals(2,credentialsForPlan.size());
        Assert.assertEquals("http://mydev-api.org",credentialsForPlan.get("CREDENTIALS_URI"));
        Assert.assertEquals("devAZERTY",credentialsForPlan.get("CREDENTIALS_ACCESS_KEY"));

    }

    @Test
    public void should_get_no_credentials_if_no_credentials_have_been_set_for_associated_service_plan() throws Exception {
        CredentialsMap credentialsMap = new CredentialsMap();
        //given credentials have been set for dev plan of service API_DIRECTORY
        credentialsMap.addCredential(API_DIRECTORY_SERVICE, DEV_PLAN,"CREDENTIALS_URI","http://mydev-api.org");
        credentialsMap.addCredential(API_DIRECTORY_SERVICE,DEV_PLAN,"CREDENTIALS_ACCESS_KEY","devAZERTY");
        //given credentials have been set for prod plan of service API_DIRECTORY
        credentialsMap.addCredential(API_DIRECTORY_SERVICE, PROD_PLAN,"CREDENTIALS_URI","http://myprod-api.org");
        credentialsMap.addCredential(API_DIRECTORY_SERVICE, PROD_PLAN,"CREDENTIALS_ACCESS_KEY","prodAZERTY");

        //when I get credentials that have been set for a service API_DIRECTORY instance whose plan is dummy
        final Map<String, Object> credentialsForPlan = credentialsMap.getCredentialsForPlan(getPlanIdFromServiceAndPlan(API_DIRECTORY_SERVICE, DUMMY_PLAN));

        //then I should get no credentials
        Assert.assertNull(credentialsForPlan);

    }
    private String getPlanIdFromServiceAndPlan(String serviceName, String plan){
        return UUID.nameUUIDFromBytes(Arrays.asList(serviceName, plan).toString().getBytes()).toString();
    }
}