package com.orange.servicebroker.staticcreds.infrastructure;

import com.orange.servicebroker.staticcreds.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.model.ServiceDefinitionRequires;
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
    public Optional<ServicePlanBinding> find(String servicePlanId) {
        return properties.getServices().values()
                .stream()
                .flatMap(service -> service.getPlans().values().stream()
                        .filter(plan -> servicePlanId.equals(plan.getId()))
                        .map(plan -> toServicePlanBinding(service, plan))
                )
                .findFirst();
    }

    private ServicePlanBinding toServicePlanBinding(ServiceProperties serviceProperties, PlanProperties planProperties) {
        if (isVolumeMountService(serviceProperties)) {
            return toVolumeServicePlanDetail(serviceProperties, planProperties);
        } else {
            return toCredentialsServicePlanBinding(serviceProperties, planProperties);
        }
    }

    private boolean isVolumeMountService(ServiceProperties serviceProperties) {
        return serviceProperties.getRequires() == null ? Boolean.FALSE : serviceProperties.getRequires().contains(ServiceDefinitionRequires.SERVICE_REQUIRES_VOLUME_MOUNT.toString());
    }

    private VolumeServicePlanBinding toVolumeServicePlanDetail(ServiceProperties serviceProperties, PlanProperties planProperties) {
        final VolumeServicePlanBinding.VolumeServicePlanBindingBuilder builder = VolumeServicePlanBinding.builder();
        serviceProperties.getVolumeMounts().forEach(builder::volumeMount);
        planProperties.getVolumeMounts().forEach(builder::volumeMount);

        return builder.build();
    }

    private CredentialsServicePlanBinding toCredentialsServicePlanBinding(ServiceProperties serviceProperties, PlanProperties planProperties) {
        final CredentialsServicePlanBinding.CredentialsServicePlanBindingBuilder builder = CredentialsServicePlanBinding.builder();
        builder.syslogDrainUrl(Optional.ofNullable(planProperties.getSyslogDrainUrl()).map(Optional::of).orElse(Optional.ofNullable(serviceProperties.getSyslogDrainUrl())));
        builder.dashboardUrl(Optional.ofNullable(planProperties.getDashboardUrl()).map(Optional::of).orElse(Optional.ofNullable(serviceProperties.getDashboardUrl())));
        serviceProperties.getFullCredentials().map(builder::credentials);
        planProperties.getFullCredentials().map(builder::credentials);

        return builder.build();
    }

}
