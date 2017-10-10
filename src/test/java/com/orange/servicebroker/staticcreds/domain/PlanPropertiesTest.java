package com.orange.servicebroker.staticcreds.domain;

import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

/**
 * Created by YSBU7453 on 08/04/2016.
 */
public class PlanPropertiesTest {

    public static final CostProperties MONTHLY_COST = new CostProperties(new AmountProperties(new Float(99.0), new Float(49.0)), "MONTHLY");
    public static final CostProperties _1G_COST = new CostProperties(new AmountProperties(new Float(0.99), new Float(0.49)), "1GB of messages over 20GB");

    @Test
    public void plan_metadata_is_set_using_json() throws Exception {
        PlanProperties planProperties = new PlanProperties();
        planProperties.setMetadata("{\"bullets\":[\"20 GB of messages\",\"20 connections\"],\"costs\":[{\"amount\":{\"usd\":99.0,\"eur\":49.0},\"unit\":\"MONTHLY\"},{\"amount\":{\"usd\":0.99,\"eur\":0.49},\"unit\":\"1GB of messages over 20GB\"}],\"displayName\":\"Big Bunny\"}");
        Assertions.assertThat(planProperties.getMetadata().getBullets()).containsOnly("20 GB of messages", "20 connections");
        Assertions.assertThat(planProperties.getMetadata().getDisplayName()).isEqualTo("Big Bunny");
        assertThat(Arrays.asList(planProperties.getMetadata().getCosts())).containsOnly(MONTHLY_COST, _1G_COST);
    }

    @Test
    public void plan_credentials_may_be_set_using_json() throws Exception {
        PlanProperties planProperties = new PlanProperties();
        planProperties.setCredentialsJson("{\"username\":\"admin\", \"password\":\"pa55woRD\", \"uri\":\"mysql://USERNAME:PASSWORD@HOSTNAME:PORT/NAME\", \"hosname\": \"us-cdbr-east-03.cleardb.com\"}");
        Assertions.assertThat(planProperties.getCredentialsJson()).hasSize(4).includes(entry("username", "admin"), entry("password", "pa55woRD"), entry("uri", "mysql://USERNAME:PASSWORD@HOSTNAME:PORT/NAME"), entry("hosname", "us-cdbr-east-03.cleardb.com"));
    }

    @Test
    public void plan_is_free_by_default() {
        PlanProperties defaultPlanProperties = new PlanProperties();
        Assertions.assertThat(defaultPlanProperties.getFree()).isTrue();
    }

    @Test
    public void plan_has_a_default_name() {
        PlanProperties defaultPlanProperties = new PlanProperties();
        Assertions.assertThat(defaultPlanProperties.getName()).isNotEmpty();
        Assertions.assertThat(defaultPlanProperties.getName()).isEqualTo("default");
    }

}