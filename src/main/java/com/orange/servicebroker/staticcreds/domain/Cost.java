package com.orange.servicebroker.staticcreds.domain;

/**
 * Plan metadata costs
 */
public class Cost {

	private Amount amount;
	private String unit;

	public Cost() {
	}

	public Cost(Amount amount, String unit) {
		this.amount = amount;
		this.unit = unit;
	}

	public Amount getAmount() {
		return amount;
	}

	public void setAmount(Amount amount) {
		this.amount = amount;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Cost cost = (Cost) o;

		if (!amount.equals(cost.amount)) return false;
		return unit.equals(cost.unit);

	}

	@Override
	public int hashCode() {
		int result = amount.hashCode();
		result = 31 * result + unit.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Cost{" +
				"amount=" + amount +
				", unit='" + unit + '\'' +
				'}';
	}
}
