package com.diegosaldiaz.inditex.pvp.application.port.outbound;

import com.diegosaldiaz.inditex.pvp.application.domain.model.Price;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Get Highest Priority Price.
 * Outbound Port.
 * Returns the Price with highest Priority along all prices matching the input conditions.
 */
@FunctionalInterface
public interface GetHighestPriorityPricePort {

  Optional<Price> apply(int brandId, long productId, LocalDateTime date);
}
