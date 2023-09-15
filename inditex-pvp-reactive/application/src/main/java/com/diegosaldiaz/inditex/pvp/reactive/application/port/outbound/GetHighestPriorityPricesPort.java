package com.diegosaldiaz.inditex.pvp.reactive.application.port.outbound;

import com.diegosaldiaz.inditex.pvp.reactive.application.domain.model.Price;
import java.time.Instant;
import reactor.core.publisher.Flux;

/**
 * Get Highest Priority Price.
 * Outbound Port.
 * Returns the Price with the highest Priority along all prices matching the input conditions.
 */
@FunctionalInterface
public interface GetHighestPriorityPricesPort {

  Flux<Price> query(int brandId, long productId, Instant date);
}
