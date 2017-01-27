package com.orange.servicebroker.staticcreds.service;

import com.orange.servicebroker.staticcreds.domain.ServicePlanDetail;
import com.orange.servicebroker.staticcreds.domain.ServicePlanDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceAppBindingResponse;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CredsServiceInstanceBindingService implements ServiceInstanceBindingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CredsServiceInstanceService.class);

    private ServicePlanDetailRepository servicePlanDetailRepository;

    @Autowired
    public CredsServiceInstanceBindingService(ServicePlanDetailRepository servicePlanDetailRepository) {
        this.servicePlanDetailRepository = servicePlanDetailRepository;
    }

    private static CreateServiceInstanceBindingResponse toResponse(ServicePlanDetail servicePlanDetail) {
        return new CreateServiceInstanceAppBindingResponse()
                .withCredentials(servicePlanDetail.getCredentials())
                .withSyslogDrainUrl(servicePlanDetail.getSyslogDrainUrl().orElse(null));
    }

    @Override
    public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {
        LOGGER.debug("binding service instance");
        final Optional<ServicePlanDetail> planSummary = servicePlanDetailRepository.find(request.getPlanId());
        return planSummary
                .map(CredsServiceInstanceBindingService::toResponse)
                .orElse(new CreateServiceInstanceAppBindingResponse());
    }

    @Override
    public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest arg0) {
        LOGGER.debug("unbinding service instance");
    }
}