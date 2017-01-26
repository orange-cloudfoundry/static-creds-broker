package com.orange.servicebroker.staticcreds.infrastructure;

import com.orange.servicebroker.staticcreds.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by YSBU7453 on 03/05/2016.
 */
@Component
public class SpringConfigServicePlanDetailRepository implements ServicePlanDetailRepository {

    private final CatalogSettings catalog;

    @Autowired
    public SpringConfigServicePlanDetailRepository(CatalogSettings catalogSettings) {
        this.catalog = catalogSettings;
    }


    @Override
    public Optional<ServicePlanDetail> find(String servicePlanId) {
        return catalog.getServices().values()
                .stream()
                .flatMap(service -> service.getPlans().values().stream()
                        .filter(plan -> servicePlanId.equals(plan.getId()))
                        .map(plan -> toServicePlanDetail(service, plan))
                )
                .findFirst();
    }

    private ServicePlanDetail toServicePlanDetail(Service service, Plan plan) {
        final ServicePlanDetail.ServicePlanDetailBuilder builder = ServicePlanDetail.builder();
        builder.syslogDrainUrl(Optional.ofNullable(plan.getSyslogDrainUrl()).map(Optional::of).orElse(Optional.ofNullable(service.getSyslogDrainUrl())));
        builder.dashboardUrl(Optional.ofNullable(plan.getDashboardUrl()).map(Optional::of).orElse(Optional.ofNullable(service.getDashboardUrl())));
        service.getFullCredentials().map(builder::credentials);
        plan.getFullCredentials().forEach(builder::credential);

        return builder.build();
    }

}
