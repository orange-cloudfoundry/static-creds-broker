package com.orange.servicebroker.staticcreds.domain;

import java.util.List;

/**
 * Created by YSBU7453 on 04/04/2016.
 */
public interface ServiceRepository {

    List<ServiceProperties> findAll();

}
