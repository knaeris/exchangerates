package com.example.exchangerates.services;

import com.example.exchangerates.domain.CurrentCurrencyRates;
import com.example.exchangerates.domain.HistoricalCurrencyRates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.DoubleStream;

@Service
public class CurrencyRateFetchingService {

	private SharedRestTemplate sharedRestTemplate;

	private final static String BASEURL = "https://api.exchangeratesapi.io";

	private final static String HIGHEST = "Highest";

	private final static String LOWEST = "Lowest";

	@Autowired
	public CurrencyRateFetchingService(SharedRestTemplate sharedRestTemplate) {
		this.sharedRestTemplate = sharedRestTemplate;
	}

	public Set<String> getSupportedCurrencies() {
		String url = BASEURL + "/latest";
		CurrentCurrencyRates currentCurrencyRates = this.sharedRestTemplate.restGet(url, CurrentCurrencyRates.class);
		Set<String> supportedCurrencies = new HashSet<>(currentCurrencyRates.getRates().keySet());
		String baseCurrency = currentCurrencyRates.getBase();
		supportedCurrencies.add(baseCurrency);
		return supportedCurrencies;
	}

	public double getCurrentCurrencyRate(String baseCurrency, String targetCurrency) {
		String url = BASEURL + "/latest?base=" + baseCurrency;
		CurrentCurrencyRates currentCurrencyRates = this.sharedRestTemplate.restGet(url, CurrentCurrencyRates.class);
		return currentCurrencyRates.getRates().get(targetCurrency);
	}

	public double getLowestCurrencyRateInTheCurrentMonth(String baseCurrency, String targetCurrency) {
		return getCurrencyRateExtremumInTheCurrentMonth(baseCurrency, targetCurrency, LOWEST);
	}

	public double getHighestCurrencyRateInTheCurrentMonth(String baseCurrency, String targetCurrency) {
		return getCurrencyRateExtremumInTheCurrentMonth(baseCurrency, targetCurrency, HIGHEST);
	}

	private double getCurrencyRateExtremumInTheCurrentMonth(String baseCurrency, String targetCurrency, String extremum) {
		Map<String, Map<String, Double>> rates = getAllRatesInCurrentMonth(baseCurrency);
		DoubleStream doubleStream = rates.values().stream().mapToDouble(entry -> entry.get(targetCurrency));
		if (HIGHEST.equals(extremum)) {
			return doubleStream.max().orElse(0);
		}
		if (LOWEST.equals(extremum)) {
			return doubleStream.min().orElse(0);
		}
		return 0;
	}

	private Map<String, Map<String, Double>> getAllRatesInCurrentMonth(String baseCurrency) {
		LocalDate todayDate = LocalDate.now();
		LocalDate firstDayOfCurrentMonth = todayDate.withDayOfMonth(1);
		String url = BASEURL + "/history?start_at=" + firstDayOfCurrentMonth + "&end_at=" + todayDate + "&base=" + baseCurrency;
		HistoricalCurrencyRates historicalCurrencyRates = this.sharedRestTemplate.restGet(url, HistoricalCurrencyRates.class);
		return historicalCurrencyRates.getRates();
	}
}
