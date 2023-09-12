package com.diegosaldiaz.inditex.pvp.reactive.application.port.outbound;

import com.diegosaldiaz.inditex.pvp.reactive.application.domain.model.Price;
import java.time.LocalDateTime;
import reactor.core.publisher.Flux;

/**
 * Get Highest Priority Price.
 * Outbound Port.
 * Returns the Price with highest Priority along all prices matching the input conditions.
 */
@FunctionalInterface
public interface GetHighestPriorityPricesPort {

  Flux<Price> apply(int brandId, long productId, LocalDateTime date);
}
