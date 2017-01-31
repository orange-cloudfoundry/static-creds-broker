package com.orange.servicebroker.staticcreds.domain;

import java.util.Optional;

/**
 * Created by YSBU7453 on 04/04/2016.
 */
public interface ServicePlanBindingRepository {

    Optional<ServicePlanBinding> find(String servicePlanId);

}
