package com.orange.config;

import com.orange.infrastructure.ServiceMapper;
import com.orange.model.Service;
import com.orange.model.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.model.Catalog;
import org.springframework.cloud.servicebroker.model.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CatalogConfig {

    @Autowired
    ServiceRepository serviceRepository;

    @Bean
    public Catalog catalog() {
        final List<Service> services = serviceRepository.findAll();
        final List<ServiceDefinition> serviceDefinitions = ServiceMapper.toServiceDefinitions(services);
        Catalog catalog = new Catalog(serviceDefinitions);
        return catalog;
    }

}
