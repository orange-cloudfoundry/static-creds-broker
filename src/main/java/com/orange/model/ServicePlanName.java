package com.orange.model;

import java.util.Arrays;
import java.util.UUID;

public class ServicePlanName {
	private String serviceName;
	private String planName;

	public ServicePlanName(String serviceName, String planName) {
		this.serviceName = serviceName;
		this.planName = planName;
	}

	public String getPlanUid() {
		return UUID.nameUUIDFromBytes(Arrays.asList(serviceName, planName).toString().getBytes()).toString();
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServicePlanName that = (ServicePlanName) o;
        if (!serviceName.equals(that.serviceName)) return false;
        return planName != null ? planName.equals(that.planName) : that.planName == null;

    }

    @Override
    public int hashCode() {
        int result = serviceName.hashCode();
        result = 31 * result + (planName != null ? planName.hashCode() : 0);
        return result;
    }
}
