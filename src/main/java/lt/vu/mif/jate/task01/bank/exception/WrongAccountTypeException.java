/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.vu.mif.jate.task01.bank.exception;

/**
 * Wrong Account Type Exception.
 *
 * @author Svajunas.
 */
public class WrongAccountTypeException extends Exception {

    /**
     * Constructor with error message.
     *
     * @param error error message
     */
    public WrongAccountTypeException(final String error) {
        super(error);
    }
}
