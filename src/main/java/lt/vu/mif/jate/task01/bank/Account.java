/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.vu.mif.jate.task01.bank;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lt.vu.mif.jate.task01.bank.exception.NoFundsException;

/**
 * Abstract account class.
 *
 * @author Svajunas
 */
public abstract class Account {

    /**
     * IBAN of the account.
     */
    private final String ibanNo;
    /**
     * Bank of the account.
     */
    private final Bank bank;

    /**
     * Balance Map to hold account balances.
     */
    private Map<Currency, BigDecimal> balance = new HashMap<>();

    /**
     * IBAN number getter.
     *
     * @return IBAN number
     */
    public final String getIbanNo() {
        return ibanNo;
    }

    /**
     * Bank object getter.
     *
     * @return bank object
     */
    public final Bank getBank() {
        return bank;
    }

    /**
     * Constructor that creates Account object from provided IBAN String.
     *
     * @param iban full IBAN number in string format
     */
    public Account(final String iban) {
        Integer indexOfBankCode = IbanPattern.getIbanPatterns()
                .get(iban.substring(0, 2))
                .getIndexOfBankCode();
        Integer lastIndexOfBankCode = IbanPattern.getIbanPatterns()
                .get(iban.substring(0, 2))
                .getLastIndexOfBankCode() + 1;
        this.ibanNo = iban;
        this.bank = Banking.getInstance()
                .getOrAddBank(iban.substring(0, 2),
                        Integer.parseInt(iban
                                .substring(indexOfBankCode,
                                        lastIndexOfBankCode)));
        Map<Currency, BigDecimal> accountBalance = new HashMap<>();
        this.balance = accountBalance;
    }

    /**
     * Account number getter.
     *
     * @return account number
     */
    public final BigInteger getNumber() {
        Integer indexOfAccountNumber = IbanPattern.getIbanPatterns()
                .get(ibanNo.substring(0, 2))
                .getIndexOfAccountNumber();
        Integer lastIndexOfAccountNumber = IbanPattern.getIbanPatterns()
                .get(ibanNo.substring(0, 2))
                .getLastIndexOfAccountNumber() + 1;
        String extractedAccountNo = ibanNo
                .substring(indexOfAccountNumber, lastIndexOfAccountNumber);
        BigInteger accountNumber = new BigInteger(extractedAccountNo);
        return accountNumber;
    }

    @Override
    public final String toString() {
        return ibanNo;
    }

    /**
     * Credit the account with chosen currency.
     *
     * @param amount amount to credit
     * @param currency currency code string
     */
    public final void credit(final BigDecimal amount, final String currency) {
        Currency currencyReal = Currency.getInstance(currency);
        credit(amount, currencyReal);
    }

    /**
     * Abstract credit method. Implemented in CreditAccount, CurrentAccount and
     * SavingsAccount subclasses.
     *
     * @param amount amount to credit
     * @param currency currency object to credit
     */
    public abstract void credit(BigDecimal amount, Currency currency);

    /**
     * Credit the account with chosen currency.
     *
     * @param amount amount to credit
     * @param currency currency object to credit
     */
    public final void creditBalance(final BigDecimal amount,
            final Currency currency) {
        Converter.getInstance().amountChecker(amount);
        if (!balance.containsKey(currency)) {
            balance.put(currency, amount);
        } else {
            balance.merge(currency, amount, BigDecimal::add);
        }
    }

    /**
     * Get balance of the current account of chosen currency.
     *
     * @param currency currency code string
     * @return amount of chosen currency in the balance
     */
    public final BigDecimal balance(final String currency) {
        Currency currencyReal = Currency.getInstance(currency);
        if (balance.containsKey(currencyReal)) {
            BigDecimal amount = balance.get(currencyReal);
            return amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        } else {
            BigDecimal amount = new BigDecimal(0);
            return amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        }
    }

    /**
     * Get total balance of the current account converted to chosen currency.
     *
     * @param currency currency code string
     * @return total balance converted to chosen currency
     */
    public final BigDecimal balanceAll(final String currency) {
        List<BigDecimal> balanceList = new ArrayList<BigDecimal>();
        balance.keySet().stream().map((key) -> {
            Currency currencyName = key;
            Currency currencyFrom = Currency.getInstance(currency);
            BigDecimal amount = balance.get(key);
            BigDecimal converted = Converter.getInstance()
                    .convert(amount, currencyName, currencyFrom);
            return converted;
        }).forEachOrdered((converted) -> {
            balanceList.add(converted);
        });
        BigDecimal result = balanceList.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return result;
    }

    /**
     * Debit the account with chosen currency.
     *
     * @param amount amount to debit
     * @param currency currency code string to debit
     */
    public final void debit(final BigDecimal amount, final String currency) {
        Currency currencyReal = Currency.getInstance(currency);
        debit(amount, currencyReal);
    }

    /**
     * Abstract debit method. Implemented in CreditAccount, CurrentAccount and
     * SavingsAccount subclasses.
     *
     * @param amount amount to debit
     * @param currency currency object to debit
     */
    public abstract void debit(BigDecimal amount, Currency currency);

    /**
     * Debit the account with chosen currency.
     *
     * @param amount amount to debit
     * @param currency currency object to debit
     */
    public final void debitBalance(final BigDecimal amount,
            final Currency currency) {
        if (!balance.containsKey(currency)) {
            throw new NoFundsException();
        }
        if (balance.get(currency).compareTo(amount) < 0) {
            throw new NoFundsException();
        }
        BigDecimal negativeAmount = amount.negate();
        balance.merge(currency, negativeAmount, BigDecimal::add);
    }

    /**
     * Convert currency from one to another within same account.
     *
     * @param amount amount to convert
     * @param currencyOne currency code string to convert from
     * @param currencyTwo currency code string to convert to
     */
    public final void convert(final BigDecimal amount,
            final String currencyOne, final String currencyTwo) {
        Currency currencyFrom = Currency.getInstance(currencyOne);
        debitBalance(amount, currencyFrom);
        Currency currencyTo = Currency.getInstance(currencyTwo);
        BigDecimal convertedAmount = Converter.getInstance()
                .convert(amount, currencyFrom, currencyTo);
        Converter.getInstance().amountChecker(amount);
        creditBalance(convertedAmount, currencyTo);
    }

    /**
     * Debit current account and credit another account with chosen currency.
     *
     * @param amount amount to transfer (debit)
     * @param currency currency code string to transfer
     * @param account account to credit
     */
    public final void debit(final BigDecimal amount,
            final String currency, final Account account) {
        Currency currencyReal = Currency.getInstance(currency);
        debit(amount, currencyReal, account);
    }

    /**
     * Abstract transfer to another account method. Implemented in
     * CreditAccount, CurrentAccount and SavingsAccount subclasses.
     *
     * @param amount amount to transfer (debit)
     * @param currency currency code string to transfer
     * @param account account to credit
     */
    public abstract void debit(final BigDecimal amount,
            final Currency currency, final Account account);

    /**
     * Debit current account and credit another account with chosen currency..
     *
     * @param amount amount to transfer (debit)
     * @param currency currency object to transfer
     * @param account account to credit
     */
    public final void debitAccount(final BigDecimal amount,
            final Currency currency, final Account account) {
        debitBalance(amount, currency);
        account.creditBalance(amount, currency);
    }
}
