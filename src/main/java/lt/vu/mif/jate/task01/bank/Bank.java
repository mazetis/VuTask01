package lt.vu.mif.jate.task01.bank;

import java.util.Locale;
import java.util.Objects;

/**
 * Bank class.
 *
 * @author Svajunas
 */
public class Bank {

    /**
     * Country code of the bank.
     */
    private String countryCode;
    /**
     * Bank code.
     */
    private Integer bankCode;
    /**
     * Bank BIC code.
     */
    private String bicCode;
    /**
     * Bank name.
     */
    private String bankName;
    /**
     * Bank address.
     */
    private String bankAddress;

    /**
     * Full bank constructor.
     *
     * @param country bank 2 letter country code
     * @param code bank code
     * @param bic bank BIC code
     * @param name bank name
     * @param address bank address
     */
    public Bank(final String country, final int code,
            final String bic, final String name, final String address) {
        this.countryCode = country;
        this.bankCode = code;
        this.bicCode = bic;
        this.bankName = name;
        this.bankAddress = address;
    }

    /**
     * Shorter bank constructor.
     *
     * @param country bank 2 letter country code
     * @param code bank code
     * @param bic bank BIC code
     * @param name bank name
     */
    public Bank(final String country, final int code,
            final String bic, final String name) {
        this(country, code, bic, name, null);
    }

    /**
     * Shorter bank constructor.
     *
     * @param country bank 2 letter country code
     * @param code bank code
     * @param bic bank BIC code
     */
    public Bank(final String country, final int code, final String bic) {
        this(country, code, bic, null);
    }

    /**
     * Shorter bank constructor.
     *
     * @param country bank 2 letter country code
     * @param code bank code
     */
    public Bank(final String country, final int code) {
        this(country, code, null);
    }

    /**
     * Shorter bank constructor.
     *
     * @param country bank 2 letter country code
     */
    public Bank(final String country) {
        this(country, 0);
    }

    /**
     * Get locale.
     *
     * @return locale
     */
    public final Locale getLocale() {
        return new Locale("en", countryCode);
    }

    /**
     * Get bank code.
     *
     * @return bank code
     */
    public final Integer getCode() {
        return bankCode;
    }

    /**
     * Get bank BIC code.
     *
     * @return BIC code
     */
    public final String getBicCode() {
        return bicCode;
    }

    /**
     * Get bank name.
     *
     * @return bank name
     */
    public final String getName() {
        return bankName;
    }

    /**
     * Get bank address.
     *
     * @return bank address
     */
    public final String getAddress() {
        return bankAddress;
    }

    /**
     * Prime non zero number for the hashCode.
     */
    private static final int PRIME_NON_ZERO = 5;

    /**
     * Another prime non zero number for the hashCode.
     */
    private static final int OTH_PRIME_NON_ZERO = 53;

    @Override
    public final int hashCode() {
        int hash = PRIME_NON_ZERO;
        hash = OTH_PRIME_NON_ZERO * hash + Objects.hashCode(this.countryCode);
        hash = OTH_PRIME_NON_ZERO * hash + Objects.hashCode(this.bankCode);
        return hash;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bank other = (Bank) obj;
        if (!Objects.equals(this.countryCode, other.countryCode)) {
            return false;
        }
        if (!Objects.equals(this.bankCode, other.bankCode)) {
            return false;
        }
        return true;
    }

    @Override
    public final String toString() {
        if (bankName != null) {
            return bankName;
        } else {
            if (bicCode != null) {
                return "Bank#" + bankCode + " (" + bicCode + "), "
                        + getLocale().getDisplayCountry();
            } else {
                return "Bank#" + bankCode + ", "
                        + getLocale().getDisplayCountry();
            }
        }
    }
}
