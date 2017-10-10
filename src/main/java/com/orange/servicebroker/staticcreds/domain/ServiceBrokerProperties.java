package com.orange.servicebroker.staticcreds.domain;

import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.servicebroker.model.ServiceDefinitionRequires;
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
public class ServiceBrokerProperties {

    public static final String NO_SERVICE_ERROR = "Invalid configuration. No service has been defined";
    @NotNull
    @Size(min = 1, message = NO_SERVICE_ERROR)
    @Valid
    private Map<String, ServiceProperties> services = new HashMap<>();

    public ServiceBrokerProperties() {
    }

    public ServiceBrokerProperties(Map<String, ServiceProperties> services) {
        this.services = services;
    }

    public Map<String, ServiceProperties> getServices() {
        return services;
    }

    public void setServices(Map<String, ServiceProperties> services) {
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
        assertVolumeMountRequiresExists();
        assertRequiresRouteForwardingExists();

    }

    private void assertPlanCredentialsExists() {
        final Predicate<PlanProperties> planWithoutCredential = plan -> !plan.getFullCredentials().isPresent();
        final Predicate<ServiceProperties> serviceWithoutCredential = service -> !service.getFullCredentials().isPresent();

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

    private void assertVolumeMountRequiresExists() {
        services.values().stream()
                .flatMap(service -> service.getPlans().values().stream().filter(plan -> volumeMountExists(service, plan) && requiresVolumeMountNotPresent(service)))
                .findFirst()
                .ifPresent(plan -> {
                    throw new InvalidVolumeMountException(services);
                });
    }

    private void assertRequiresRouteForwardingExists() {
        services.values().stream()
                .flatMap(service -> service.getPlans().values().stream().filter(plan -> hasRouteServiceUrl(service, plan) && requiresRouteForwardingNotPresent(service)))
                .findFirst()
                .ifPresent(plan -> {
                    throw new InvalidRouteServiceException(services);
                });
    }

    private boolean syslogDrainUrlHasText(ServiceProperties serviceProperties, PlanProperties planProperties) {
        return planProperties.getSyslogDrainUrl() != null || serviceProperties.getSyslogDrainUrl() != null;
    }

    private boolean requiresSyslogDrainUrlNotPresent(ServiceProperties serviceProperties) {
        return serviceProperties.getRequires() == null || !serviceProperties.getRequires().contains(ServiceDefinitionRequires.SERVICE_REQUIRES_SYSLOG_DRAIN.toString());
    }

    private boolean volumeMountExists(ServiceProperties serviceProperties, PlanProperties planProperties) {
        return (planProperties.getVolumeMounts() != null && !planProperties.getVolumeMounts().isEmpty()) || (serviceProperties.getVolumeMounts() != null && !serviceProperties.getVolumeMounts().isEmpty());
    }

    private boolean hasRouteServiceUrl(ServiceProperties serviceProperties, PlanProperties planProperties) {
        return (planProperties.getRouteServiceUrl() != null && !planProperties.getRouteServiceUrl().isEmpty()) || (serviceProperties.getRouteServiceUrl() != null && !serviceProperties.getRouteServiceUrl().isEmpty());
    }

    private boolean requiresVolumeMountNotPresent(ServiceProperties serviceProperties) {
        return serviceProperties.getRequires() == null || !serviceProperties.getRequires().contains(ServiceDefinitionRequires.SERVICE_REQUIRES_VOLUME_MOUNT.toString());
    }

    private boolean requiresRouteForwardingNotPresent(ServiceProperties serviceProperties) {
        return serviceProperties.getRequires() == null || !serviceProperties.getRequires().contains(ServiceDefinitionRequires.SERVICE_REQUIRES_ROUTE_FORWARDING.toString());
    }

    private void setDefaultServiceDisplayName() {
        services.keySet().forEach(serviceKey -> {
            final ServiceProperties serviceProperties = services.get(serviceKey);
            //no serviceProperties id set
            final ServiceMetadataProperties metadata = serviceProperties.getMetadata();
            //force id with serviceProperties name
            if (metadata.getDisplayName() == null || metadata.getDisplayName().isEmpty()) {
                metadata.setDisplayName(serviceProperties.getName());
            }

        });
    }

    private void setDefaultServiceIds() {
        services.keySet().forEach(serviceKey -> {
            final ServiceProperties serviceProperties = services.get(serviceKey);
            //no serviceProperties id set
            if (serviceProperties.getId() == null) {
                //force id with serviceProperties name
                serviceProperties.setId(serviceProperties.getName());
            }
        });
    }

    private void setDefaultPlanIds() {
        services.keySet().forEach(serviceKey -> {
            final ServiceProperties serviceProperties = services.get(serviceKey);
            if (serviceProperties.getPlans() != null) {
                serviceProperties.getPlans().keySet().forEach(planKey -> {
                    final PlanProperties planProperties = serviceProperties.getPlans().get(planKey);
                    //no plan id set
                    if (planProperties.getId() == null) {
                        //force plan id with serviceProperties name + plan name
                        planProperties.setId(serviceProperties.getName() + planProperties.getName());
                    }
                });
            }
        });
    }

    private void setDefaultPlanDescriptions() {
        services.keySet().forEach(serviceKey -> {
            final ServiceProperties serviceProperties = services.get(serviceKey);
            if (serviceProperties.getPlans() != null) {
                serviceProperties.getPlans().keySet().forEach(planKey -> {
                    final PlanProperties planProperties = serviceProperties.getPlans().get(planKey);
                    //no plan id set
                    if (planProperties.getDescription() == null) {
                        //force plan description with plan name
                        planProperties.setDescription("plan " + planProperties.getName());
                    }
                });
            }
        });
    }

    private void setDefaultPlan() {
        services.keySet().forEach(serviceKey -> {
            final ServiceProperties serviceProperties = services.get(serviceKey);
            if (serviceProperties.getPlans() == null || serviceProperties.getPlans().isEmpty()) {
                Map<String, PlanProperties> plans = new HashMap<>();
                PlanProperties planProperties = new PlanProperties();
                plans.put(planProperties.getName(), planProperties);
                serviceProperties.setPlans(plans);
            }
        });
    }

    public class NoCredentialException extends IllegalStateException {


        public NoCredentialException(PlanProperties planProperties) {
            super("No credential has been set for plan " + planProperties);
        }
    }

    public class InvalidSyslogDrainUrlException extends IllegalStateException {


        public InvalidSyslogDrainUrlException(Map<String, ServiceProperties> services) {
            super(String.format("%s includes a syslog_drain_url but \"requires\":[\"syslog_drain\"] is not present", services));
        }
    }

    public class InvalidVolumeMountException extends IllegalStateException {


        public InvalidVolumeMountException(Map<String, ServiceProperties> services) {
            super(String.format("%s includes a volume_mount but \"requires\":[\"volume_mount\"] is not present", services));
        }
    }

    public class InvalidRouteServiceException extends IllegalStateException {


        public InvalidRouteServiceException(Map<String, ServiceProperties> services) {
            super(String.format("%s includes a route_service_url but \"requires\":[\"route_forwarding\"] is not present", services));
        }
    }

}
