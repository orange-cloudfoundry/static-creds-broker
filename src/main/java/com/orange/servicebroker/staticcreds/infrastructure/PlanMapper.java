package com.orange.servicebroker.staticcreds.infrastructure;

import com.orange.servicebroker.staticcreds.domain.Plan;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by YSBU7453 on 26/04/2016.
 */
public class PlanMapper {


    public static org.springframework.cloud.servicebroker.model.Plan toServiceBrokerPlan(Plan plan) {

        return new org.springframework.cloud.servicebroker.model.Plan(plan.getId().toString(),
                plan.getName(),
                plan.getDescription(),
                plan.getMetadata() != null ? plan.getMetadata().toMap() : null,
                plan.isFree());
    }

    public static List<org.springframework.cloud.servicebroker.model.Plan> toServiceBrokerPlans(List<Plan> plans) {
        return plans.stream()
                .map(PlanMapper::toServiceBrokerPlan)
                .collect(Collectors.toList());
    }
}
