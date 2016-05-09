package com.orange.servicebroker.staticcreds.domain;

import org.fest.assertions.Assertions;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
    public void should_set_service_with_valid_id_name_and_description_and_plans() {
        this.context.register(TestConfiguration.class);
        EnvironmentTestUtils.addEnvironment(this.context,
                "services.tripadvisor.name:a_name",
                "services.tripadvisor.description:a_description",
                "services.tripadvisor.plans.default.name:default",
                "services.tripadvisor.plans.default.id:default_id");
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

    @SpringBootApplication(scanBasePackages = {"com.orange.servicebroker.staticcreds.domain"})
    protected static class TestConfiguration {
    }

}
