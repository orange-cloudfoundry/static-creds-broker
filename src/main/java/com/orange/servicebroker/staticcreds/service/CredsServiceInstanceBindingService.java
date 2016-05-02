package com.orange.servicebroker.staticcreds.service;

import com.orange.servicebroker.staticcreds.domain.Plan;
import com.orange.servicebroker.staticcreds.domain.PlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CredsServiceInstanceBindingService implements ServiceInstanceBindingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CredsServiceInstanceService.class);

    private PlanRepository planRepository;

    @Autowired
    public CredsServiceInstanceBindingService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {
        LOGGER.debug("binding service instance");
        UUID planId = UUID.fromString(request.getPlanId());
        final Optional<Plan> plan = planRepository.find(planId);
        return new CreateServiceInstanceBindingResponse(plan.map(Plan::getFullCredentials).orElse(null));
    }

    @Override
    public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest arg0) {
        LOGGER.debug("unbinding service instance");
    }
}