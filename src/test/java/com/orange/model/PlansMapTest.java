package com.orange.model;

import org.junit.Assert;
import org.junit.Test;

public class PlansMapTest {
	private static String planOneID = "PLAN_1";
	private static String planTwoID = "PLAN_2";
	
	@Test
	public void should_get_default_plan_info_when_plan_not_set(){
		PlansMap plansMap = new PlansMap();
		plansMap.setPlansPropertiesDefaults();
		Assert.assertEquals(1, plansMap.getIDs().size());
		Assert.assertEquals(PlansMap.defaultPlanID, plansMap.getIDs().toArray()[0]);
		Assert.assertEquals(PlansMap.defaultPlanName, plansMap.get(PlansMap.defaultPlanID).get(PlanPropertyName.NAME));
		Assert.assertEquals(PlansMap.defaultPlanDescription, plansMap.get(PlansMap.defaultPlanID).get(PlanPropertyName.DESCRIPTION));
		Assert.assertEquals(PlansMap.defaultFree, plansMap.get(PlansMap.defaultPlanID).get(PlanPropertyName.FREE));
		Assert.assertEquals(PlansMap.defaultMetadata, plansMap.get(PlansMap.defaultPlanID).get(PlanPropertyName.METADATA));
	}
	
	@Test
	//done see PlanTest
	public void should_get_default_info_when_plan_set_which_not_complete_info(){
		PlansMap plansMap = new PlansMap();
		plansMap.addPlanWithoutProperty(planOneID);
		plansMap.addPlanWithoutProperty(planTwoID);
		plansMap.setPlansPropertiesDefaults();
		Assert.assertEquals(2, plansMap.getIDs().size());
		Assert.assertEquals(planOneID, plansMap.getIDs().toArray()[0]);
		Assert.assertEquals(planTwoID, plansMap.getIDs().toArray()[1]);
		Assert.assertEquals(plansMap.getDefaultName(planOneID), plansMap.get(planOneID).get(PlanPropertyName.NAME));
		Assert.assertEquals(plansMap.getDefaultDescription(planOneID), plansMap.get(planOneID).get(PlanPropertyName.DESCRIPTION));
		Assert.assertEquals(PlansMap.defaultFree, plansMap.get(planOneID).get(PlanPropertyName.FREE));
		Assert.assertEquals(PlansMap.defaultMetadata, plansMap.get(planOneID).get(PlanPropertyName.METADATA));
		Assert.assertEquals(plansMap.getDefaultName(planTwoID), plansMap.get(planTwoID).get(PlanPropertyName.NAME));
		Assert.assertEquals(plansMap.getDefaultDescription(planTwoID), plansMap.get(planTwoID).get(PlanPropertyName.DESCRIPTION));
		Assert.assertEquals(PlansMap.defaultFree, plansMap.get(planTwoID).get(PlanPropertyName.FREE));
		Assert.assertEquals(PlansMap.defaultMetadata, plansMap.get(planTwoID).get(PlanPropertyName.METADATA));
	}

}
