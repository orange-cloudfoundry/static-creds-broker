package com.orange.servicebroker.staticcreds.service;

import com.orange.servicebroker.staticcreds.domain.AppServiceInstanceBinding;
import com.orange.servicebroker.staticcreds.domain.RouteServiceInstanceBinding;
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

    private CreateServiceInstanceBindingResponse toResponse(AppServiceInstanceBinding appServiceInstanceBinding) {
        return new CreateServiceInstanceAppBindingResponse()
                .withCredentials(appServiceInstanceBinding.getCredentials() == null || appServiceInstanceBinding.getCredentials().isEmpty() ? null : appServiceInstanceBinding.getCredentials())
                .withSyslogDrainUrl(appServiceInstanceBinding.getSyslogDrainUrl().orElse(null))
                .withVolumeMounts(Optional.ofNullable(appServiceInstanceBinding.getVolumeMounts()).filter(volumeMounts -> !volumeMounts.isEmpty()).map(volumeMounts -> volumeMounts.stream()
                        .map(volumeMount -> volumeMountMapper.map(volumeMount, VolumeMount.class))
                        .collect(Collectors.toList())).orElse(null));
    }

    private CreateServiceInstanceBindingResponse toResponse(RouteServiceInstanceBinding routeServiceInstanceBinding) {
        return new CreateServiceInstanceRouteBindingResponse()
                .withRouteServiceUrl(routeServiceInstanceBinding.getRouteServiceUrl());
    }

    @Override
    public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {
        LOGGER.debug("binding service instance");
        return servicePlanBindingRepository
                .find(request.getPlanId())
                .map(binding -> {
                    if (binding instanceof AppServiceInstanceBinding) {
                        return toResponse(AppServiceInstanceBinding.class.cast(binding));
                    } else {
                        return toResponse(RouteServiceInstanceBinding.class.cast(binding));
                    }
                })
                .orElse(new CreateServiceInstanceBindingResponse());
    }

    @Override
    public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest arg0) {
        LOGGER.debug("unbinding service instance");
    }
}