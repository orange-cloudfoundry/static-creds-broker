package com.orange.servicebroker.staticcreds.infrastructure;

import com.orange.servicebroker.staticcreds.domain.CatalogSettings;
import com.orange.servicebroker.staticcreds.domain.Plan;
import com.orange.servicebroker.staticcreds.domain.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YSBU7453 on 28/04/2016.
 */
public class CatalogTestFactory {

    public static final String SERVICE_PLAN_DEV = "dev";
    public static final String SERVICE_PLAN_PROD = "prod";
    public static final String SERVICE_PLAN_PREPROD = "preprod";
    public static final String SERVICE_PLAN_DEFAULT = "default";
    public static final String TRIPADVISOR_TEST_SERVICE = "TRIPADVISOR_test_Service";
    public static final String API_DIRECTORY_TEST_SERVICE = "API_DIRECTORY_test_Service";
    public static final String NO_ID = "no_id";
    public static final String HOSTNAME_PLAN_PROD_VALUE = "http://prod.company.com";

    public static CatalogSettings newInstance() {

        Plan dev = new Plan(SERVICE_PLAN_DEV);
        dev.setName(SERVICE_PLAN_DEV);
        Map<String, Object> credentialsDev = new HashMap<>();
        credentialsDev.put("URI", "http://mydev-api.org");
        credentialsDev.put("ACCESS_KEY", "dev");
        dev.setCredentials(credentialsDev);

        Plan prod = new Plan(SERVICE_PLAN_PROD);
        prod.setName(SERVICE_PLAN_PROD);
        Map<String, Object> credentialsProd = new HashMap<>();
        credentialsProd.put("URI", "http://myprod-api.org");
        credentialsProd.put("ACCESS_KEY", "prod");
        credentialsProd.put("HOSTNAME", HOSTNAME_PLAN_PROD_VALUE);
        prod.setCredentials(credentialsProd);

        //no id plan
        Plan preProd = new Plan();
        preProd.setName(SERVICE_PLAN_PREPROD);
        Map<String, Object> credentialsPreProd = new HashMap<>();
        credentialsPreProd.put("URI", "http://mypreprod-api.org");
        credentialsPreProd.put("ACCESS_KEY", "preprod");
        preProd.setCredentials(credentialsPreProd);

        Service apiDirectoryService = new Service();
        apiDirectoryService.setName(API_DIRECTORY_TEST_SERVICE);
        Map<String, Object> credentialsApiDirectoryService = new HashMap<>();
        credentialsApiDirectoryService.put("HOSTNAME", "http://company.com");
        apiDirectoryService.setCredentials(credentialsApiDirectoryService);
        final Map<String, Plan> apiDirectoryServicePlans = new HashMap<>();
        apiDirectoryServicePlans.put(SERVICE_PLAN_PROD, prod);
        apiDirectoryServicePlans.put(SERVICE_PLAN_PREPROD, preProd);
        apiDirectoryServicePlans.put(SERVICE_PLAN_DEV, dev);
        apiDirectoryService.setPlans(apiDirectoryServicePlans);

        Service tripAdvisorService = new Service();
        tripAdvisorService.setName(TRIPADVISOR_TEST_SERVICE);
        Map<String, Object> tripAdvisorServiceCredentials = new HashMap<>();
        tripAdvisorServiceCredentials.put("URI", "http://my-api.org");
        tripAdvisorService.setCredentials(tripAdvisorServiceCredentials);

        final Map<String, Service> services = new HashMap<>();
        services.put(API_DIRECTORY_TEST_SERVICE, apiDirectoryService);
        services.put(TRIPADVISOR_TEST_SERVICE, tripAdvisorService);

        return new CatalogSettings(services);
    }

}
