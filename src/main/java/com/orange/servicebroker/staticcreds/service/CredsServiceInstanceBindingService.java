package com.orange.servicebroker.staticcreds.service;

import com.orange.servicebroker.staticcreds.domain.CredentialsServicePlanBinding;
import com.orange.servicebroker.staticcreds.domain.ServicePlanBindingRepository;
import com.orange.servicebroker.staticcreds.infrastructure.VolumeMountMapper;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.model.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CredsServiceInstanceBindingService implements ServiceInstanceBindingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CredsServiceInstanceService.class);

    private final ServicePlanBindingRepository servicePlanBindingRepository;

    private final MapperFacade volumeMountMapper = new VolumeMountMapper();

    @Autowired
    public CredsServiceInstanceBindingService(final ServicePlanBindingRepository servicePlanBindingRepository) {
        this.servicePlanBindingRepository = servicePlanBindingRepository;
    }

    private CreateServiceInstanceBindingResponse toResponse(CredentialsServicePlanBinding credentialsServicePlanBinding) {
        return new CreateServiceInstanceAppBindingResponse()
                .withCredentials(credentialsServicePlanBinding.getCredentials() == null || credentialsServicePlanBinding.getCredentials().isEmpty() ? null : credentialsServicePlanBinding.getCredentials())
                .withSyslogDrainUrl(credentialsServicePlanBinding.getSyslogDrainUrl().orElse(null))
                .withVolumeMounts(Optional.ofNullable(credentialsServicePlanBinding.getVolumeMounts()).filter(volumeMounts -> !volumeMounts.isEmpty()).map(volumeMounts -> volumeMounts.stream()
                        .map(volumeMount -> volumeMountMapper.map(volumeMount, VolumeMount.class))
                        .collect(Collectors.toList())).orElse(null));
    }

    @Override
    public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {
        LOGGER.debug("binding service instance");
        return servicePlanBindingRepository
                .find(request.getPlanId())
                .map(CredentialsServicePlanBinding.class::cast)
                .map(this::toResponse)
                .orElse(new CreateServiceInstanceBindingResponse());
    }

    @Override
    public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest arg0) {
        LOGGER.debug("unbinding service instance");
    }
}