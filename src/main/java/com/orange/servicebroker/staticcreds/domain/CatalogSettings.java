package com.orange.servicebroker.staticcreds.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Cloud Foundry Catalog
 * http://docs.cloudfoundry.org/services/api.html
 */
@Component
@ConfigurationProperties
public class CatalogSettings {

    public static final String NO_SERVICE_ERROR = "Invalid configuration. No service has been defined";

    public void setServices(Map<String, Service> services) {
        this.services = services;
    }

    @NotNull
    @Size(min = 1, message = NO_SERVICE_ERROR)
    @Valid
    private Map<String, Service> services = new HashMap<>();

    public CatalogSettings() {
    }

    public CatalogSettings(Map<String, Service> services) {
        this.services = services;
    }

    public Map<String, Service> getServices() {
        return services;
    }

    @PostConstruct
    public void init() {
        setDefaultServiceIds();
        setDefaultServiceDisplayName();
        setDefaultPlan();
        setDefaultPlanIds();
        setDefaultPlanDescriptions();
        assertPlanCredentialsExists();
    }

    private void assertPlanCredentialsExists() {
        final Predicate<Plan> planWithoutCredential = plan -> plan.getFullCredentials() == null || plan.getFullCredentials().isEmpty();
        final Predicate<Service> serviceWithoutCredential = service -> service.getFullCredentials() == null || service.getFullCredentials().isEmpty();

        services.values().stream()
                .filter(serviceWithoutCredential)
                .flatMap(service -> service.getPlans().values().stream())
                .filter(planWithoutCredential)
                .findFirst()
                .ifPresent(plan -> {
                    throw new NoCredentialException(plan);
                });
    }

    private void setDefaultServiceDisplayName() {
        services.keySet().forEach(serviceKey -> {
            final Service service = services.get(serviceKey);
            //no service id set
            final ServiceMetadata metadata = service.getMetadata();
            //force id with service name
            if (metadata.getDisplayName() == null || metadata.getDisplayName().isEmpty()) {
                metadata.setDisplayName(service.getName());
            }

        });
    }

    private void setDefaultServiceIds() {
        services.keySet().forEach(serviceKey -> {
            final Service service = services.get(serviceKey);
            //no service id set
            if (service.getId() == null) {
                //force id with service name
                service.setId(service.getName());
            }
        });
    }

    private void setDefaultPlanIds() {
        services.keySet().forEach(serviceKey -> {
            final Service service = services.get(serviceKey);
            if (service.getPlans() != null) {
                service.getPlans().keySet().forEach(planKey -> {
                    final Plan plan = service.getPlans().get(planKey);
                    //no plan id set
                    if (plan.getId() == null) {
                        //force plan id with service name + plan name
                        plan.setId(service.getName() + plan.getName());
                    }
                });
            }
        });
    }

    private void setDefaultPlanDescriptions() {
        services.keySet().forEach(serviceKey -> {
            final Service service = services.get(serviceKey);
            if (service.getPlans() != null) {
                service.getPlans().keySet().forEach(planKey -> {
                    final Plan plan = service.getPlans().get(planKey);
                    //no plan id set
                    if (plan.getDescription() == null) {
                        //force plan description with plan name
                        plan.setDescription("plan " + plan.getName());
                    }
                });
            }
        });
    }

    private void setDefaultPlan() {
        services.keySet().forEach(serviceKey -> {
            final Service service = services.get(serviceKey);
            if (service.getPlans() == null || service.getPlans().isEmpty()) {
                Map<String, Plan> plans = new HashMap<>();
                Plan plan = new Plan();
                plans.put(plan.getName(), plan);
                service.setPlans(plans);
            }
        });
    }

    class NoCredentialException extends IllegalStateException {


        public NoCredentialException(Plan plan) {
            super("No credential has been set for plan " + plan);
        }
    }

}
