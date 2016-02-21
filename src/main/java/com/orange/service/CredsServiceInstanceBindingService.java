package com.orange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceAppBindingResponse;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;

import com.orange.model.Credentials;

@Service
public class CredsServiceInstanceBindingService implements ServiceInstanceBindingService{
	private Credentials credentials;

	@Autowired
	public CredsServiceInstanceBindingService(Credentials credentials) {
		this.credentials = credentials;
	}

	@Override
	public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest arg0) {
		return new CreateServiceInstanceAppBindingResponse().withCredentials(credentials.getCredentials());
	}

	@Override
	public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest arg0) {
	}

}
