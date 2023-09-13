package com.diegosaldiaz.inditex.pvp.reactive.application.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Price Entity.
 *
 * @param brandId int with the Brand Identifier
 * @param productId long with the Product Identifier
 * @param validFrom Instant with the point in time where the price starts being available
 * @param validTo Instant with the point in time where the price is not available anymore
 * @param priceListId long with the PriceList identifier
 * @param priority int with the priority for the given price
 * @param price BigDecimal with the amount of money
 * @param currency String with the currency
 */
public record Price(int brandId, long productId, Instant validFrom, Instant validTo, long priceListId, int priority,
                    BigDecimal price, String currency) {

}
