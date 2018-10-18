/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.vu.mif.jate.task01.bank.exception;

/**
 * IBAN exception. Thrown then submitted IBAN is incorrect.
 *
 * @author Svajunas.
 */
public class IBANException extends Exception {

    /**
     * bank IBAN.
     */
    private final String ibanNo;

    /**
     * Constructor.
     *
     * @param iban IBAN number
     */
    public IBANException(final String iban) {
        this.ibanNo = iban;

    }

    /**
     * Constructor with error message.
     *
     * @param iban IBAN number
     * @param error error message
     */
    public IBANException(final String iban, final String error) {
        super(error);
        this.ibanNo = iban;

    }

    /**
     * Get IBAN number.
     *
     * @return IBAN number
     */
    public final String getValue() {
        return ibanNo.replaceAll("\\s+", "");
    }
}
