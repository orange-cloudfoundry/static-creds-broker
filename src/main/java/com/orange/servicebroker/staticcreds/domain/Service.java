package com.orange.servicebroker.staticcreds.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cloud Foundry Service
 * http://docs.cloudfoundry.org/services/api.html
 */
public class Service {

    public static final String NO_ID_ERROR = "Invalid configuration. No id has been set for service";
    public static final String NO_NAME_ERROR = "Invalid configuration. No name has been set for service";
    public static final String NO_DESCRIPTION_ERROR = "Invalid configuration. No description has been set for service";
    public static final String NO_PLAN_ERROR = "Invalid configuration. No plans has been set for service";

    //@NotEmpty(message = NO_ID_ERROR)
    private String id;

    @NotEmpty(message = NO_NAME_ERROR)
    private String name;

    @NotEmpty(message = NO_DESCRIPTION_ERROR)
    private String description;

    private Boolean bindable = Boolean.TRUE;

    private Boolean planUpdateable = Boolean.TRUE;

    private List<String> tags;

    @Valid
    private ServiceMetadata metadata = new ServiceMetadata();

    @NotEmpty(message = NO_PLAN_ERROR)
    //@Size(min=1,message = NO_PLAN_ERROR)
    @Valid
    private Map<String, Plan> plans = new HashMap<>();

    private Map<String, Object> credentials;

    private Map<String, Object> credentialsJson;

    public Service() {
    }

    public Service(String id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Boolean isBindable() {
        return bindable;
    }

    public void setBindable(Boolean bindable) {
        this.bindable = bindable;
    }

    public Map<String, Plan> getPlans() {
        return plans;
    }

    public void setPlans(Map<String, Plan> plans) {
        this.plans = plans;
    }

    public ServiceMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ServiceMetadata metadata) {
        this.metadata = metadata;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isPlanUpdateable() {
        return planUpdateable;
    }

    public void setPlanUpdateable(Boolean planUpdateable) {
        this.planUpdateable = planUpdateable;
    }

    public Map<String, Object> getFullCredentials() {
        final Map<String, Object> full = new HashMap<>();
        if (credentials != null) {
            full.putAll(credentials);
        }
        if (credentialsJson != null) {
            full.putAll(credentialsJson);
        }
        return full.isEmpty() ? null : full;
    }

    public void setCredentials(Map<String, Object> credentials) {
        this.credentials = credentials;
    }

    public Map<String, Object> getCredentials() {
        return credentials;
    }

    public Map<String, Object> getCredentialsJson() {
        return credentialsJson;
    }

    public void setCredentialsJson(String credentialsJson) {
        JsonParser parser = JsonParserFactory.getJsonParser();
        this.credentialsJson = parser.parseMap(credentialsJson);
    }
}
