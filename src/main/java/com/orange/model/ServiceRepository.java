package com.orange.model;

import java.util.List;

/**
 * Created by YSBU7453 on 04/04/2016.
 */
public interface ServiceRepository {

    List<Service> findAll();

}
