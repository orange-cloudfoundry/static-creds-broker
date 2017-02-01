package com.orange.servicebroker.staticcreds.infrastructure;

import com.orange.servicebroker.staticcreds.domain.PlanProperties;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by YSBU7453 on 26/04/2016.
 */
public class PlanMapper {


    public static org.springframework.cloud.servicebroker.model.Plan toServiceBrokerPlan(PlanProperties planProperties) {

        return new org.springframework.cloud.servicebroker.model.Plan(planProperties.getId(),
                planProperties.getName(),
                planProperties.getDescription(),
                planProperties.getMetadata() != null ? planProperties.getMetadata().toMap() : null,
                planProperties.getFree());
    }

    public static List<org.springframework.cloud.servicebroker.model.Plan> toServiceBrokerPlans(List<PlanProperties> planProperties) {
        return planProperties.stream()
                .map(PlanMapper::toServiceBrokerPlan)
                .collect(Collectors.toList());
    }
}
