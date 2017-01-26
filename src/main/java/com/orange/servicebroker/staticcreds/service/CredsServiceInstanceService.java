package com.orange.servicebroker.staticcreds.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.servicebroker.model.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.stereotype.Service;

@Service
public class CredsServiceInstanceService implements ServiceInstanceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CredsServiceInstanceService.class);

	@Override
	public CreateServiceInstanceResponse createServiceInstance(CreateServiceInstanceRequest arg0) {
		LOGGER.debug("creating service instance");
		return new CreateServiceInstanceResponse();
	}

	@Override
	public DeleteServiceInstanceResponse deleteServiceInstance(DeleteServiceInstanceRequest arg0) {
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
