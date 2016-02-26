package com.orange.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;

import com.orange.model.CredentialsMap;

@Service
public class CredsServiceInstanceBindingService implements ServiceInstanceBindingService{
	private CredentialsMap credentialsMap;

	@Autowired
	public CredsServiceInstanceBindingService(CredentialsMap credentialsMap) {
		this.credentialsMap = credentialsMap;
	}

	@Override
	public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {
		String serviceGUID = request.getServiceDefinitionId();
		for (String service_id : credentialsMap.getServiceIds()) {
			String service_name = getServiceName(service_id);
			String guid = UUID.nameUUIDFromBytes(service_name.getBytes()).toString();
			if (guid.equals(serviceGUID)) {
				return new CreateServiceInstanceBindingResponse(credentialsMap.getCredentials(service_id));
			}
		}
		return new CreateServiceInstanceBindingResponse();
	}

	@Override
	public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest arg0) {
	}
	
	private String getServiceName(String service_id){
		String envConfigName = "SERVICES_" + service_id + "_NAME";
		return System.getenv().get(envConfigName);
	}

}
