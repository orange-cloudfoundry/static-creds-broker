package com.orange.servicebroker.staticcreds.domain;

import lombok.ToString;
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
@ToString
public class CatalogSettings {

    public static final String NO_SERVICE_ERROR = "Invalid configuration. No service has been defined";
    public static final String SYSLOG_DRAIN_REQUIRES = "syslog_drain";
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

    public void setServices(Map<String, Service> services) {
        this.services = services;
    }

    @PostConstruct
    public void init() {
        setDefaultServiceIds();
        setDefaultServiceDisplayName();
        setDefaultPlan();
        setDefaultPlanIds();
        setDefaultPlanDescriptions();
        assertPlanCredentialsExists();
        assertSyslogDrainUrlRequiresExists();
    }

    private void assertPlanCredentialsExists() {
        final Predicate<Plan> planWithoutCredential = plan -> plan.getFullCredentials() == null || plan.getFullCredentials().isEmpty();
        final Predicate<Service> serviceWithoutCredential = service -> !service.getFullCredentials().isPresent();

        services.values().stream()
                .filter(serviceWithoutCredential)
                .flatMap(service -> service.getPlans().values().stream())
                .filter(planWithoutCredential)
                .findFirst()
                .ifPresent(plan -> {
                    throw new NoCredentialException(plan);
                });
    }

    private void assertSyslogDrainUrlRequiresExists() {
        services.values().stream()
                .flatMap(service -> service.getPlans().values().stream().filter(plan -> syslogDrainUrlHasText(service, plan) && requiresSyslogDrainUrlNotPresent(service)))
                .findFirst()
                .ifPresent(plan -> {
                    throw new InvalidSyslogDrainUrlException(services);
                });
    }

    private boolean syslogDrainUrlHasText(Service service, Plan plan) {
        return plan.getSyslogDrainUrl() != null || service.getSyslogDrainUrl() != null;
    }

    private boolean requiresSyslogDrainUrlNotPresent(Service service) {
        return service.getRequires() == null || !service.getRequires().contains(SYSLOG_DRAIN_REQUIRES);
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

    class InvalidSyslogDrainUrlException extends IllegalStateException {


        public InvalidSyslogDrainUrlException(Map<String, Service> services) {
            super(String.format("%s includes a syslog_drain_url but \"requires\":[\"syslog_drain\"] is not present", services));
        }
    }

}
