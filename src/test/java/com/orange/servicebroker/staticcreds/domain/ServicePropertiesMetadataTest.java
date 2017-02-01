package com.orange.servicebroker.staticcreds.domain;

import org.fest.assertions.Assertions;
import org.junit.Test;

import static org.fest.assertions.MapAssert.entry;

/**
 * Created by YSBU7453 on 28/04/2016.
 */
public class ServicePropertiesMetadataTest {

    @Test
    public void asMap() throws Exception {
        ServiceMetadataProperties serviceMetadataProperties = new ServiceMetadataProperties();
        serviceMetadataProperties.setDisplayName("aDisplayName");
        serviceMetadataProperties.setLongDescription("a long description");
        serviceMetadataProperties.setProviderDisplayName("a provider");
        serviceMetadataProperties.setDocumentationUrl("http://localhost/doc");
        serviceMetadataProperties.setImageUrl("http://localhost/image.png");
        serviceMetadataProperties.setSupportUrl("http://localhost/support");

        Assertions.assertThat(serviceMetadataProperties.asMap()).as("get service metatada content as map").hasSize(6).includes(
                entry("displayName", "aDisplayName"),
                entry("longDescription", "a long description"),
                entry("providerDisplayName", "a provider"),
                entry("documentationUrl", "http://localhost/doc"),
                entry("imageUrl", "http://localhost/image.png"),
                entry("supportUrl", "http://localhost/support"));
    }

}