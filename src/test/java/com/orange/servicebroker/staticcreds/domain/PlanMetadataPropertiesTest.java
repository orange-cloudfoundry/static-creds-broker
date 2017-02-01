package com.orange.servicebroker.staticcreds.domain;

import org.junit.Test;

import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

/**
 * Created by YSBU7453 on 26/04/2016.
 */
public class PlanMetadataPropertiesTest {

    public static final CostProperties MONTHLY_COST = new CostProperties(new AmountProperties(new Float(99.0), new Float(49.0)), "MONTHLY");
    public static final CostProperties _1G_COST = new CostProperties(new AmountProperties(new Float(0.99), new Float(0.49)), "1GB of messages over 20GB");

    @Test
    public void should_get_map_representation_of_plan_metadata() throws Exception {
        PlanMetadataProperties planMetadataProperties = new PlanMetadataProperties();
        planMetadataProperties.setDisplayName("very expensive");
        final String[] BULLETS = {"20 GB of messages", "20 connections"};
        planMetadataProperties.setBullets(BULLETS);
        final CostProperties[] COSTS = {MONTHLY_COST, _1G_COST};
        planMetadataProperties.setCosts(COSTS);

        final Map<String, Object> res = planMetadataProperties.toMap();

        assertThat(res).hasSize(3).includes(entry("displayName", "very expensive"),entry("bullets", BULLETS),entry("costs", COSTS));

    }

}