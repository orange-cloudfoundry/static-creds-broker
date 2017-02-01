package com.orange.servicebroker.staticcreds.domain;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AmountProperties {
    private float usd;
	private float eur;

    public AmountProperties() {
    }

    public AmountProperties(float usd, float eur) {
        this.usd = usd;
		this.eur = eur;
	}

}
