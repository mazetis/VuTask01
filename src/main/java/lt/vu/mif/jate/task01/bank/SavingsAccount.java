/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.vu.mif.jate.task01.bank;

import java.math.BigDecimal;
import java.util.Currency;
import lt.vu.mif.jate.task01.bank.exception.AccountActionException;

/**
 * Savings account class.
 *
 * @author Svajunas
 */
public class SavingsAccount extends Account {

    /**
     * Class constructor.
     *
     * @param ibanNo IBAN number string
     */
    public SavingsAccount(final String ibanNo) {
        super(ibanNo);
    }

    @Override
    public final void debit(final BigDecimal ammount,
            final Currency currency) {
        throw new AccountActionException();
    }

    @Override
    public final void debit(final BigDecimal ammount,
            final Currency currency, final Account account) {
        throw new AccountActionException();
    }

    @Override
    public final void credit(final BigDecimal amount, final Currency currency) {
        super.creditBalance(amount, currency);
    }
}
