package com.orange.servicebroker.staticcreds.infrastructure;

import com.orange.servicebroker.staticcreds.domain.Service;
import org.springframework.cloud.servicebroker.model.ServiceDefinition;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by YSBU7453 on 28/04/2016.
 */
public class ServiceMapper {

    public static ServiceDefinition toServiceDefinition(Service service) {
        //TODO service.getName().toString()
        return new ServiceDefinition(service.getId( ).toString(),
                service.getName(),
                service.getDescription(),
                service.isBindable(),
                service.isPlanUpdateable(),
                PlanMapper.toServiceBrokerPlans(service.getPlans().entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList())),
                service.getTags(),
                service.getMetadata() != null ? service.getMetadata().asMap() : null,
                null,
                null);
    }

    public static List<ServiceDefinition> toServiceDefinitions(List<Service> services) {
        return services.stream()
                       .map(ServiceMapper::toServiceDefinition)
                       .collect(Collectors.toList());
    }
}
