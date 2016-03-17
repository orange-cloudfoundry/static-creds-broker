package com.orange.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YSBU7453 on 17/03/2016.
 */
public class Credentials {
    
    private Map<String, Object> credentials =new HashMap<>();

    public void putAll(Credentials credentials) {
        this.credentials.putAll(credentials.toMap());
    }

    public Map<String,Object> toMap() {
        return Collections.unmodifiableMap(credentials);
    }

    public void put(String credentialName, Object credentialValue) {
        this.credentials.put(credentialName,credentialValue);
    }
}
