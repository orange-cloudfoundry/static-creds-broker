package com.orange.servicebroker.staticcreds.infrastructure;

import com.orange.servicebroker.staticcreds.domain.ServiceProperties;
import org.springframework.cloud.servicebroker.model.ServiceDefinition;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by YSBU7453 on 28/04/2016.
 */
public class ServiceMapper {

    public static ServiceDefinition toServiceDefinition(ServiceProperties serviceProperties) {
        return new ServiceDefinition(serviceProperties.getId().toString(),
                serviceProperties.getName(),
                serviceProperties.getDescription(),
                serviceProperties.getBindable(),
                serviceProperties.getPlanUpdateable(),
                PlanMapper.toServiceBrokerPlans(serviceProperties.getPlans().entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList())),
                serviceProperties.getTags(),
                serviceProperties.getMetadata() != null ? serviceProperties.getMetadata().asMap() : null,
                serviceProperties.getRequires(),
                null);
    }

    public static List<ServiceDefinition> toServiceDefinitions(List<ServiceProperties> serviceProperties) {
        return serviceProperties.stream()
                .map(ServiceMapper::toServiceDefinition)
                .collect(Collectors.toList());
    }
}
