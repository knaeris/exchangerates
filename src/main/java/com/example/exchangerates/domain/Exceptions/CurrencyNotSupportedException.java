package com.example.exchangerates.domain.Exceptions;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CurrencyNotSupportedException extends RuntimeException {

	private String error;

	public CurrencyNotSupportedException(String message, String error) {
		super(message);
		this.error = error;
	}
}
