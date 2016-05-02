package com.orange.model;

import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;
import static org.junit.Assert.*;

/**
 * Created by YSBU7453 on 26/04/2016.
 */
public class PlanMetadataTest {

    public static final Cost MONTHLY_COST = new Cost(new Amount(new Float(99.0), new Float(49.0)), "MONTHLY");
    public static final Cost _1G_COST = new Cost(new Amount(new Float(0.99), new Float(0.49)), "1GB of messages over 20GB");

    @Test
    public void should_get_map_representation_of_plan_metadata() throws Exception {
        PlanMetadata planMetadata = new PlanMetadata();
        planMetadata.setDisplayName("very expensive");
        final String[] BULLETS = {"20 GB of messages", "20 connections"};
        planMetadata.setBullets(BULLETS);
        final Cost[] COSTS = {MONTHLY_COST, _1G_COST};
        planMetadata.setCosts(COSTS);

        final Map<String, Object> res = planMetadata.toMap();

        assertThat(res).hasSize(3).includes(entry("displayName", "very expensive"),entry("bullets", BULLETS),entry("costs", COSTS));

    }

}