package com.orange.servicebroker.staticcreds.infrastructure;

import com.orange.servicebroker.staticcreds.domain.PlanProperties;
import com.orange.servicebroker.staticcreds.domain.ServiceBrokerProperties;
import com.orange.servicebroker.staticcreds.domain.ServiceProperties;

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

    public static ServiceBrokerProperties newInstance() {

        PlanProperties dev = new PlanProperties(SERVICE_PLAN_DEV);
        dev.setName(SERVICE_PLAN_DEV);
        Map<String, Object> credentialsDev = new HashMap<>();
        credentialsDev.put("URI", "http://mydev-api.org");
        credentialsDev.put("ACCESS_KEY", "dev");
        dev.setCredentials(credentialsDev);

        PlanProperties prod = new PlanProperties(SERVICE_PLAN_PROD);
        prod.setName(SERVICE_PLAN_PROD);
        Map<String, Object> credentialsProd = new HashMap<>();
        credentialsProd.put("URI", "http://myprod-api.org");
        credentialsProd.put("ACCESS_KEY", "prod");
        credentialsProd.put("HOSTNAME", HOSTNAME_PLAN_PROD_VALUE);
        prod.setCredentials(credentialsProd);

        //no id plan
        PlanProperties preProd = new PlanProperties();
        preProd.setName(SERVICE_PLAN_PREPROD);
        Map<String, Object> credentialsPreProd = new HashMap<>();
        credentialsPreProd.put("URI", "http://mypreprod-api.org");
        credentialsPreProd.put("ACCESS_KEY", "preprod");
        preProd.setCredentials(credentialsPreProd);

        ServiceProperties apiDirectoryServiceProperties = new ServiceProperties();
        apiDirectoryServiceProperties.setName(API_DIRECTORY_TEST_SERVICE);
        Map<String, Object> credentialsApiDirectoryService = new HashMap<>();
        credentialsApiDirectoryService.put("HOSTNAME", "http://company.com");
        apiDirectoryServiceProperties.setCredentials(credentialsApiDirectoryService);
        final Map<String, PlanProperties> apiDirectoryServicePlans = new HashMap<>();
        apiDirectoryServicePlans.put(SERVICE_PLAN_PROD, prod);
        apiDirectoryServicePlans.put(SERVICE_PLAN_PREPROD, preProd);
        apiDirectoryServicePlans.put(SERVICE_PLAN_DEV, dev);
        apiDirectoryServiceProperties.setPlans(apiDirectoryServicePlans);

        ServiceProperties tripAdvisorServiceProperties = new ServiceProperties();
        tripAdvisorServiceProperties.setName(TRIPADVISOR_TEST_SERVICE);
        Map<String, Object> tripAdvisorServiceCredentials = new HashMap<>();
        tripAdvisorServiceCredentials.put("URI", "http://my-api.org");
        tripAdvisorServiceProperties.setCredentials(tripAdvisorServiceCredentials);

        final Map<String, ServiceProperties> services = new HashMap<>();
        services.put(API_DIRECTORY_TEST_SERVICE, apiDirectoryServiceProperties);
        services.put(TRIPADVISOR_TEST_SERVICE, tripAdvisorServiceProperties);

        return new ServiceBrokerProperties(services);
    }

}
