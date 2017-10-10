package com.orange.servicebroker.staticcreds.infrastructure;

import com.orange.servicebroker.staticcreds.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by YSBU7453 on 03/05/2016.
 */
@Component
public class SpringConfigServicePlanBindingRepository implements ServicePlanBindingRepository {

    private final ServiceBrokerProperties properties;

    @Autowired
    public SpringConfigServicePlanBindingRepository(ServiceBrokerProperties serviceBrokerProperties) {
        this.properties = serviceBrokerProperties;
    }


    @Override
    public Optional<ServiceInstanceBinding> find(String servicePlanId) {
        return properties.getServices().values()
                .stream()
                .flatMap(service -> service.getPlans().values().stream()
                        .filter(plan -> servicePlanId.equals(plan.getId()))
                        .map(plan -> toServicePlanBinding(service, plan))
                )
                .findFirst();
    }

    private ServiceInstanceBinding toServicePlanBinding(ServiceProperties serviceProperties, PlanProperties planProperties) {
        Optional<String> routeServiceUrl = getRouteServiceUrl(serviceProperties, planProperties);
        if (routeServiceUrl.isPresent()) {
            return RouteServiceInstanceBinding.builder().routeServiceUrl(routeServiceUrl.get()).build();
        } else {
            AppServiceInstanceBinding.AppServiceInstanceBindingBuilder builder = AppServiceInstanceBinding.builder();
            builder.syslogDrainUrl(getSyslogDrainUrl(serviceProperties, planProperties));
            builder.dashboardUrl(getDashboardUrl(serviceProperties, planProperties));
            serviceProperties.getFullCredentials().map(builder::credentials);
            planProperties.getFullCredentials().map(builder::credentials);
            Optional.ofNullable(serviceProperties.getVolumeMounts()).ifPresent(volumeMountProperties -> volumeMountProperties.stream().forEach(builder::volumeMount));
            Optional.ofNullable(planProperties.getVolumeMounts()).ifPresent(volumeMountProperties -> volumeMountProperties.stream().forEach(builder::volumeMount));
            return builder.build();
        }
    }

    private Optional<String> getRouteServiceUrl(ServiceProperties serviceProperties, PlanProperties planProperties) {
        return Optional.ofNullable(planProperties.getRouteServiceUrl()).map(Optional::of).orElse(Optional.ofNullable(serviceProperties.getRouteServiceUrl()));
    }

    private Optional<String> getSyslogDrainUrl(ServiceProperties serviceProperties, PlanProperties planProperties) {
        return Optional.ofNullable(planProperties.getSyslogDrainUrl()).map(Optional::of).orElse(Optional.ofNullable(serviceProperties.getSyslogDrainUrl()));
    }

    private Optional<String> getDashboardUrl(ServiceProperties serviceProperties, PlanProperties planProperties) {
        return Optional.ofNullable(planProperties.getDashboardUrl()).map(Optional::of).orElse(Optional.ofNullable(serviceProperties.getDashboardUrl()));
    }

}
