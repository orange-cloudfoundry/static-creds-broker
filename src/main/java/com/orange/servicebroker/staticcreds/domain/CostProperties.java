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
public class CostProperties {

    private AmountProperties amount;
    private String unit;

    public CostProperties() {
    }

    public CostProperties(AmountProperties amount, String unit) {
        this.amount = amount;
        this.unit = unit;
    }

}
