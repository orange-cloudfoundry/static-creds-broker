package com.orange.servicebroker.staticcreds.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Cloud Foundry Service Metadata
 * see http://docs.cloudfoundry.org/services/catalog-metadata.html#service-metadata-fields
 */
public class ServiceMetadata {

    private String displayName = "";
    private String imageUrl = "";
    private String supportUrl = "";
    private String documentationUrl = "";
    private String providerDisplayName = "";
    private String longDescription = "";

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSupportUrl() {
        return supportUrl;
    }

    public void setSupportUrl(String supportUrl) {
        this.supportUrl = supportUrl;
    }

    public String getDocumentationUrl() {
        return documentationUrl;
    }

    public void setDocumentationUrl(String documentationUrl) {
        this.documentationUrl = documentationUrl;
    }

    public String getProviderDisplayName() {
        return providerDisplayName;
    }

    public void setProviderDisplayName(String providerDisplayName) {
        this.providerDisplayName = providerDisplayName;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
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
