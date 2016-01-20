package com.orange.model;

import java.util.HashMap;
import java.util.Map;

public class Credentials {
	private Map<String, Object> credentials = new HashMap<String, Object>();

	public Credentials(Map<String, Object> credentials) {
		this.credentials = credentials;
	}

	public Map<String, Object> getCredentials() {
		return credentials;
	}
}
