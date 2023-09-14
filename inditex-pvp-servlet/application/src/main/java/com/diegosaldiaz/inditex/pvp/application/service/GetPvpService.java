package com.diegosaldiaz.inditex.pvp.application.service;

import com.diegosaldiaz.inditex.pvp.application.domain.model.Price;
import com.diegosaldiaz.inditex.pvp.application.exception.PriceNotFoundException;
import com.diegosaldiaz.inditex.pvp.application.exception.PriorityCollisionException;
import com.diegosaldiaz.inditex.pvp.application.port.inbound.GetPvpUseCasePort;
import com.diegosaldiaz.inditex.pvp.application.port.outbound.GetHighestPriorityPricesPort;
import io.micrometer.observation.annotation.Observed;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
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

  private final GetHighestPriorityPricesPort getPvpPort;

  /**
   * Returns the PVP price for a given product of a given brand in a given date.
   * The price with the highest priority value is returned when more than one price match the input conditions.
   *
   * @param brandId int with the Brand ID
   * @param productId long with the ProductID
   * @param date Instant with the date
   * @return Price with highest 'priority' value among all prices matching the input conditions.
   * @throws PriceNotFoundException when there is no Price for the input conditions.
   * @throws PriorityCollisionException when there are more than one price sharing the max priority (for the input conditions).
   */
  @Override
  @Observed(name = "pvp.service",
      contextualName = "getting-pvp",
      lowCardinalityKeyValues = {"service.type", "servlet"})
  public Price apply(final int brandId, final long productId, final LocalDateTime date) {
    return getPvpPort.apply(brandId, productId, date)
        .collect(
            Collectors.collectingAndThen(
                Collectors.toList(),
                list -> switch (list.size()) {
                  case 0 -> throw new PriceNotFoundException(brandId, productId, date);
                  case 1 -> list.get(0);
                  default -> throw new PriorityCollisionException(brandId, productId, date, list.get(0).priority(), list.size());
                })
        );
  }
}
