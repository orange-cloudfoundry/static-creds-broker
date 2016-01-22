package com.orange.model;

public class PlanMetadata {
	private String[] bullets;
	private Cost[] costs;
	private String displayName;

	public Cost[] getCosts() {
		return costs;
	}

	public void setCosts(Cost[] costs) {
		this.costs = costs;
	}

	public String[] getBullets() {
		return bullets;
	}

	public void setBullets(String[] bullets) {
		this.bullets = bullets;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
