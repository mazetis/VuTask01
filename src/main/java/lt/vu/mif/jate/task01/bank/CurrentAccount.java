/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.vu.mif.jate.task01.bank;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Current account class.
 *
 * @author Svajunas
 */
public class CurrentAccount extends Account {

    /**
     * Class constructor.
     *
     * @param ibanNo IBAN number string
     */
    public CurrentAccount(final String ibanNo) {
        super(ibanNo);
    }

    @Override
    public final void credit(final BigDecimal amount, final Currency currency) {
        super.creditBalance(amount, currency);
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
