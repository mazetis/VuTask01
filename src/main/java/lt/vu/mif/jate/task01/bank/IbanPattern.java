/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.vu.mif.jate.task01.bank;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import lt.vu.mif.jate.task01.bank.exception.IBANException;

/**
 * IBAN pattern class (holds IBAN logic by country).
 *
 * @author Svajunas
 */
public class IbanPattern {

    /**
     * Country 2 letter code.
     */
    private String countryCode;
    /**
     * Index of the first bank code number.
     */
    private Integer indexOfBankCode;
    /**
     * Index of the last bank code number.
     */
    private Integer lastIndexOfBankCode;
    /**
     * Index of the first account number.
     */
    private Integer indexOfAccountNumber;
    /**
     * Index of the last account number.
     */
    private Integer lastIndexOfAccountNumber;
    /**
     * IBAN length.
     */
    private Integer ibanLength;

    /**
     * Constructor.
     */
    IbanPattern() {
    }

    /**
     * Create IBAN pattern.
     *
     * @param country country 2 letter code
     * @param indexBC index of the first bank code number
     * @param lastIndexBC index of the last bank code number
     * @param indexAN index of the first account number
     * @param lastIndexAN index of the last account number
     * @param length IBAN length
     */
    public IbanPattern(final String country, final Integer indexBC,
            final Integer lastIndexBC, final Integer indexAN,
            final Integer lastIndexAN, final Integer length) {
        this.countryCode = country;
        this.indexOfBankCode = indexBC;
        this.lastIndexOfBankCode = lastIndexBC;
        this.indexOfAccountNumber = indexAN;
        this.lastIndexOfAccountNumber = lastIndexAN;
        this.ibanLength = length;
    }

    /**
     * Country code getter.
     *
     * @return country code
     */
    public final String getCountryCode() {
        return countryCode;
    }

    /**
     * Get place of the first bank code number in IBAN string.
     *
     * @return index of the first bank code number
     */
    public final Integer getIndexOfBankCode() {
        return indexOfBankCode;
    }

    /**
     * Get place of the last bank code number in IBAN string.
     *
     * @return index of the last bank code number
     */
    public final Integer getLastIndexOfBankCode() {
        return lastIndexOfBankCode;
    }

    /**
     * Get place of the first account number in IBAN string.
     *
     * @return index of the first account number
     */
    public final Integer getIndexOfAccountNumber() {
        return indexOfAccountNumber;
    }

    /**
     * Get place of the last account number in IBAN string.
     *
     * @return index of the last account number
     */
    public final Integer getLastIndexOfAccountNumber() {
        return lastIndexOfAccountNumber;
    }

    /**
     * Get IBAN length.
     *
     * @return length of the IBAN
     */
    public final Integer getIbanLength() {
        return ibanLength;
    }

    /**
     * IBAN patterns Map to hold all IBAN patterns by country.
     */
    private static final Map<String, IbanPattern> IBANPATTERNS
            = new HashMap<>();

    /**
     * Read and return IBAN patterns by country from source file.
     *
     * @return IBAN patterns
     */
    public static Map<String, IbanPattern> getIbanPatterns() {
        File inputFile = new File("src/test/resources/banking/iban.txt");
        try {
            Scanner input = new Scanner(inputFile, "UTF8");

            while (input.hasNext()) {
                String info = input.nextLine();
                String[] elements;
                elements = info.split(":");
                String patternNoSpaces = elements[1].replaceAll("\\s+", "");
                String countryCode = patternNoSpaces.substring(0, 2);
                Integer startOfBankCode = patternNoSpaces.indexOf("b");
                Integer endOfBankCode = patternNoSpaces.lastIndexOf("b");
                Integer startOfAccountNumber = patternNoSpaces.indexOf("c");
                Integer endOfAccountNumber = patternNoSpaces.lastIndexOf("c");
                Integer length = patternNoSpaces.length();
                IbanPattern ibanPattern = new IbanPattern(countryCode,
                        startOfBankCode, endOfBankCode, startOfAccountNumber,
                        endOfAccountNumber, length);

                IBANPATTERNS.put(countryCode, ibanPattern);
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return IBANPATTERNS;
    }

    /**
     * Check and return correct form IBAN number.
     *
     * @param ibanNo IBAN number string
     * @return correct form IBAN number
     * @throws IBANException then string does not match pattern
     */
    public static String ibanChecker(final String ibanNo) throws IBANException {
        String correctFormIbanNoSpace = ibanNo
                .replaceAll("\\s+", "")
                .replaceAll("\t", "");
        String correctFormIban = correctFormIbanNoSpace
                .toUpperCase(Locale.ENGLISH);
        String countryCode = correctFormIban.substring(0, 2);
        if (!getIbanPatterns().containsKey(countryCode)) {
            throw new IBANException(ibanNo,
                    "IBAN country not found: " + countryCode);
        }
        Integer expectedLength = IbanPattern.getIbanPatterns()
                .get(countryCode)
                .getIbanLength();
        if (expectedLength != correctFormIban.length()) {
            throw new IBANException(ibanNo,
                    "IBAN number length wrong: expected " + expectedLength
                    + ", got " + correctFormIban.length());
        }
        if (!correctFormIban.substring(2, correctFormIban
                .length())
                .matches("[0-9]+")) {
            throw new IBANException(ibanNo,
                    "IBAN format wrong: " + correctFormIban);
        }
        return correctFormIban;
    }
}
