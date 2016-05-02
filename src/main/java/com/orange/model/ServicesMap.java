package com.orange.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.orange.util.ParserProperties;

/**
 * toServiceDefinition of service id (String) and service properties definitions (Map<ServicePropertyName, String>)
 */
public class ServicesMap {
	private Map<String, Map<ServicePropertyName, String>> servicesMap = new HashMap<>();
	
	public Set<String> getServicesID(){
		return servicesMap.keySet();
	}
	
	/**
	 * get services properties for all services
	 * @return
	 */
	public Collection<Map<ServicePropertyName, String>> getAllServicesProperties() {
		return servicesMap.values();
	}
	
	public Set<Map.Entry<String, Map<ServicePropertyName, String>>> geEntrySet(){
		return servicesMap.entrySet();
	}

	/**
	 * get services properties of a specified service
	 * @param serviceID
	 * @return
	 */
	public Map<ServicePropertyName, String> get(String serviceID) {
		return servicesMap.get(serviceID);
	}

	/**
	 * add a service property, if the serviceID is not yet added to the servicesMap, its associated mandatory properties(service name and description) will be checked
	 * @param serviceID
	 * @param servicePropertyName
	 * @param servicePropertyValue
	 */
	public void addServiceProperty(String serviceID, ServicePropertyName servicePropertyName, String servicePropertyValue, ParserProperties parserProperties) {
		Map<ServicePropertyName, String> service = servicesMap.get(serviceID);
		if (service == null) {
			// when parsing a new id, check its mandatory properties
			parserProperties.checkServiceMandatoryPropertiesDefined(serviceID);
			// if mandatory properties defined, add it into toServiceDefinition
			service = new HashMap<>();
			servicesMap.put(serviceID, service);
		}
		service.put(servicePropertyName, servicePropertyValue);
	}
	
	/**
	 * For all services in serviceMap, set all the optional properties not defined in the system env variables to its default values
	 */
	public void setServicesPropertiesDefaults() {
		for (Map<ServicePropertyName, String> service : getAllServicesProperties()) {
			for (ServicePropertyName propertyName : ServicePropertyName.values()) {
				if (service.get(propertyName) != null) {
					continue;
				}
				switch (propertyName) {
					case BINDEABLE: 
						service.put(propertyName, "true");
						break;
					case METADATA_DISPLAYNAME:
						service.put(propertyName, service.get(ServicePropertyName.NAME));
						break;
					case METADATA_IMAGEURL: case METADATA_SUPPORTURL: case METADATA_DOCUMENTATIONURL: case METADATA_PROVIDERDISPLAYNAME: case METADATA_LONGDESCRIPTION:
						service.put(propertyName, "");
						break;
					default:
						break;
				}

			}
		}
	}
	
	public void checkServicesNameNotDuplicated() throws IllegalArgumentException {
		Set<String> serviceNames = new HashSet<>();
		for (Map<ServicePropertyName, String> service : getAllServicesProperties()) {
			String name = service.get(ServicePropertyName.NAME);
			if (!serviceNames.add(name)) {
				throw new IllegalArgumentException("Duplicated service name is not allowed: " + name);
			}
		}
	}
}
