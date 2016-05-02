package com.orange.servicebroker.staticcreds.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Cloud Foundry plan Metadata
 * see ttp://docs.cloudfoundry.org/services/catalog-metadata.html#plan-metadata-fields
 */
public class PlanMetadata {
    private String[] bullets;
    private Cost[] costs;
    private String displayName;

    public Cost[] getCosts() {
        return costs;
    }

    public void setCosts(Cost[] costs) {
        this.costs = costs;
    }

    public String[] getBullets() {
        return bullets;
    }

    public void setBullets(String[] bullets) {
        this.bullets = bullets;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> metadata = new HashMap<>();
        metadata.put("displayName", displayName);
        metadata.put("costs", costs);
        metadata.put("bullets", bullets);
        return metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlanMetadata that = (PlanMetadata) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(bullets, that.bullets)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(costs, that.costs)) return false;
        return displayName != null ? displayName.equals(that.displayName) : that.displayName == null;

    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(bullets);
        result = 31 * result + Arrays.hashCode(costs);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PlanMetadata{" +
                "bullets=" + Arrays.toString(bullets) +
                ", costs=" + Arrays.toString(costs) +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
