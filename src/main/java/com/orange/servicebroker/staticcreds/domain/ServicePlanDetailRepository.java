package com.orange.servicebroker.staticcreds.domain;

import java.util.Optional;

/**
 * Created by YSBU7453 on 04/04/2016.
 */
public interface ServicePlanDetailRepository {

    Optional<ServicePlanDetail> find(String servicePlanId);

}
