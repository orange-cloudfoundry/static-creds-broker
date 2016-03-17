package com.orange.model;

import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.*;
import static org.fest.assertions.MapAssert.entry;
import static org.junit.Assert.*;

/**
 * Created by YSBU7453 on 17/03/2016.
 */
public class CredentialsTest {

    @Test
    public void can_add_credentials() throws Exception {

        Credentials toAdd = new Credentials();
        toAdd.put("username","john");
        toAdd.put("password","secret");

        Credentials credentials = new Credentials();
        credentials.putAll(toAdd);

        assertThat(credentials.toMap()).hasSize(2).includes(entry("username", "john"), entry("password", "secret"));


    }

}