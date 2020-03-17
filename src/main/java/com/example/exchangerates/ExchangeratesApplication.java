package com.example.exchangerates;

import com.example.exchangerates.services.CurrencyRateFetchingService;
import com.example.exchangerates.services.SharedRestTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;
import java.util.Set;

@SpringBootApplication
public class ExchangeratesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeratesApplication.class, args);

		CurrencyRateFetchingService currencyRateFetchingService = new CurrencyRateFetchingService(new SharedRestTemplate());

		System.out.println("Supported currencies are: ");

		Set<String> supportedCurrencies = currencyRateFetchingService.getSupportedCurrencies();

		supportedCurrencies.forEach(System.out::println);

		while (true) {

			Scanner scanner = new Scanner(System.in);

			String baseCurrency = getCurrencyFromInput(supportedCurrencies, scanner, "base");

			String targetCurrency = getCurrencyFromInput(supportedCurrencies, scanner, "target");

			System.out.println("Current exchange rate for base currency to target currency");

			System.out.println(currencyRateFetchingService.getCurrentCurrencyRate(baseCurrency, targetCurrency));

			System.out.println("The lowest base currency rate to target currency in the current month");

			System.out.println(currencyRateFetchingService.getLowestCurrencyRateInTheCurrentMonth(baseCurrency, targetCurrency));

			System.out.println("The highest base currency rate to target currency in the current month");

			System.out.println(currencyRateFetchingService.getHighestCurrencyRateInTheCurrentMonth(baseCurrency, targetCurrency));

			boolean answerValid = false;

			while (!answerValid) {

				System.out.print("Keep going? Y/N ");

				String answer = scanner.next().toUpperCase().trim();

				switch (answer) {
					case "Y":
						answerValid = true;
						break;
					case "N":
						System.exit(0);
					default:
						System.out.println("Please enter only Y or N");
						break;
				}
			}
		}
	}

	private static String getCurrencyFromInput(Set<String> supportedCurrencies, Scanner scanner, String currencyType) {
		String currency = null;

		boolean currencyValid = false;

		while (!currencyValid) {

			System.out.println("Enter " + currencyType + " currency");

			currency = scanner.next().toUpperCase().trim();

			if (!supportedCurrencies.contains(currency)) {
				System.out.println("Currency " + currency + " is not supported");
				continue;
			}

			currencyValid = true;
		}
		return currency;
	}

}
