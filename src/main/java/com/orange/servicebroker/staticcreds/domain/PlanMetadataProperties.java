package com.orange.servicebroker.staticcreds.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Cloud Foundry plan Metadata
 * see ttp://docs.cloudfoundry.org/services/catalog-metadata.html#plan-metadata-fields
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class PlanMetadataProperties {
    private String[] bullets;
    private CostProperties[] costs;
    private String displayName;

    public Map<String, Object> toMap() {
        HashMap<String, Object> metadata = new HashMap<>();
        metadata.put("displayName", displayName);
        metadata.put("costs", costs);
        metadata.put("bullets", bullets);
        return metadata;
    }

}
