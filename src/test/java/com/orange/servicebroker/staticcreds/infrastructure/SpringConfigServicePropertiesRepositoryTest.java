package com.orange.servicebroker.staticcreds.infrastructure;

import com.orange.servicebroker.staticcreds.domain.ServiceProperties;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by YSBU7453 on 28/04/2016.
 */
public class SpringConfigServicePropertiesRepositoryTest {

    @Test
    public void should_find_all_services() throws Exception {
        //GIVEN
        SpringConfigServiceRepository repository = new SpringConfigServiceRepository(CatalogTestFactory.newInstance());
        //WHEN
        final List<ServiceProperties> serviceProperties = repository.findAll();
        //THEN
        Assertions.assertThat(serviceProperties).hasSize(2);
        //there should only be API_DIRECTORY_TEST_SERVICE and TRIPADVISOR_TEST_SERVICE
        Assertions.assertThat(serviceProperties.stream().map(ServiceProperties::getName).collect(Collectors.toList())).hasSize(2).containsOnly(CatalogTestFactory.API_DIRECTORY_TEST_SERVICE, CatalogTestFactory.TRIPADVISOR_TEST_SERVICE);
    }

}