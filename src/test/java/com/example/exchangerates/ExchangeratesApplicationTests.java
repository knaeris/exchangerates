package com.example.exchangerates;

import com.example.exchangerates.domain.CurrentCurrencyRates;
import com.example.exchangerates.domain.HistoricalCurrencyRates;
import com.example.exchangerates.services.CurrencyRateFetchingService;
import com.example.exchangerates.services.SharedRestTemplate;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
class ExchangeratesApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void getCurrentExchangeRateForBaseCurrencyToTargetCurrency(){
		String baseCurrency = "USD";
		String targetCurrency = "EUR";
		String url = "https://api.exchangeratesapi.io/latest?base=" + baseCurrency;
		SharedRestTemplate mockSharedRestTemplate = mock(SharedRestTemplate.class);
		Map<String, Double> rates = new HashMap<>();
		rates.put("EUR", 1.223);
		rates.put("CAD", 1.234);
		when(mockSharedRestTemplate.restGet(url, CurrentCurrencyRates.class)).thenReturn(new CurrentCurrencyRates(baseCurrency, "12.12.2012", rates));
		CurrencyRateFetchingService currencyRateFetchingService = new CurrencyRateFetchingService(mockSharedRestTemplate);
		double currentCurrencyRate = currencyRateFetchingService.getCurrentCurrencyRate(baseCurrency, targetCurrency);
		MatcherAssert.assertThat(currentCurrencyRate, is(1.223));
	}

	@Test
	void getLowestBaseCurrencyRateToTargetCurrencyInTheCurrentMonth(){
		String baseCurrency = "USD";
		String targetCurrency = "EUR";
		LocalDate todayDate = LocalDate.now();
		LocalDate firstDayOfCurrentMonth = todayDate.withDayOfMonth(1);
		String url = "https://api.exchangeratesapi.io/history?start_at=" + firstDayOfCurrentMonth + "&end_at="+ todayDate +"&base=" + baseCurrency;
		SharedRestTemplate mockSharedRestTemplate = mock(SharedRestTemplate.class);
		Map<String, Map<String,Double>> rates = new HashMap<>();
		Map<String, Double> todaysRates = new HashMap<>();
		todaysRates.put("EUR", 1.1);
		todaysRates.put("CAD", 1.11);

		Map<String, Double> yesterdaysRates = new HashMap<>();
		yesterdaysRates.put("EUR", 1.2);
		yesterdaysRates.put("CAD", 1.22);

		Map<String, Double> ratesTwoDaysAgo = new HashMap<>();
		ratesTwoDaysAgo.put("EUR", 1.3);
		ratesTwoDaysAgo.put("CAD", 1.33);

		Map<String, Double> ratesThreeDaysAgo = new HashMap<>();
		ratesThreeDaysAgo.put("EUR", 0.9);
		ratesThreeDaysAgo.put("CAD", 0.99);

		rates.put(todayDate.toString(), todaysRates);
		rates.put(todayDate.minusDays(1).toString(), yesterdaysRates);
		rates.put(todayDate.minusDays(2).toString(), ratesTwoDaysAgo);
		rates.put(todayDate.minusDays(3).toString(), ratesThreeDaysAgo);

		when(mockSharedRestTemplate.restGet(url, HistoricalCurrencyRates.class))
				.thenReturn(new HistoricalCurrencyRates(baseCurrency, firstDayOfCurrentMonth.toString(), todayDate.toString(), rates));

		CurrencyRateFetchingService currencyRateFetchingService = new CurrencyRateFetchingService(mockSharedRestTemplate);
		double currentCurrencyRate = currencyRateFetchingService.getLowestCurrencyRateInTheCurrentMonth(baseCurrency, targetCurrency);
		MatcherAssert.assertThat(currentCurrencyRate, is(0.9));

	}

	@Test
	void getHighestBaseCurrencyRateToTargetCurrencyInTheCurrentMonth(){
		String baseCurrency = "USD";
		String targetCurrency = "CAD";
		LocalDate todayDate = LocalDate.now();
		LocalDate firstDayOfCurrentMonth = todayDate.withDayOfMonth(1);
		String url = "https://api.exchangeratesapi.io/history?start_at=" + firstDayOfCurrentMonth + "&end_at="+ todayDate +"&base=" + baseCurrency;
		SharedRestTemplate mockSharedRestTemplate = mock(SharedRestTemplate.class);
		Map<String, Map<String,Double>> rates = new HashMap<>();
		Map<String, Double> todaysRates = new HashMap<>();
		todaysRates.put("EUR", 1.1);
		todaysRates.put("CAD", 1.11);

		Map<String, Double> yesterdaysRates = new HashMap<>();
		yesterdaysRates.put("EUR", 1.2);
		yesterdaysRates.put("CAD", 1.22);

		Map<String, Double> ratesTwoDaysAgo = new HashMap<>();
		ratesTwoDaysAgo.put("EUR", 1.3);
		ratesTwoDaysAgo.put("CAD", 1.33);

		Map<String, Double> ratesThreeDaysAgo = new HashMap<>();
		ratesThreeDaysAgo.put("EUR", 0.9);
		ratesThreeDaysAgo.put("CAD", 0.99);

		rates.put(todayDate.toString(), todaysRates);
		rates.put(todayDate.minusDays(1).toString(), yesterdaysRates);
		rates.put(todayDate.minusDays(2).toString(), ratesTwoDaysAgo);
		rates.put(todayDate.minusDays(3).toString(), ratesThreeDaysAgo);

		when(mockSharedRestTemplate.restGet(url, HistoricalCurrencyRates.class))
				.thenReturn(new HistoricalCurrencyRates(baseCurrency, firstDayOfCurrentMonth.toString(), todayDate.toString(), rates));

		CurrencyRateFetchingService currencyRateFetchingService = new CurrencyRateFetchingService(mockSharedRestTemplate);
		double currentCurrencyRate = currencyRateFetchingService.getHighestCurrencyRateInTheCurrentMonth(baseCurrency, targetCurrency);
		MatcherAssert.assertThat(currentCurrencyRate, is(1.33));

	}
}
