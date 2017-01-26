package com.orange.servicebroker.staticcreds.service;

import com.orange.servicebroker.staticcreds.domain.PlanSummary;
import com.orange.servicebroker.staticcreds.domain.PlanSummaryRepository;
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

    private PlanSummaryRepository planSummaryRepository;

    @Autowired
    public CredsServiceInstanceBindingService(PlanSummaryRepository planSummaryRepository) {
        this.planSummaryRepository = planSummaryRepository;
    }

    private static CreateServiceInstanceBindingResponse toResponse(PlanSummary planSummary) {
        return new CreateServiceInstanceAppBindingResponse()
                .withCredentials(planSummary.getCredentials())
                .withSyslogDrainUrl(planSummary.getSyslogDrainUrl().orElse(null));
    }

    @Override
    public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {
        LOGGER.debug("binding service instance");
        final Optional<PlanSummary> planSummary = planSummaryRepository.find(request.getPlanId());
        return planSummary
                .map(CredsServiceInstanceBindingService::toResponse)
                .orElse(new CreateServiceInstanceAppBindingResponse());
    }

    @Override
    public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest arg0) {
        LOGGER.debug("unbinding service instance");
    }
}