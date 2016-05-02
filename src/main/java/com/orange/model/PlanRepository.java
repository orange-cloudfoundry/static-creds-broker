package com.orange.model;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by YSBU7453 on 04/04/2016.
 */
public interface PlanRepository {

    Optional<Plan> find(UUID servicePlanId);

}
