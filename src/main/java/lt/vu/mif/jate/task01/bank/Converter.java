/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.vu.mif.jate.task01.bank;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Currency converter class.
 *
 * @author Svajunas
 */
public class Converter {

    /**
     * Currencies list.
     */
    private Set<Currency> currencies = new HashSet<>();

    /**
     * Create currency rates Map.
     */
    private static final Map<String, Rates> CURRENCYRATES = new HashMap<>();

    /**
     * Converter constructor. Loads currency rates and puts in the Map.
     */
    Converter() {
        File inputFile = new File("src/test/resources/banking/rates.txt");
        try {
            Scanner input = new Scanner(inputFile, "UTF8");

            while (input.hasNext()) {
                String info = input.nextLine();
                String[] elements = info.split(":");
                String currency = elements[0];
                BigDecimal rateToBase = new BigDecimal(elements[1]);
                BigDecimal rateFromBase = new BigDecimal(elements[2]);
                Rates rate = new Rates(rateToBase, rateFromBase);
                CURRENCYRATES.put(currency, rate);
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converter instance.
     */
    private static Converter converter;

    /**
     * Currency converter instance.
     *
     * @return converter instance
     */
    public static synchronized Converter getInstance() {
        if (converter == null) {
            converter = new Converter();
        }
        return converter;
    }

    /**
     * Get base currency.
     *
     * @return base currency
     */
    public final Currency getBaseCurrency() {
        Currency baseCurrency = Currency.getInstance("EUR");
        return baseCurrency;
    }

    /**
     * Get rate from chosen currency to base currency.
     *
     * @param currency currency code string
     * @return conversion rate of the chosen currency
     */
    public final BigDecimal getRateToBase(final String currency) {
        BigDecimal rate = CURRENCYRATES.get(currency).getRateToBase();
        return rate;
    }

    /**
     * Get rate from chosen currency to base currency.
     *
     * @param currency currency object
     * @return conversion rate of the chosen currency
     */
    public final BigDecimal getRateToBase(final Currency currency) {
        BigDecimal rate = CURRENCYRATES
                .get(currency.getCurrencyCode())
                .getRateToBase();
        return rate;
    }

    /**
     * Get rate from base currency to chosen currency.
     *
     * @param currency currency code string
     * @return conversion rate of the chosen currency
     */
    public final BigDecimal getRateFromBase(final String currency) {
        BigDecimal rate = CURRENCYRATES
                .get(currency)
                .getRateFromBase();
        return rate;
    }

    /**
     * Get rate from base currency to chosen currency.
     *
     * @param currency currency object
     * @return conversion rate of the currency
     */
    public final BigDecimal getRateFromBase(final Currency currency) {
        BigDecimal rate = CURRENCYRATES
                .get(currency.getCurrencyCode())
                .getRateFromBase();
        return rate;
    }

    /**
     * Convert amount of chosen currency to base currency.
     *
     * @param amount amount to be converted
     * @param currency chosen currency code string
     * @return converted amount
     * @throws NumberFormatException then amount number is incorrect
     */
    public final BigDecimal toBase(final String amount, final String currency)
            throws NumberFormatException {
        BigDecimal amountBD = new BigDecimal(amount);
        amountChecker(amountBD);
        currencyChecker(currency);
        BigDecimal rate = CURRENCYRATES.get(currency).getRateToBase();
        BigDecimal totalAmount = amountBD.multiply(rate);
        return totalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * Convert amount of chosen currency from base currency.
     *
     * @param amount amount to be converted
     * @param currency chosen currency code string
     * @return converted amount
     */
    public final BigDecimal fromBase(final String amount,
            final String currency) {
        BigDecimal amountBD = new BigDecimal(amount);
        amountChecker(amountBD);
        currencyChecker(currency);
        BigDecimal rate = CURRENCYRATES.get(currency).getRateFromBase();
        BigDecimal totalAmount = amountBD.multiply(rate);
        return totalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * Convert amount from one currency to another.
     *
     * @param amount amount to convert
     * @param currencyFrom currency code string to convert from
     * @param currencyTo currency code string to convert to
     * @return converted amount
     */
    public final BigDecimal convert(final String amount,
            final String currencyFrom, final String currencyTo) {
        BigDecimal amountBD = new BigDecimal(amount);
        amountChecker(amountBD);
        currencyChecker(currencyFrom);
        currencyChecker(currencyTo);
        BigDecimal currencyFromRate = CURRENCYRATES
                .get(currencyFrom)
                .getRateToBase();
        BigDecimal currencyToRate = CURRENCYRATES
                .get(currencyTo)
                .getRateFromBase();
        BigDecimal totalAmountFrom = amountBD.multiply(currencyFromRate);
        BigDecimal finalAmount = totalAmountFrom.multiply(currencyToRate);
        return finalAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * Get all currency objects.
     *
     * @return currency objects HahList
     */
    public final Set<Currency> getCurrencies() {
        for (String key : CURRENCYRATES.keySet()) {
            try {
                currencies.add(Currency.getInstance(key));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return currencies;
    }

    /**
     * Convert amount from one currency to another.
     *
     * @param amount amount to convert
     * @param cfrom currency object to convert from
     * @param cto currency object to convert to
     * @return converted amount
     */
    public final BigDecimal convert(final BigDecimal amount,
            final Currency cfrom, final Currency cto) {
        amountChecker(amount);
        if (cfrom.equals(cto)) {
            return amount;
        }
        BigDecimal currencyFromRate = CURRENCYRATES
                .get(cfrom.toString())
                .getRateToBase();
        BigDecimal currencyToRate = CURRENCYRATES
                .get(cto.toString())
                .getRateFromBase();
        BigDecimal totalAmountFrom = amount.multiply(currencyFromRate);
        BigDecimal finalAmount = totalAmountFrom.multiply(currencyToRate);
        return finalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Checks if amount format is correct.
     *
     * @param amount amount to check
     */
    public final void amountChecker(final BigDecimal amount) {
        if (getNumberOfDecimalPlaces(amount) > 2
                || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new NumberFormatException();
        }
    }

    /**
     * Get number of decimal places.
     *
     * @param bigDecimal BigDecimal number
     * @return number of decimal places
     */
    private int getNumberOfDecimalPlaces(final BigDecimal bigDecimal) {
        return Math.max(0, bigDecimal.stripTrailingZeros().scale());
    }

    /**
     * Checks if Currency Rates Map contains currency code string.
     *
     * @param currency currency code string
     */
    public static void currencyChecker(final String currency) {
        if (!CURRENCYRATES.containsKey(currency)) {
            throw new IllegalArgumentException();
        }
    }
}
