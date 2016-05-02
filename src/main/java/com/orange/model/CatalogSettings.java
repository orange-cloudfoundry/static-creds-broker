package com.orange.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

/**
 * Cloud Foundry Catalog
 * http://docs.cloudfoundry.org/services/api.html
 */
@Component
@ConfigurationProperties
public class CatalogSettings {

    public static final String NO_SERVICE_ERROR = "Invalid configuration. No service has been defined";

    @NotNull
    @Size(min=1,message = NO_SERVICE_ERROR)
    @Valid
    private Map<String,Service> services = new HashMap<>();

    public CatalogSettings(){
    }

    public CatalogSettings(Map<String, Service> services) {
        this.services = services;
    }

    public Map<String, Service> getServices() {
        return services;
    }

}
