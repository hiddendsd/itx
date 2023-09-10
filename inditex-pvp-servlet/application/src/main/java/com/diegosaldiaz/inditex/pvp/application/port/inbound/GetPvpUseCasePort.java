package com.diegosaldiaz.inditex.pvp.application.port.inbound;

import com.diegosaldiaz.inditex.pvp.application.domain.model.Price;
import java.time.Instant;

/**
 * Get PVP UserCase Inbound Port.
 */
@FunctionalInterface
public interface GetPvpUseCasePort {

  Price apply(int brandId, long productId, Instant date);
}
