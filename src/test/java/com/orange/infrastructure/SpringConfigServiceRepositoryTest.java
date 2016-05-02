package com.orange.infrastructure;

import com.orange.model.Service;
import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by YSBU7453 on 28/04/2016.
 */
public class SpringConfigServiceRepositoryTest {

    @Test
    public void should_find_all_services() throws Exception {
        //GIVEN
        SpringConfigServiceRepository repository = new SpringConfigServiceRepository(CatalogTestFactory.newInstance());
        //WHEN
        final List<Service> services = repository.findAll();
        //THEN
        Assertions.assertThat(services).hasSize(2);
        //there should only be API_DIRECTORY_TEST_SERVICE and TRIPADVISOR_TEST_SERVICE
        Assertions.assertThat(services.stream().map(Service::getName).collect(Collectors.toList())).hasSize(2).containsOnly(CatalogTestFactory.API_DIRECTORY_TEST_SERVICE,CatalogTestFactory.TRIPADVISOR_TEST_SERVICE);
    }

}