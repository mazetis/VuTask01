/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.vu.mif.jate.task01.bank;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lt.vu.mif.jate.task01.bank.exception.BankNotFoundException;
import java.util.Scanner;
import lt.vu.mif.jate.task01.bank.exception.IBANException;
import lt.vu.mif.jate.task01.bank.exception.WrongAccountTypeException;

/**
 * Banking facade class.
 *
 * @author Svajunas
 */
public class Banking {

    /**
     * Banking instance.
     */
    private static Banking banking;

    /**
     * Get banking instance.
     *
     * @return banking instance
     */
    public static synchronized Banking getInstance() {
        if (banking == null) {
            banking = new Banking();
        }
        return banking;
    }

    /**
     * Column number of bank code in the text file.
     */
    private static final int BANK_CODE_COLUMN = 3;

    /**
     * Banks Map to hold all banks.
     */
    private Map<String, Map<Integer, Bank>> banks = new HashMap<>();

    /**
     * Get banks Map. Return copy so that original Map would not be effected.
     *
     * @return banks Map copy
     */
    public final Map<String, Map<Integer, Bank>> getBanks() {
        Map<String, Map<Integer, Bank>> banksDuplicate = new HashMap<>();
        banks.entrySet().forEach((b) -> {
            banksDuplicate.put(b.getKey(), new HashMap<>(b.getValue()));
        });
        return banksDuplicate;
    }

    /**
     * Constructor. Loads banks objects from the file.
     */
    Banking() {
        File inputFile = new File("src/test/resources/banking/banks.txt");
        try {
            Scanner input = new Scanner(inputFile, "UTF8");
            Map<Integer, Bank> banksFromTxt = new HashMap<>();
            while (input.hasNext()) {
                String info = input.nextLine();
                String[] elements = info.split(":");
                String name = elements[0];
                String address = elements[1];
                String bicCode = elements[2];
                int code = Integer.parseInt(elements[BANK_CODE_COLUMN]);

                Bank bank = new Bank("LT", code, bicCode, name, address);
                banksFromTxt.put(code, bank);
            }
            banks.put("LT", banksFromTxt);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get bank object from the Map.
     *
     * @param countryCode bank 2 letters country code
     * @param bankCode bank code
     * @return bank object
     * @throws BankNotFoundException then bank is not in the list
     */
    public final Bank getBank(final String countryCode, final Integer bankCode)
            throws BankNotFoundException {
        if (banks.containsKey(countryCode)) {
            Bank bank = banks.get(countryCode).get(bankCode);
            if (bank == null) {
                throw new BankNotFoundException(countryCode, bankCode);
            }
            return bank;
        } else {
            throw new BankNotFoundException(countryCode, bankCode);
        }
    }

    /**
     * Get bank object if found, else create one and add.
     *
     * @param countryCode bank 2 letters country code
     * @param bankCode bank code
     * @return found or created bank object
     */
    public final Bank getOrAddBank(final String countryCode,
            final Integer bankCode) {
        try {
            Bank bank = getBank(countryCode, bankCode);
            return bank;
        } catch (BankNotFoundException ex) {
            Map<Integer, Bank> banksToAdd = new HashMap<>();
            Bank bank = new Bank(countryCode, bankCode);
            banksToAdd.put(bankCode, bank);
            banks.put(countryCode, banksToAdd);
            return bank;
        }
    }

    /**
     * Map of accounts list.
     */
    private Map<String, Account> accountsList = new HashMap<>();

    /**
     * Get Current Account object from the string.
     *
     * @param ibanNo IBAN number string
     * @return Current Account object
     * @throws IBANException then IBAN is wrong
     * @throws WrongAccountTypeException then IBAN belongs to other type account
     */
    public final CurrentAccount getCurrentAccount(final String ibanNo)
            throws IBANException, WrongAccountTypeException {
        String correctFormIban = IbanPattern.ibanChecker(ibanNo);
        if (!accountsList.containsKey(correctFormIban)) {
            CurrentAccount account = new CurrentAccount(correctFormIban);
            accountsList.put(correctFormIban, account);
            return account;
        }
        Account accountFound = accountsList.get(correctFormIban);
        if (accountFound instanceof CurrentAccount) {
            return (CurrentAccount) accountFound;
        }
        throw new WrongAccountTypeException("Account type was "
                + accountFound.getClass().getSimpleName());
    }

    /**
     * Get Credit Account object from the string.
     *
     * @param ibanNo IBAN number string
     * @return Credit Account object
     * @throws IBANException then IBAN is wrong
     * @throws WrongAccountTypeException then IBAN belongs to other type account
     */
    public final CreditAccount getCreditAccount(final String ibanNo)
            throws IBANException, WrongAccountTypeException {
        String correctFormIban = IbanPattern.ibanChecker(ibanNo);
        if (!accountsList.containsKey(correctFormIban)) {
            CreditAccount account = new CreditAccount(correctFormIban);
            accountsList.put(correctFormIban, account);
            return account;
        }
        Account accountFound = accountsList.get(correctFormIban);
        if (accountFound instanceof CreditAccount) {
            return (CreditAccount) accountFound;
        }
        throw new WrongAccountTypeException("Account type was "
                + accountFound.getClass().getSimpleName());
    }

    /**
     * Get Savings Account object from the string.
     *
     * @param ibanNo IBAN number string
     * @return Saving Account object
     * @throws IBANException then IBAN is wrong
     * @throws WrongAccountTypeException then IBAN belongs to other type account
     */
    public final SavingsAccount getSavingsAccount(final String ibanNo)
            throws IBANException, WrongAccountTypeException {
        String correctFormIban = IbanPattern.ibanChecker(ibanNo);
        if (!accountsList.containsKey(correctFormIban)) {
            SavingsAccount account = new SavingsAccount(correctFormIban);
            accountsList.put(correctFormIban, account);
            return account;
        }
        Account accountFound = accountsList.get(correctFormIban);
        if (accountFound instanceof SavingsAccount) {
            return (SavingsAccount) accountFound;
        }
        throw new WrongAccountTypeException("Account type was "
                + accountFound.getClass().getSimpleName());
    }

    /**
     * Get converter instance.
     *
     * @return converter instance
     */
    public final Converter getConverter() {
        return Converter.getInstance();
    }
}
