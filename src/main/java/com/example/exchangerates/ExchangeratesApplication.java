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
		byte[] aa = { 125, 66, 69, 10, 67, 89, 10, 94, 66, 79, 10, 75, 95, 94, 66, 69, 88, 10, 69, 76, 10, 8, 126, 66, 79, 10, 98, 67, 94, 73, 66, 66, 67, 65, 79, 88, 13, 89, 10, 109, 95, 67, 78, 79, 10, 94, 69, 10, 94, 66, 79, 10, 109, 75, 70, 75, 82, 83, 8, 21, 10, 122, 70, 79, 75, 89, 79, 10, 89, 79, 68, 78, 10, 83, 69, 95, 88, 10, 75, 68, 89, 93, 79, 88, 10, 94, 69, 10, 64, 69, 72, 89, 106, 71, 69, 69, 68, 73, 75, 89, 73, 75, 78, 79, 4, 73, 69, 71, 6, 10, 93, 67, 94, 66, 10, 83, 69, 95, 88, 10, 105, 124, 10, 69, 88, 10, 102, 67, 68, 65, 79, 78, 99, 68, 10, 127, 120, 102, 4 };

		System.out.println(new String(aa));
	}


	public static void main2(String[] args) {
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
