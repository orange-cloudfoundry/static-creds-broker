package com.orange.servicebroker.staticcreds.infrastructure;

import com.orange.servicebroker.staticcreds.domain.ServiceBrokerProperties;
import com.orange.servicebroker.staticcreds.domain.ServiceProperties;
import com.orange.servicebroker.staticcreds.domain.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by YSBU7453 on 28/04/2016.
 */
@Component
public class SpringConfigServiceRepository implements ServiceRepository {

    private ServiceBrokerProperties serviceBrokerProperties;

    @Autowired
    public SpringConfigServiceRepository(ServiceBrokerProperties serviceBrokerProperties) {
        this.serviceBrokerProperties = serviceBrokerProperties;
    }


    @Override
    public List<ServiceProperties> findAll() {
        return serviceBrokerProperties.getServices().entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
