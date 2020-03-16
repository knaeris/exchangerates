package com.example.exchangerates.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentCurrencyRates {

    private String base;

    private String date;

    private Map<String, Double> rates;

	public String getBase() {
        return base;
    }
}