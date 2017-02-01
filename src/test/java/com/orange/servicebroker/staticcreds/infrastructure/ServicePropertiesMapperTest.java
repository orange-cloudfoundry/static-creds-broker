package com.orange.servicebroker.staticcreds.infrastructure;

import com.orange.servicebroker.staticcreds.domain.ServiceMetadataProperties;
import com.orange.servicebroker.staticcreds.domain.ServiceProperties;
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
public class ServicePropertiesMapperTest {

    public static final String SERVICE_ID = "a service id";
    public static final String SERVICE_DESCRIPTION = "a description";
    public static final String SERVICE_NAME = "a service name";

    @Test
    public void map_service_id() {
        //GIVEN
        ServiceProperties serviceProperties = new ServiceProperties(SERVICE_ID);
        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(serviceProperties);
        //THEN
        assertThat(res.getId()).as("toServiceDefinition serviceProperties id").isEqualTo(SERVICE_ID.toString());
    }

    @Test
    public void map_service_description() {
        //GIVEN
        ServiceProperties serviceProperties = new ServiceProperties(SERVICE_ID);
        serviceProperties.setDescription(SERVICE_DESCRIPTION);
        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(serviceProperties);
        //THEN
        assertThat(res.getDescription()).as("toServiceDefinition serviceProperties description").isEqualTo(SERVICE_DESCRIPTION);
    }

    @Test
    public void map_service_name() {
        //GIVEN
        ServiceProperties serviceProperties = new ServiceProperties(SERVICE_ID);
        serviceProperties.setName(SERVICE_NAME);
        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(serviceProperties);
        //THEN
        assertThat(res.getName()).as("toServiceDefinition serviceProperties name").isEqualTo(SERVICE_NAME);
    }

    @Test
    public void map_service_bindable() {
        //GIVEN
        ServiceProperties serviceProperties = new ServiceProperties(SERVICE_ID);
        serviceProperties.setBindable(Boolean.FALSE);
        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(serviceProperties);
        //THEN
        assertThat(res.isBindable()).as("toServiceDefinition serviceProperties bindable").isFalse();
    }

    @Test
    public void map_service_requires() {
        //GIVEN
        ServiceProperties serviceProperties = new ServiceProperties(SERVICE_ID);
        serviceProperties.setRequires(Arrays.asList("syslog_drain"));
        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(serviceProperties);
        //THEN
        assertThat(res.getRequires()).as("toServiceDefinition serviceProperties requires")
                .containsExactly("syslog_drain");
    }

    @Test
    public void map_service_plan_updateable() {
        //GIVEN
        ServiceProperties serviceProperties = new ServiceProperties(SERVICE_ID);
        serviceProperties.setPlanUpdateable(Boolean.FALSE);
        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(serviceProperties);
        //THEN
        assertThat(res.isPlanUpdateable()).as("toServiceDefinition serviceProperties plan updateable").isFalse();
    }

    @Test
    public void map_service_metadata() {
        //GIVEN
        ServiceMetadataProperties serviceMetadataProperties = new ServiceMetadataProperties();
        serviceMetadataProperties.setDisplayName("aDisplayName");
        serviceMetadataProperties.setLongDescription("a long description");
        serviceMetadataProperties.setProviderDisplayName("a provider");
        serviceMetadataProperties.setDocumentationUrl("http://localhost/doc");
        serviceMetadataProperties.setImageUrl("http://localhost/image.png");
        serviceMetadataProperties.setSupportUrl("http://localhost/support");

        ServiceProperties serviceProperties = new ServiceProperties(SERVICE_ID);
        serviceProperties.setMetadata(serviceMetadataProperties);

        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(serviceProperties);
        //THEN
        assertThat(res.getMetadata()).as("toServiceDefinition serviceProperties metadata").hasSize(6).includes(
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
        ServiceProperties serviceProperties = new ServiceProperties(SERVICE_ID);
        serviceProperties.setTags(Stream.of("tag1", "tag2", "tag3").collect(Collectors.toList()));

        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(serviceProperties);
        //THEN
        assertThat(res.getTags()).as("toServiceDefinition serviceProperties tags").containsOnly("tag1", "tag2", "tag3");
    }

    @Test
    public void does_not_map_requires() {
        //GIVEN
        ServiceProperties serviceProperties = new ServiceProperties(SERVICE_ID);
        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(serviceProperties);
        //THEN
        assertThat(res.getRequires()).as("does not toServiceDefinition requires").isNull();
    }

    @Test
    public void does_not_map_dashboard_client() {
        //GIVEN
        ServiceProperties serviceProperties = new ServiceProperties(SERVICE_ID);
        //WHEN
        final ServiceDefinition res = ServiceMapper.toServiceDefinition(serviceProperties);
        //THEN
        assertThat(res.getDashboardClient()).as("does not toServiceDefinition dashboard client").isNull();
    }

    public void should_map_services() {
        //GIVEN
        final String aServiceId = "a service id";
        ServiceProperties aServiceProperties = new ServiceProperties(aServiceId);
        final String anotherServiceId = "another service id";
        ServiceProperties anotherServiceProperties = new ServiceProperties(anotherServiceId);
        //WHEN
        final List<ServiceDefinition> serviceDefinitions = ServiceMapper.toServiceDefinitions(Arrays.asList(aServiceProperties, anotherServiceProperties));
        //THEN
        assertThat(serviceDefinitions).as("map services to service definitions").hasSize(2);
        assertThat(serviceDefinitions.stream().map(ServiceDefinition::getId).collect(Collectors.toList())).as("map services to service definitions").containsOnly(aServiceId, anotherServiceId);


    }

}