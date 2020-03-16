package com.example.exchangerates.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Input {

	private String baseCurrency;

	private String targetCurrency;
}
