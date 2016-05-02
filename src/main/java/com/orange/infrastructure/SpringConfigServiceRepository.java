package com.orange.infrastructure;

import com.orange.model.CatalogSettings;
import com.orange.model.Service;
import com.orange.model.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by YSBU7453 on 28/04/2016.
 */
@Component
public class SpringConfigServiceRepository implements ServiceRepository {

    private CatalogSettings catalogSettings;

    @Autowired
    public SpringConfigServiceRepository(CatalogSettings catalogSettings) {
        this.catalogSettings = catalogSettings;
    }


    @Override
    public List<Service> findAll() {
        return catalogSettings.getServices().entrySet().stream()
                                                       .map(Map.Entry::getValue)
                                                       .collect(Collectors.toList());
    }
}
