package com.orange.model;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by YSBU7453 on 17/03/2016.
 */
public class ServicePlan {

    private String serviceId;
    private String planId;

    public ServicePlan(String serviceID, String planID) {
        this.serviceId = serviceID;
        this.planId = planID;
    }

    public String getPlanUid() {
        return UUID.nameUUIDFromBytes(Arrays.asList(serviceId, planId).toString().getBytes()).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServicePlan that = (ServicePlan) o;

        if (!serviceId.equals(that.serviceId)) return false;
        return planId != null ? planId.equals(that.planId) : that.planId == null;

    }

    @Override
    public int hashCode() {
        int result = serviceId.hashCode();
        result = 31 * result + (planId != null ? planId.hashCode() : 0);
        return result;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getPlanId() {
        return planId;
    }
}
