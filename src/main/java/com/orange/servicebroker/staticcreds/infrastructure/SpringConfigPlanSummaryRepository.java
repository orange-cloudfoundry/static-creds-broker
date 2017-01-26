package com.orange.servicebroker.staticcreds.infrastructure;

import com.orange.servicebroker.staticcreds.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by YSBU7453 on 03/05/2016.
 */
@Component
public class SpringConfigPlanSummaryRepository implements PlanSummaryRepository {

    private final CatalogSettings catalog;

    @Autowired
    public SpringConfigPlanSummaryRepository(CatalogSettings catalogSettings) {
        this.catalog = catalogSettings;
    }


    @Override
    public Optional<PlanSummary> find(String servicePlanId) {
        return catalog.getServices().values()
                .stream()
                .flatMap(service -> service.getPlans().values().stream()
                        .filter(plan -> servicePlanId.equals(plan.getId()))
                        .map(plan -> toPlanSummary(service, plan))
                )
                .findFirst();
    }

    private PlanSummary toPlanSummary(Service service, Plan plan) {
        final PlanSummary.PlanSummaryBuilder builder = PlanSummary.builder();

        builder.syslogDrainUrl(plan.getSyslogDrainUrl() != null ? Optional.of(plan.getSyslogDrainUrl()) : Optional.ofNullable(service.getSyslogDrainUrl()));
        service.getFullCredentials().map(builder::credentials);
        plan.getFullCredentials().forEach(builder::credential);

        return builder.build();
    }

}
