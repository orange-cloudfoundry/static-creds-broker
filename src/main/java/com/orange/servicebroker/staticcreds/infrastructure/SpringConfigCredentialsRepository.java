package com.orange.servicebroker.staticcreds.infrastructure;

import com.orange.servicebroker.staticcreds.domain.CatalogSettings;
import com.orange.servicebroker.staticcreds.domain.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by YSBU7453 on 03/05/2016.
 */
@Component
public class SpringConfigCredentialsRepository implements CredentialsRepository {

    private final CatalogSettings catalog;

    @Autowired
    public SpringConfigCredentialsRepository(CatalogSettings catalogSettings) {
        this.catalog = catalogSettings;
    }


    @Override
    public Optional<Map<String, Object>> findByPlan(String servicePlanId) {
        return catalog.getServices().values()
                .stream()
                .flatMap(service -> service.getPlans().values().stream()
                                                               .filter(plan -> servicePlanId.equals(plan.getId())).map(plan -> {
                                                                        final Map<String, Object> credentials = new HashMap<>();
                                                                        final Map<String, Object> serviceCredentials = service.getFullCredentials();
                                                                        final Map<String, Object> planCredentials = plan.getFullCredentials();

                                                                        if (serviceCredentials != null) {
                                                                            credentials.putAll(serviceCredentials);
                                                                        }
                                                                        if (planCredentials != null) {
                                                                            credentials.putAll(planCredentials);
                                                                        }

                                                                        return credentials;
                                                                    }))
                .findFirst();
    }

}
