package com.orange.servicebroker.staticcreds.service;

import org.springframework.cloud.servicebroker.model.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.stereotype.Service;

@Service
public class CredsServiceInstanceService implements ServiceInstanceService {
	@Override
	public CreateServiceInstanceResponse createServiceInstance(CreateServiceInstanceRequest arg0) {
		System.out.println("a service instance created.");
		return new CreateServiceInstanceResponse();
	}

	@Override
	public DeleteServiceInstanceResponse deleteServiceInstance(DeleteServiceInstanceRequest arg0) {
		System.out.println("a service instance deleted.");
		return new DeleteServiceInstanceResponse();
	}

	@Override
	public GetLastServiceOperationResponse getLastOperation(GetLastServiceOperationRequest arg0) {
		//TODO check id -> succeeded or failed
		return new GetLastServiceOperationResponse(OperationState.SUCCEEDED);
	}

	@Override
	public UpdateServiceInstanceResponse updateServiceInstance(UpdateServiceInstanceRequest arg0) {
		return new UpdateServiceInstanceResponse();
	}

}
