package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;

public class CurrencyConverter {

private static final String API_BASE_URL = "https://api.exchangerate-api.com/v4/latest/USD";
    private static JSONObject exchangeRates;
    private static Set<String> favoriteCurrencies = new HashSet<>();


    public static void main(String[] args) {
        loadExchangeRates();

        while (true) {
            System.out.println("Options:");
            System.out.println("1. Convert Currency");
            System.out.println("2. Add Favorite Currency");
            System.out.println("3. View Favorite Currencies");
            System.out.println("4. Update Favorite Currencies");
            System.out.println("5. Quit");
            System.out.print("Enter your choice: ");

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int choice = Integer.parseInt(reader.readLine());

                switch (choice) {
                    case 1:
                        convertCurrency();
                        break;
                    case 2:
                        addFavoriteCurrency();
                        break;
                    case 3:
                        viewFavoriteCurrencies();
                        break;
                    case 4:
                        updateFavoriteCurrencies();
                        break;
                    case 5:
                        System.out.println("Goodbye!");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please select a valid option.");
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadExchangeRates() {
        try {
            URL url = new URL(API_BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == 200) {
                StringBuilder response = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                exchangeRates = new JSONObject(response.toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void convertCurrency() {
        if (exchangeRates != null) {

            System.out.println("Enter the amount: ");
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                double amount = Double.parseDouble(reader.readLine());

                System.out.print("From currency (e.g., USD): ");
                String fromCurrency = reader.readLine().toUpperCase();

                System.out.print("To currency (e.g., EUR): ");
                String toCurrency = reader.readLine().toUpperCase();

                double conversionRate = exchangeRates.getJSONObject("rates").getDouble(toCurrency);
                System.out.println(conversionRate);
                double convertedAmount = amount * conversionRate;

                System.out.println(amount + " " + fromCurrency + " = " + convertedAmount + " " + toCurrency);
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Exchange rates not available. Please try again later.");
        }
    }



    private static void addFavoriteCurrency() {
        System.out.print("Enter the currency code you want to add to your favorites: ");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String currencyCode = reader.readLine().toUpperCase();
                if (favoriteCurrencies.add(currencyCode)) {
                    System.out.println(currencyCode + " has been added to your favorites.");
                } else {
                    System.out.println(currencyCode + " is already in your favorites.");
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void viewFavoriteCurrencies() {
        if (favoriteCurrencies.isEmpty()) {
            System.out.println("You haven't added any favorite currencies yet.");
        } else {
            System.out.println("Your favorite currencies:");
            for (String currency : favoriteCurrencies) {
                System.out.println(currency);
            }
        }
    }


    private static void updateFavoriteCurrencies() {
           addFavoriteCurrency();

        }



    private static boolean isValidCurrency(String currencyCode) {
        if (exchangeRates != null) {
            return exchangeRates.has(currencyCode);
        }
        return false; // Handle the case when exchange rates are not available
    }

}

