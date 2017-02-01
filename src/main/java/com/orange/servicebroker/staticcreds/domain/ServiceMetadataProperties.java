package com.orange.servicebroker.staticcreds.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Cloud Foundry ServiceProperties Metadata
 * see http://docs.cloudfoundry.org/services/catalog-metadata.html#service-metadata-fields
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ServiceMetadataProperties {

    private String displayName = "";
    private String imageUrl = "";
    private String supportUrl = "";
    private String documentationUrl = "";
    private String providerDisplayName = "";
    private String longDescription = "";

    public ServiceMetadataProperties() {
    }

    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("displayName", displayName);
        map.put("imageUrl", imageUrl);
        map.put("longDescription", longDescription);
        map.put("providerDisplayName", providerDisplayName);
        map.put("documentationUrl", documentationUrl);
        map.put("supportUrl", supportUrl);
        return map;
    }
}
