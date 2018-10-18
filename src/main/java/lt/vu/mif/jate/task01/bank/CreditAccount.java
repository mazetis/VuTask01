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
 * Credit account class.
 *
 * @author Svajunas
 */
public class CreditAccount extends Account {

    /**
     * Count of credit method calls.
     */
    private Integer count = 0;

    /**
     * Class constructor.
     *
     * @param ibanNo IBAN number string
     */
    public CreditAccount(final String ibanNo) {
        super(ibanNo);
    }

    @Override
    public final void credit(final BigDecimal ammount,
            final Currency currency) {
        count++;
        if (count < 2) {
            super.creditBalance(ammount, currency);
        } else {
            throw new AccountActionException();
        }
    }

    @Override
    public final void debit(final BigDecimal amount, final Currency currency) {
        super.debitBalance(amount, currency);
    }

    @Override
    public final void debit(final BigDecimal amount, final Currency currency,
            final Account account) {
        debit(amount, currency);
        account.credit(amount, currency);
    }
}
