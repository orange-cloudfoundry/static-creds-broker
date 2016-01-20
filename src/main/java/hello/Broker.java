package hello;

import cf.spring.servicebroker.*;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;

@Configuration
@EnableAutoConfiguration
@EnableServiceBroker(username = "user", password = "#{ systemEnvironment['SECURITY_PASSWORD'] }")
@ServiceBroker(@Service(id = "000d5d66-e95b-4c19-beaf-064becbd3ada", name = "#{ systemEnvironment['SERVICES_ID_NAME'] }", 
	description = "#{ systemEnvironment['SERVICES_ID_DESCRIPTION'] }", bindable = "#{ systemEnvironment['SERVICES_ID_BINDEABLE'] ?: 'true'}",
	tags = "#{ systemEnvironment['SERVICES_ID_TAGS'] ?: {}}", // T(java.util.Arrays).asList()
//	tags = new SpelExpressionParser().parseExpression("#{ {'orange', 'coconut'} }").getValue(String.class),
	metadata = {
			@Metadata(field = Metadata.DISPLAY_NAME, value = { "#{ systemEnvironment['SERVICES_ID_METADATA_DISPLAYNAME'] ?: systemEnvironment['SERVICES_ID_NAME']}" }),
			@Metadata(field = Metadata.IMAGE_URL, value = { "#{ systemEnvironment['SERVICES_ID_METADATA_IMAGEURL'] ?: '' }"}),
			@Metadata(field = Metadata.SUPPORT_URL, value = { "#{ systemEnvironment['SERVICES_ID_METADATA_SUPPORTURL'] ?: '' }"}),
			@Metadata(field = Metadata.DOCUMENTATION_URL, value = { "#{ systemEnvironment['SERVICES_ID_METADATA_DOCUMENTATIONURL'] ?: ''}"}),
			@Metadata(field = Metadata.PROVIDER_DISPLAY_NAME, value = { "#{ systemEnvironment['SERVICES_ID_METADATA_PROVIDERDISPLAYNAME '] ?: ''}"}),
			@Metadata(field = Metadata.LONG_DESCRIPTION, value = { "#{ systemEnvironment['SERVICES_ID_METADATA_LONGDESCRIPTION'] ?: ''}"})
	}, 
	plans = {
			@ServicePlan(id = "101d240e-c36f-46e8-b35f-97d2f69bd185", name = "#{ systemEnvironment['PLAN_NAME'] ?: 'default' }", 
					description = "#{ systemEnvironment['PLAN_DESCRIPTION'] ?: 'Default plan' }", free = "#{ systemEnvironment['PLAN_FREE'] ?: 'true'}", 
					metadata = {
//							@Metadata(field = "#{ systemEnvironment['PLAN_METADATA'] }", value = "") 
							}
			)
	}
))
public class Broker {

	@Provision
	public ProvisionResponse provision(ProvisionRequest request) {
		System.out.println("instance provisioning: " + request.getInstanceGuid());
		return new ProvisionResponse();
	}

	@Deprovision
	public void deprovision(DeprovisionRequest request) {
		System.out.println("Delete service instance: " + request.getInstanceGuid());
	}

	@Bind
	public BindResponse bind(BindRequest request) {
//		System.out.println("Binding " + request.getServiceInstanceGuid() + " to " + request.getApplicationGuid());
		Map<String, String> credentialsMap = new HashMap<>();
//		credentialsMap.put("uri", "#{ systemEnvironment['SERVICES_ID_CREDENTIALS_URI'] }");
//		credentialsMap.put("hostname", "#{ systemEnvironment['SERVICES_ID_CREDENTIALS_HOSTNAME'] ?: 'hostname'}");
//		credentialsMap.put("myownkey", "#{ systemEnvironment['SERVICES_ID_CREDENTIALS_MYOWNKEY'] ?: 'myownkey'}");
		return new BindResponse(credentialsMap); 
	}

	@Unbind
	public void unbind(UnbindRequest request) {
		System.out.println("Removing binding " + request.getBindingGuid());
	}

	public static void main(String[] args) {
		SpringApplication.run(Broker.class, args);
	}
}
