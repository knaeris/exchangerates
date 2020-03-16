package com.example.exchangerates.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricalCurrencyRates {

	private String base;

	@JsonProperty("start_at")
	private String startAt;

	@JsonProperty("end_at")
	private String endAt;

	private Map<String, Map<String, Double>> rates = new HashMap<>();




}
