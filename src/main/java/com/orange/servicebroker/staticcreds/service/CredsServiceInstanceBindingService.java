package com.orange.servicebroker.staticcreds.service;

import com.orange.servicebroker.staticcreds.domain.CredentialsServicePlanBinding;
import com.orange.servicebroker.staticcreds.domain.ServicePlanBinding;
import com.orange.servicebroker.staticcreds.domain.ServicePlanBindingRepository;
import com.orange.servicebroker.staticcreds.domain.VolumeServicePlanBinding;
import com.orange.servicebroker.staticcreds.infrastructure.VolumeMountMapper;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.model.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    private static CreateServiceInstanceBindingResponse toResponse(CredentialsServicePlanBinding credentialsServicePlanBinding) {
        return new CreateServiceInstanceAppBindingResponse()
                .withCredentials(credentialsServicePlanBinding.getCredentials())
                .withSyslogDrainUrl(credentialsServicePlanBinding.getSyslogDrainUrl().orElse(null));
    }

    private CreateServiceInstanceBindingResponse toResponse(VolumeServicePlanBinding volumeServicePlanDetail) {
        return new CreateServiceInstanceVolumeBindingResponse()
                .withVolumeMounts(volumeServicePlanDetail.getVolumeMounts().stream()
                        .map(volumeMount -> volumeMountMapper.map(volumeMount, VolumeMount.class))
                        .collect(Collectors.toList()));
    }

    @Override
    public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {
        LOGGER.debug("binding service instance");
        final Optional<ServicePlanBinding> servicePlanBinding = servicePlanBindingRepository.find(request.getPlanId());

        final Optional<CreateServiceInstanceBindingResponse> createServiceInstanceVolumeBindingResponse = servicePlanBinding
                .filter(VolumeServicePlanBinding.class::isInstance)
                .map(VolumeServicePlanBinding.class::cast)
                .map(this::toResponse);

        final Optional<CreateServiceInstanceBindingResponse> createServiceInstanceAppBindingResponse = servicePlanBinding
                .filter(CredentialsServicePlanBinding.class::isInstance)
                .map(CredentialsServicePlanBinding.class::cast)
                .map(CredsServiceInstanceBindingService::toResponse);

        return Arrays.asList(createServiceInstanceVolumeBindingResponse, createServiceInstanceAppBindingResponse).stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst().orElse(new CreateServiceInstanceBindingResponse());
    }

    @Override
    public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest arg0) {
        LOGGER.debug("unbinding service instance");
    }
}