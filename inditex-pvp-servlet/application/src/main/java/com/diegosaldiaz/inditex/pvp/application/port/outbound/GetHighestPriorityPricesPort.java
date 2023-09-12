package com.diegosaldiaz.inditex.pvp.application.port.outbound;

import com.diegosaldiaz.inditex.pvp.application.domain.model.Price;
import java.time.LocalDateTime;
import java.util.stream.Stream;

/**
 * Get Highest Priority Price.
 * Outbound Port.
 * Returns the Price with highest Priority along all prices matching the input conditions.
 */
@FunctionalInterface
public interface GetHighestPriorityPricesPort {

  Stream<Price> apply(int brandId, long productId, LocalDateTime date);
}
