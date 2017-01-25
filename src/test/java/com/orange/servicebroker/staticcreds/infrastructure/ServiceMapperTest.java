package com.orange.servicebroker.staticcreds.infrastructure;

import com.orange.servicebroker.staticcreds.domain.Service;
import com.orange.servicebroker.staticcreds.domain.ServiceMetadata;
import org.junit.Test;
import org.springframework.cloud.servicebroker.model.ServiceDefinition;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

/**
 * Created by YSBU7453 on 28/04/2016.
 */
public class ServiceMapperTest {

    public static final String SERVICE_ID = "a service id";
    public static final String SERVICE_DESCRIPTION = "a description";
    public static final String SERVICE_NAME = "a service name";

    @Test
    public void map_service_id() {
        //GIVEN
        Service service = new Service(SERVICE_ID);
        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(service);
        //THEN
        assertThat(res.getId()).as("toServiceDefinition service id").isEqualTo(SERVICE_ID.toString());
    }

    @Test
    public void map_service_description() {
        //GIVEN
        Service service = new Service(SERVICE_ID);
        service.setDescription(SERVICE_DESCRIPTION);
        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(service);
        //THEN
        assertThat(res.getDescription()).as("toServiceDefinition service description").isEqualTo(SERVICE_DESCRIPTION);
    }

    @Test
    public void map_service_name() {
        //GIVEN
        Service service = new Service(SERVICE_ID);
        service.setName(SERVICE_NAME);
        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(service);
        //THEN
        assertThat(res.getName()).as("toServiceDefinition service name").isEqualTo(SERVICE_NAME);
    }

    @Test
    public void map_service_bindable() {
        //GIVEN
        Service service = new Service(SERVICE_ID);
        service.setBindable(Boolean.FALSE);
        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(service);
        //THEN
        assertThat(res.isBindable()).as("toServiceDefinition service bindable").isFalse();
    }

    @Test
    public void map_service_requires() {
        //GIVEN
        Service service = new Service(SERVICE_ID);
        service.setRequires(Arrays.asList("syslog_drain"));
        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(service);
        //THEN
        assertThat(res.getRequires()).as("toServiceDefinition service requires")
                .containsExactly("syslog_drain");
    }

    @Test
    public void map_service_plan_updateable() {
        //GIVEN
        Service service = new Service(SERVICE_ID);
        service.setPlanUpdateable(Boolean.FALSE);
        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(service);
        //THEN
        assertThat(res.isPlanUpdateable()).as("toServiceDefinition service plan updateable").isFalse();
    }

    @Test
    public void map_service_metadata() {
        //GIVEN
        ServiceMetadata serviceMetadata = new ServiceMetadata();
        serviceMetadata.setDisplayName("aDisplayName");
        serviceMetadata.setLongDescription("a long description");
        serviceMetadata.setProviderDisplayName("a provider");
        serviceMetadata.setDocumentationUrl("http://localhost/doc");
        serviceMetadata.setImageUrl("http://localhost/image.png");
        serviceMetadata.setSupportUrl("http://localhost/support");

        Service service = new Service(SERVICE_ID);
        service.setMetadata(serviceMetadata);

        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(service);
        //THEN
        assertThat(res.getMetadata()).as("toServiceDefinition service metadata").hasSize(6).includes(
                entry("displayName", "aDisplayName"),
                entry("longDescription", "a long description"),
                entry("providerDisplayName", "a provider"),
                entry("documentationUrl", "http://localhost/doc"),
                entry("imageUrl", "http://localhost/image.png"),
                entry("supportUrl", "http://localhost/support"));
    }

    @Test
    public void map_service_tags() {
        //GIVEN
        Service service = new Service(SERVICE_ID);
        service.setTags(Stream.of("tag1", "tag2", "tag3").collect(Collectors.toList()));

        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(service);
        //THEN
        assertThat(res.getTags()).as("toServiceDefinition service tags").containsOnly("tag1", "tag2", "tag3");
    }

    @Test
    public void does_not_map_requires() {
        //GIVEN
        Service service = new Service(SERVICE_ID);
        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(service);
        //THEN
        assertThat(res.getRequires()).as("does not toServiceDefinition requires").isNull();
    }

    @Test
    public void does_not_map_dashboard_client() {
        //GIVEN
        Service service = new Service(SERVICE_ID);
        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(service);
        //THEN
        assertThat(res.getDashboardClient()).as("does not toServiceDefinition dashboard client").isNull();
    }

    public void should_map_services() {
        //GIVEN
        final String aServiceId = "a service id";
        Service aService = new Service(aServiceId);
        final String anotherServiceId = "another service id";
        Service anotherService = new Service(anotherServiceId);
        //WHEN
        final List<ServiceDefinition> serviceDefinitions = ServiceMapper.toServiceDefinitions(Arrays.asList(aService, anotherService));
        //THEN
        assertThat(serviceDefinitions).as("map services to service definitions").hasSize(2);
        assertThat(serviceDefinitions.stream().map(ServiceDefinition::getId).collect(Collectors.toList())).as("map services to service definitions").containsOnly(aServiceId, anotherServiceId);


    }

}