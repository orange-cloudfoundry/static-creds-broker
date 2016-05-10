package com.orange.servicebroker.staticcreds.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Cloud Foundry plan
 * see http://docs.cloudfoundry.org/services/catalog-metadata.html#plan-metadata-fields
 */
public class Plan {

    public static final String NO_ID_ERROR = "Invalid configuration. No id has been set for plan";

    public static final Boolean FREE_DEFAULT = Boolean.TRUE;
    public static final String PLAN_NAME_DEFAULT = "default";

    //@NotEmpty(message = NO_ID_ERROR)
    private String id;

    @NotNull
    private String name = PLAN_NAME_DEFAULT;

    private String description;

    @Valid
    private PlanMetadata metadata = new PlanMetadata();

    @NotNull
    private Boolean free = FREE_DEFAULT;

    private Map<String, Object> credentials = new HashMap<>();

    private Map<String, Object> credentialsJson = new HashMap<>();

    public Plan() {
    }

    public Plan(String id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Object> getCredentials() {
        return credentials;
    }

    public Map<String, Object> getFullCredentials() {
        final Map<String, Object> full = new HashMap<>();
        full.putAll(credentials);
        full.putAll(credentialsJson);
        return full;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCredentials(Map<String, Object> credentials) {
        this.credentials = credentials;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFree(Boolean free) {
        this.free = free;
    }

    public Boolean getFree() {
        return free;
    }

    public PlanMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadataJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.metadata = mapper.readValue(metadataJson, PlanMetadata.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCredentialsJson(String credentialsJson) {
        JsonParser parser = JsonParserFactory.getJsonParser();
        this.credentialsJson = parser.parseMap(credentialsJson);
    }

    public Map<String, Object> getCredentialsJson() {
        return credentialsJson;
    }

    public String getId() {
        return id;
    }

    public boolean isFree() {
        return free;
    }
}
