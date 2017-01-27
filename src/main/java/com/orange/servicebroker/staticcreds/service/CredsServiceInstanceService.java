package com.orange.servicebroker.staticcreds.service;

import com.orange.servicebroker.staticcreds.domain.ServicePlanDetail;
import com.orange.servicebroker.staticcreds.domain.ServicePlanDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.servicebroker.model.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.stereotype.Service;

@Service
public class CredsServiceInstanceService implements ServiceInstanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CredsServiceInstanceService.class);

    private final ServicePlanDetailRepository repository;

    public CredsServiceInstanceService(ServicePlanDetailRepository repository) {
        this.repository = repository;
    }

    private static CreateServiceInstanceResponse toResponse(ServicePlanDetail servicePlanDetail) {
        return new CreateServiceInstanceResponse()
                .withDashboardUrl(servicePlanDetail.getDashboardUrl().orElse(null));
    }

    @Override
    public CreateServiceInstanceResponse createServiceInstance(CreateServiceInstanceRequest request) {
        LOGGER.debug("creating service instance");
        return repository.find(request.getPlanId())
                .map(CredsServiceInstanceService::toResponse)
                .orElse(new CreateServiceInstanceResponse());
    }

    @Override
    public DeleteServiceInstanceResponse deleteServiceInstance(DeleteServiceInstanceRequest request) {
        LOGGER.debug("deleting service instance");
        return new DeleteServiceInstanceResponse();
    }

    @Override
    public GetLastServiceOperationResponse getLastOperation(GetLastServiceOperationRequest arg0) {
        //TODO check id -> succeeded or failed
        return new GetLastServiceOperationResponse().withOperationState(OperationState.SUCCEEDED);
    }

    @Override
    public UpdateServiceInstanceResponse updateServiceInstance(UpdateServiceInstanceRequest arg0) {
        LOGGER.debug("updating service instance");
        return new UpdateServiceInstanceResponse();
    }
}
