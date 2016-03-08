package com.orange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;

import com.orange.model.CredentialsMap;

@Service
public class CredsServiceInstanceBindingService implements ServiceInstanceBindingService {
	private CredentialsMap credentialsMap;

	@Autowired
	public CredsServiceInstanceBindingService(CredentialsMap credentialsMap) {
		this.credentialsMap = credentialsMap;
	}

	@Override
	public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {
		String planId = request.getPlanId();
		return new CreateServiceInstanceBindingResponse(credentialsMap.getCredentialsForPlan(planId));
	}

	@Override
	public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest arg0) {
	}
}