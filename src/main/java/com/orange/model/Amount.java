package com.orange.model;

public class Amount {
	private float usd;
	private float eur;

	public Amount() {
	}

	public Amount(float usd, float eur) {
		this.usd = usd;
		this.eur = eur;
	}

	public float getUsd() {
		return usd;
	}

	public void setUsd(float usd) {
		this.usd = usd;
	}

	public float getEur() {
		return eur;
	}

	public void setEur(float eur) {
		this.eur = eur;
	}

	@Override
	public String toString() {
		return "Amount{" +
				"usd=" + usd +
				", eur=" + eur +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Amount amount = (Amount) o;

		if (Float.compare(amount.usd, usd) != 0) return false;
		return Float.compare(amount.eur, eur) == 0;

	}

	@Override
	public int hashCode() {
		int result = (usd != +0.0f ? Float.floatToIntBits(usd) : 0);
		result = 31 * result + (eur != +0.0f ? Float.floatToIntBits(eur) : 0);
		return result;
	}
}
