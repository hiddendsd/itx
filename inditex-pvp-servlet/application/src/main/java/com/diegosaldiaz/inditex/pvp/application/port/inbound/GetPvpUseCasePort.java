package com.diegosaldiaz.inditex.pvp.application.port.inbound;

import com.diegosaldiaz.inditex.pvp.application.domain.model.Price;
import java.time.LocalDateTime;

/**
 * Get PVP UserCase Inbound Port.
 */
@FunctionalInterface
public interface GetPvpUseCasePort {

  Price query(int brandId, long productId, LocalDateTime date);
}
