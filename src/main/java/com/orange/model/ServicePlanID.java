package com.orange.model;

/**
 * Created by YSBU7453 on 17/03/2016.
 */
public class ServicePlanID {

    private String serviceId;
    private String planId;

    public ServicePlanID(String serviceID, String planID) {
        this.serviceId = serviceID;
        this.planId = planID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServicePlanID that = (ServicePlanID) o;

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
