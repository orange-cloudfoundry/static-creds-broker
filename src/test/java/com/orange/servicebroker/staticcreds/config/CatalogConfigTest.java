package com.orange.servicebroker.staticcreds.config;

import com.orange.servicebroker.staticcreds.domain.CatalogSettings;
import com.orange.servicebroker.staticcreds.domain.Plan;
import com.orange.servicebroker.staticcreds.domain.Service;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by YSBU7453 on 26/04/2016.
 */
public class CatalogConfigTest {

    public static final UUID SERVICE_PLAN_DEV_ID = UUID.randomUUID();
    public static final UUID SERVICE_PLAN_PROD_ID = UUID.randomUUID();

    public static final String API_DIRECTORY_SERVICE = "API_DIRECTORY";
    public static final String DEV_PLAN = "dev";
    public static final String PROD_PLAN = "prod";

    @Test
    public void should_get_catalog_from_config() throws Exception {

        CatalogConfig catalogConfig = new CatalogConfig();
    }

    private CatalogSettings catalog() {
        Plan dev = new Plan(SERVICE_PLAN_DEV_ID);
        Map<String,Object> credentialsDev = new HashMap<>();
        credentialsDev.put("URI","http://mydev-api.org");
        credentialsDev.put("ACCESS_KEY","devAZERTY");
        dev.setCredentials(credentialsDev);

        Plan prod = new Plan(SERVICE_PLAN_PROD_ID);
        Map<String,Object> credentialsProd = new HashMap<>();
        credentialsProd.put("URI","http://myprod-api.org");
        credentialsProd.put("ACCESS_KEY","prodAZERTY");
        prod.setCredentials(credentialsProd);

        Service apiDirectoryService = new Service();
        apiDirectoryService.setName(API_DIRECTORY_SERVICE);
        final Map<String, Plan> apiDirectoryServicePlans = new HashMap<>();
        apiDirectoryServicePlans.put(DEV_PLAN,dev);
        apiDirectoryServicePlans.put(PROD_PLAN,prod);
        apiDirectoryService.setPlans(apiDirectoryServicePlans);

        final Map<String, Service> services = new HashMap<>();
        services.put(API_DIRECTORY_SERVICE,apiDirectoryService);

        return new CatalogSettings(services);
    }

}