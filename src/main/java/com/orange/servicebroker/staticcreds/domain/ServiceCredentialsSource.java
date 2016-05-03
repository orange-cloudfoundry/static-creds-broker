package com.orange.servicebroker.staticcreds.domain;

import java.util.Map;

/**
 * Created by YSBU7453 on 02/05/2016.
 */
public class ServiceCredentialsSource extends CredentialsSource<Service> {

    protected ServiceCredentialsSource(Service source) {
        super(source);
    }

    @Override
    public Map<String, Object> getCredentials() {
        return source.getFullCredentials();
    }
}
