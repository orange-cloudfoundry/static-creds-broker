package com.orange.servicebroker.staticcreds.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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

    private Map<String, Object> credentials;

    private Map<String, Object> credentialsJson;

    public Service() {
    }

    public Service(String id) {
        this.id = id;
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

    public void setCredentialsJson(String credentialsJson) {
        JsonParser parser = JsonParserFactory.getJsonParser();
        this.credentialsJson = parser.parseMap(credentialsJson);
    }
}
