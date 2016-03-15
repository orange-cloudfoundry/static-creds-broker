package com.orange.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * map of plan id (String) and plan properties definitions (Map<PlanPropertyName, String>)
 * which store multi plans properties of a specific service
 */
public class PlansMap {
	private Map<String, Map<PlanPropertyName, String>> plansMap = new HashMap<>();
	public static final String defaultPlanID = "0";
	
	/**
	 * get plans properties for all plans
	 * @return
	 */
	public Collection<Map<PlanPropertyName, String>> getAllPlansProperties() {
		return plansMap.values();
	}
	
	/**
	 * get plan properties of a specified plan
	 * @param planID
	 * @return
	 */
	public Map<PlanPropertyName, String> get(String planID) {
		return plansMap.get(planID);
	}
	
	/**
	 * get all plan ids defined in the map
	 * @return
	 */
	public Set<String> getIDs(){
		return plansMap.keySet();
	}
	
	/**
	 * get all plan names defined in the map
	 * @return
	 */
	public Set<String> getNames(){
		Set<String> names = new HashSet<>();
		for (Map<PlanPropertyName, String> planProperties : getAllPlansProperties()) {
			names.add(planProperties.get(PlanPropertyName.NAME));
		}
		return names;
	}
	
	public void addPlanProperty(String planID, PlanPropertyName planPropertyName, String planPropertyValue) {
		Map<PlanPropertyName, String> plan = plansMap.get(planID);
		if (plan == null) {
			plan = new HashMap<>();
			plansMap.put(planID, plan);
		}
		plan.put(planPropertyName, planPropertyValue);
	}
	
	/**
	 * For all plans in planMap, set all the optional properties not defined in the system env variables to its default values
	 * if no plan defined, add a default plan
	 * make sure the function is called when the parse of all related env. var. is finished
	 * 
	 */
	public void setPlansPropertiesDefaults() {
		if (plansMap.isEmpty()) {
			Map<PlanPropertyName, String> plan = new HashMap<>();
			plansMap.put(defaultPlanID, plan);
		}
		for (Map<PlanPropertyName, String> plan : plansMap.values()) {
			for (PlanPropertyName propertyName : PlanPropertyName.values()) {
				if (plan.get(propertyName) != null) {
					continue;
				}
				switch (propertyName) {
					case FREE:
						plan.put(propertyName, "true");
						break;
					case NAME:
						plan.put(propertyName, "default");
						break;
					case DESCRIPTION:
						plan.put(propertyName, "Default plan");
						break;
					case METADATA:
						plan.put(propertyName, "{}");
						break;
					default:
						break;
				}

			}
		}
	}
	
	public void checkPlansNameNotDuplicated() throws IllegalArgumentException {
		Set<String> planNames = new HashSet<>();
		for (Map<PlanPropertyName, String> plan : plansMap.values()) {
			String name = plan.get(PlanPropertyName.NAME);
			if (!planNames.add(name)) {
				throw new IllegalArgumentException("Duplicated plan name is not allowed: " + name);
			}
		}
	}
}
