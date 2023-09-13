package com.diegosaldiaz.inditex.pvp.reactive.application.port.inbound;

import com.diegosaldiaz.inditex.pvp.reactive.application.domain.model.Price;
import java.time.Instant;
import reactor.core.publisher.Mono;

/**
 * Get PVP UserCase Inbound Port.
 */
@FunctionalInterface
public interface GetPvpUseCasePort {

  Mono<Price> apply(int brandId, long productId, Instant date);
}
