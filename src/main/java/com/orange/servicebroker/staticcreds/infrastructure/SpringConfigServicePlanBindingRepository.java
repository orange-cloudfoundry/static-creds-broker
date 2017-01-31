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

    private final CatalogSettings catalog;

    @Autowired
    public SpringConfigServicePlanBindingRepository(CatalogSettings catalogSettings) {
        this.catalog = catalogSettings;
    }


    @Override
    public Optional<ServicePlanBinding> find(String servicePlanId) {
        return catalog.getServices().values()
                .stream()
                .flatMap(service -> service.getPlans().values().stream()
                        .filter(plan -> servicePlanId.equals(plan.getId()))
                        .map(plan -> toServicePlanBinding(service, plan))
                )
                .findFirst();
    }

    private ServicePlanBinding toServicePlanBinding(Service service, Plan plan) {
        if (isVolumeMountService(service)) {
            return toVolumeServicePlanDetail(service, plan);
        } else {
            return toCredentialsServicePlanBinding(service, plan);
        }
    }

    private boolean isVolumeMountService(Service service) {
        return service.getRequires() == null ? Boolean.FALSE : service.getRequires().contains(ServiceDefinitionRequires.SERVICE_REQUIRES_VOLUME_MOUNT.toString());
    }

    private VolumeServicePlanBinding toVolumeServicePlanDetail(Service service, Plan plan) {
        final VolumeServicePlanBinding.VolumeServicePlanBindingBuilder builder = VolumeServicePlanBinding.builder();
        service.getVolumeMounts().forEach(builder::volumeMount);
        plan.getVolumeMounts().forEach(builder::volumeMount);

        return builder.build();
    }

    private CredentialsServicePlanBinding toCredentialsServicePlanBinding(Service service, Plan plan) {
        final CredentialsServicePlanBinding.CredentialsServicePlanBindingBuilder builder = CredentialsServicePlanBinding.builder();
        builder.syslogDrainUrl(Optional.ofNullable(plan.getSyslogDrainUrl()).map(Optional::of).orElse(Optional.ofNullable(service.getSyslogDrainUrl())));
        builder.dashboardUrl(Optional.ofNullable(plan.getDashboardUrl()).map(Optional::of).orElse(Optional.ofNullable(service.getDashboardUrl())));
        service.getFullCredentials().map(builder::credentials);
        plan.getFullCredentials().map(builder::credentials);

        return builder.build();
    }

}
