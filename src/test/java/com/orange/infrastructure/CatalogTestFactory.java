package com.orange.infrastructure;

import com.orange.model.CatalogSettings;
import com.orange.model.Plan;
import com.orange.model.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by YSBU7453 on 28/04/2016.
 */
public class CatalogTestFactory {

    public static final UUID SERVICE_PLAN_DEV = UUID.randomUUID();
    public static final UUID SERVICE_PLAN_PROD = UUID.randomUUID();
    public static final UUID SERVICE_PLAN_PREPROD = UUID.randomUUID();
    public static final UUID SERVICE_PLAN_DEFAULT = UUID.randomUUID();
    public static final String TRIPADVISOR_TEST_SERVICE = "TRIPADVISOR_test_Service";
    public static final String API_DIRECTORY_TEST_SERVICE = "API_DIRECTORY_test_Service";

    public static CatalogSettings newInstance() {

        Plan dev = new Plan(SERVICE_PLAN_DEV);
        dev.setName("dev");
        Map<String, Object> credentialsDev = new HashMap<>();
        credentialsDev.put("URI", "http://mydev-api.org");
        credentialsDev.put("ACCESS_KEY", "dev");
        dev.setCredentials(credentialsDev);

        Plan prod = new Plan(SERVICE_PLAN_PROD);
        prod.setName("prod");
        Map<String, Object> credentialsProd = new HashMap<>();
        credentialsProd.put("URI", "http://myprod-api.org");
        credentialsProd.put("ACCESS_KEY", "prod");
        prod.setCredentials(credentialsProd);

        Plan preProd = new Plan(SERVICE_PLAN_PREPROD);
        preProd.setName("preprod");
        Map<String, Object> credentialsPreProd = new HashMap<>();
        credentialsPreProd.put("URI", "http://mypreprod-api.org");
        credentialsPreProd.put("ACCESS_KEY", "preprod");
        preProd.setCredentials(credentialsPreProd);

        Service apiDirectoryService = new Service();
        apiDirectoryService.setName(API_DIRECTORY_TEST_SERVICE);
        final Map<String, Plan> apiDirectoryServicePlans = new HashMap<>();
        apiDirectoryServicePlans.put("prod", prod);
        apiDirectoryServicePlans.put("preProd", preProd);
        apiDirectoryServicePlans.put("dev", dev);
        apiDirectoryService.setPlans(apiDirectoryServicePlans);

        Plan defaultTripAdvisorServicePlan = new Plan(SERVICE_PLAN_DEFAULT);
        defaultTripAdvisorServicePlan.setName("default");
        Map<String, Object> credentialsDefaulTripAdvisorServicePlan = new HashMap<>();
        credentialsDefaulTripAdvisorServicePlan.put("URI", "http://my-api.org");
        defaultTripAdvisorServicePlan.setCredentials(credentialsDefaulTripAdvisorServicePlan);

        Service tripAdvisorService = new Service();
        tripAdvisorService.setName(TRIPADVISOR_TEST_SERVICE);
        final Map<String, Plan> tripAdvisorServicePlans = new HashMap<>();
        apiDirectoryServicePlans.put("default", defaultTripAdvisorServicePlan);

        final Map<String, Service> services = new HashMap<>();
        services.put("API_DIRECTORY", apiDirectoryService);
        services.put("TRIPADVISOR", tripAdvisorService);
        return new CatalogSettings(services);
    }

}
