package com.orange.config;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.orange.infrastructure.ServiceMapper;
import com.orange.model.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.model.Catalog;
import org.springframework.cloud.servicebroker.model.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

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
