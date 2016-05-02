package com.orange.servicebroker.staticcreds.service;

import com.orange.servicebroker.staticcreds.domain.Plan;
import com.orange.servicebroker.staticcreds.domain.PlanRepository;
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
    private PlanRepository planRepository;

    @Autowired
    public CredsServiceInstanceBindingService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {
        UUID planId = UUID.fromString(request.getPlanId());
        final Optional<Plan> plan = planRepository.find(planId);
        return new CreateServiceInstanceBindingResponse(plan.map(Plan::getFullCredentials).orElse(null));
    }

    @Override
    public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest arg0) {
    }
}