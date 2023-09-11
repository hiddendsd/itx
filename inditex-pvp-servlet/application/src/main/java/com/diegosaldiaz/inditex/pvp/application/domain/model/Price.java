package com.diegosaldiaz.inditex.pvp.application.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public record Price(int brandId, long productId, LocalDateTime validFrom, LocalDateTime validTo, long priceListId, int priority,
                    BigDecimal price, String currency) {

}
// TODO explicar por qué no un enum con las monedas.
