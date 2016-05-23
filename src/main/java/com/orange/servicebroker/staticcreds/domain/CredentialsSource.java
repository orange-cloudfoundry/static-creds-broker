package com.orange.servicebroker.staticcreds.domain;

import java.util.Map;

/**
 * Created by YSBU7453 on 02/05/2016.
 */
public abstract class CredentialsSource<T> {

    protected final T source;

    protected CredentialsSource( T source ) {
        this.source = source;
    }

    /**
     * Return the credentials,
     * or {@code null} if not found.
     */
    public abstract Map<String, Object> getCredentials();
}
