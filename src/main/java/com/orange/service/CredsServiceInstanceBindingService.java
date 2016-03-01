package com.orange.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

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
		String targetPlanGUID = request.getPlanId();
		for (Entry<List<String>,Map<String,Object>> entry : credentialsMap.getEntrySet()) {
			List<String> servicePlanName = entry.getKey();
			String service_name = servicePlanName.get(0);
			String plan_name = servicePlanName.get(1);
			String plan_guid = UUID.nameUUIDFromBytes(Arrays.asList(service_name, plan_name).toString().getBytes()).toString();
			if (plan_guid.equals(targetPlanGUID)) {
				return new CreateServiceInstanceBindingResponse(entry.getValue());
			}
		}
		return new CreateServiceInstanceBindingResponse();
	}

	@Override
	public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest arg0) {
	}
}