package com.diegosaldiaz.inditex.pvp.application.service;

import com.diegosaldiaz.inditex.pvp.application.domain.model.Price;
import com.diegosaldiaz.inditex.pvp.application.exception.PriceNotFoundException;
import com.diegosaldiaz.inditex.pvp.application.port.inbound.GetPvpUseCasePort;
import com.diegosaldiaz.inditex.pvp.application.port.outbound.GetHighestPriorityPricePort;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Get PVP Service.
 * Returns the price with highest 'priority' that matches the input conditions:
 * - Brand ID
 * - Product ID
 * - Given date between START_DATE and END_DATE
 */
@Service
@RequiredArgsConstructor
public class GetPvpService implements GetPvpUseCasePort {

  private final GetHighestPriorityPricePort getPvpPort;

  /**
   * Returns the PVP price for a given product of a given brand in a given date.
   * The price with the highest priority value is returned when more than one price match the input conditions.
   *
   * @param brandId int with the Brand ID
   * @param productId long with the ProductID
   * @param date Instant with the date
   * @return Price with highest 'priority' value among all prices matching the input conditions.
   * @throws PriceNotFoundException when there is no Price for the input conditions.
   */
  // TODO Qué hacer cuando dos precios empatan a máxima prioridad?
  @Override
  public Price apply(final int brandId, final long productId, final Instant date) {
    return getPvpPort.apply(brandId, productId, date)
        .orElseThrow(PriceNotFoundException::new);
  }
}
