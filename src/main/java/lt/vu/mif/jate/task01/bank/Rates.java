/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lt.vu.mif.jate.task01.bank;

import java.math.BigDecimal;

/**
 * Currency rates class.
 *
 * @author Svajunas
 */
public class Rates {

    /**
     * Currency rate to base currency.
     */
    private final BigDecimal rateToBase;
    /**
     * Currency rate from base currency.
     */
    private final BigDecimal rateFromBase;

    /**
     * Get rate to base currency.
     *
     * @return rate to base currency
     */
    public final BigDecimal getRateToBase() {
        return rateToBase;
    }

    /**
     * Get rate from base currency.
     *
     * @return rate from base currency
     */
    public final BigDecimal getRateFromBase() {
        return rateFromBase;
    }

    /**
     * Create currency rates object.
     *
     * @param rateToBases rate to base
     * @param rateFromBases rate from base
     */
    public Rates(final BigDecimal rateToBases, final BigDecimal rateFromBases) {
        this.rateToBase = rateToBases;
        this.rateFromBase = rateFromBases;
    }
}
