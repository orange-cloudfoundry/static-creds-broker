package com.orange.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Cloud Foundry Service
 * http://docs.cloudfoundry.org/services/api.html
 */
public class Service {

    public static final String NO_ID_ERROR = "Invalid configuration. No id has been set for service";
    public static final String NO_NAME_ERROR = "Invalid configuration. No name has been set for service";
    public static final String NO_DESCRIPTION_ERROR = "Invalid configuration. No description has been set for service";
    public static final String NO_PLAN_ERROR = "Invalid configuration. No plans has been set for service";

    public void setId(UUID id) {
        this.id = id;
    }

    @NotNull(message = NO_ID_ERROR)
    private UUID id = UUID.randomUUID();

    @NotEmpty(message = NO_NAME_ERROR)
    private String name;

    @NotEmpty(message = NO_DESCRIPTION_ERROR)
    private String description;

    private Boolean bindable = Boolean.TRUE;

    private Boolean planUpdateable = Boolean.TRUE;

    private List<String> tags;

    @Valid
    private ServiceMetadata metadata;

    @NotEmpty(message = NO_PLAN_ERROR)
    //@Size(min=1,message = NO_PLAN_ERROR)
    @Valid
    private Map<String,Plan> plans = new HashMap<>();

    public Service() {
    }

    public Service(UUID id) {
        this.id = id;
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

    public UUID getId() {
        return id;
    }

    public Boolean isPlanUpdateable() {
        return planUpdateable;
    }

    public void setPlanUpdateable(Boolean planUpdateable) {
        this.planUpdateable = planUpdateable;
    }
}
