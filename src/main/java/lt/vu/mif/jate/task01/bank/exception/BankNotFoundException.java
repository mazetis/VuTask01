/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.vu.mif.jate.task01.bank.exception;

/**
 * Bank Not Found Exception.
 *
 * @author Svajunas.
 */
public class BankNotFoundException extends Exception {

    /**
     * Bank 2 letters country code.
     */
    private String countryCode;
    /**
     * Bank code.
     */
    private Integer bankCode;

    /**
     * Constructor with error message.
     *
     * @param country bank 2 letters country code
     * @param code bank code
     */
    public BankNotFoundException(final String country,
            final Integer code) {
        super(String.format("Bank (%s-%d) was not found.", country, code));
        this.countryCode = country;
        this.bankCode = code;
    }

    /**
     * Constructor.
     */
    public BankNotFoundException() {
    }

    /**
     * Get bank 2 letters country code.
     *
     * @return bank 2 letters country code
     */
    public final String getCountry() {
        return countryCode;
    }

    /**
     * Get bank code.
     *
     * @return bank code
     */
    public final Integer getCode() {
        return bankCode;
    }
}
