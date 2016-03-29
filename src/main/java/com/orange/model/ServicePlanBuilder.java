package com.orange.model;

public class ServicePlanBuilder {
    private String serviceID;
    private String planID;

    public ServicePlanBuilder withServiceID(String serviceID) {
        this.serviceID = serviceID;
        return this;
    }

    public ServicePlanBuilder withPlanID(String planID) {
        this.planID = planID;
        return this;
    }

    public ServicePlanID build() {
        return new ServicePlanID(serviceID, planID);
    }
}