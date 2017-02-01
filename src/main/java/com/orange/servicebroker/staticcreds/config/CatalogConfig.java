package com.orange.servicebroker.staticcreds.config;

import com.orange.servicebroker.staticcreds.domain.ServiceProperties;
import com.orange.servicebroker.staticcreds.domain.ServiceRepository;
import com.orange.servicebroker.staticcreds.infrastructure.ServiceMapper;
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
        final List<ServiceProperties> serviceProperties = serviceRepository.findAll();
        final List<ServiceDefinition> serviceDefinitions = ServiceMapper.toServiceDefinitions(serviceProperties);
        return new Catalog(serviceDefinitions);
    }

}
