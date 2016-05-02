package com.orange.servicebroker.staticcreds.domain;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by YSBU7453 on 04/04/2016.
 */
public class CatalogTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

    @After
    public void closeContext() {
        this.context.close();
    }

    @Test
    public void fail_to_set_config_with_no_service() {
        this.context.register(TestConfiguration.class);
        this.thrown.expect(BeanCreationException.class);
        this.thrown.expectMessage(CatalogSettings.NO_SERVICE_ERROR);
        this.context.refresh();
    }


    @Configuration
    @ComponentScan(basePackages = "com.orange.servicebroker.staticcreds.domain")
    protected static class TestConfiguration {
    }

}