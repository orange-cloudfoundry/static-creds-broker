package com.orange.model;

import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by YSBU7453 on 04/04/2016.
 */
public class ServiceTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

    @After
    public void closeContext() {
        this.context.close();
    }

    /*
    @Test
    public void should_map_credentials_from_json_to_map() throws Exception {
        Service service = new Service();
        service.setCredentialsJson("{\"username\":\"admin\", \"password\":\"pa55woRD\", \"uri\":\"mysql://USERNAME:PASSWORD@HOSTNAME:PORT/NAME\", \"hosname\": \"us-cdbr-east-03.cleardb.com\"}");
        assertThat(service.getCredentialsJson()).hasSize(4).includes(entry("username", "admin"), entry("password", "pa55woRD"),entry("uri", "mysql://USERNAME:PASSWORD@HOSTNAME:PORT/NAME"),entry("hosname", "us-cdbr-east-03.cleardb.com"));
    }
    */

    @Test
    public void service_has_a_default_id() {
        Service service = new Service();
        Assertions.assertThat(service.getId()).isNotNull();
    }

    @Test
    public void service_is_bindable_by_default() {
        Service service = new Service();
        Assertions.assertThat(service.isBindable()).isTrue();
    }

    @Test
    public void service_is_plan_updateable_by_default() {
        Service service = new Service();
        Assertions.assertThat(service.isPlanUpdateable()).isTrue();
    }

    @Test
    public void should_set_service_with_valid_name_and_description_and_plans() {
        this.context.register(TestConfiguration.class);
        EnvironmentTestUtils.addEnvironment(this.context,
                "services.tripadvisor.name:a_name",
                "services.tripadvisor.description:a_description",
                "services.tripadvisor.plans.default.name:default");
        this.context.refresh();
    }

    @Test
    public void fail_to_set_service_with_no_name() {
        this.context.register(TestConfiguration.class);
        EnvironmentTestUtils.addEnvironment(this.context,
                "services.tripadvisor.name:",
                "services.tripadvisor.description:a_description",
                "services.tripadvisor.plans.default.name:default");
        this.thrown.expect(BeanCreationException.class);
        this.thrown.expectMessage(Service.NO_NAME_ERROR);
        this.context.refresh();
    }

    @Test
    public void fail_to_set_service_with_no_description() {
        this.context.register(TestConfiguration.class);
        EnvironmentTestUtils.addEnvironment(this.context,
                "services.tripadvisor.name:a_name",
                "services.tripadvisor.description:",
                "services.tripadvisor.plans.default.name:default");
        this.thrown.expect(BeanCreationException.class);
        this.thrown.expectMessage(Service.NO_DESCRIPTION_ERROR);
        this.context.refresh();
    }

    @Test
    public void fail_to_set_service_with_no_plan() {
        this.context.register(TestConfiguration.class);
        EnvironmentTestUtils.addEnvironment(this.context,
                "services.tripadvisor.name:a_name",
                "services.tripadvisor.description:a_description");
        this.thrown.expect(BeanCreationException.class);
        this.thrown.expectMessage(Service.NO_PLAN_ERROR);
        this.context.refresh();
    }

    @SpringBootApplication(scanBasePackages = {"com.orange.model"})
    protected static class TestConfiguration {
    }

}
