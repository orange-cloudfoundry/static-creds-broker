package com.orange.servicebroker.staticcreds.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.cloud.servicebroker.model.ServiceDefinitionRequires;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Cloud Foundry Service
 * http://docs.cloudfoundry.org/services/api.html
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Service {

    public static final String NO_NAME_ERROR = "Invalid configuration. No name has been set for service";
    public static final String NO_DESCRIPTION_ERROR = "Invalid configuration. No description has been set for service";

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

    //@NotEmpty(message = NO_PLAN_ERROR)
    //@Size(min=1,message = NO_PLAN_ERROR)
    @Valid
    private Map<String, Plan> plans = new HashMap<>();

    private Map<String, Object> credentials = new HashMap<>();

    private Map<String, Object> credentialsJson = new HashMap<>();

    /**
     * The URL to which Cloud Foundry should drain logs for the bound application.
     */
    private String syslogDrainUrl;

    /**
     * A list of permissions that the user would have to give the service, if they provision it. See
     * {@link ServiceDefinitionRequires} for supported permissions.
     */
    private List<String> requires;

    /**
     * The URL of a web-based management user interface for the service instance. Can be <code>null</code> to indicate
     * that a management dashboard is not provided.
     */
    private String dashboardUrl;

    public Service() {
    }

    public Service(String id) {
        this.id = id;
    }

    public Optional<Map<String, Object>> getFullCredentials() {
        final Map<String, Object> full = new HashMap<>();
        if (credentials != null) {
            full.putAll(credentials);
        }
        if (credentialsJson != null) {
            full.putAll(credentialsJson);
        }
        return full.isEmpty() ? Optional.empty() : Optional.of(full);
    }

    public void setCredentialsJson(String credentialsJson) {
        JsonParser parser = JsonParserFactory.getJsonParser();
        this.credentialsJson = parser.parseMap(credentialsJson);
    }
}
