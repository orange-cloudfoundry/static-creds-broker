package com.orange.servicebroker.staticcreds.infrastructure;

import com.orange.servicebroker.staticcreds.domain.CatalogSettings;
import com.orange.servicebroker.staticcreds.domain.Plan;
import com.orange.servicebroker.staticcreds.domain.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by YSBU7453 on 04/04/2016.
 */
@Component
public class SpringConfigPlanRepository implements PlanRepository {

    private CatalogSettings catalog;

    @Autowired
    public SpringConfigPlanRepository(CatalogSettings catalog) {
        this.catalog = catalog;
    }

    @Override
    public Optional<Plan> find(UUID servicePlanId) {
        return catalog.getServices().entrySet()
                .stream()
                .flatMap(serviceEntry -> serviceEntry.getValue().getPlans().entrySet().stream())
                .map(planEntry -> planEntry.getValue())
                .filter(plan -> servicePlanId.equals(plan.getId()))
                .findFirst();
    }
}
