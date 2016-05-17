package com.orange.servicebroker.staticcreds.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Plan metadata costs
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Cost {

	private Amount amount;
	private String unit;

	public Cost() {
	}

	public Cost(Amount amount, String unit) {
		this.amount = amount;
		this.unit = unit;
	}

}
