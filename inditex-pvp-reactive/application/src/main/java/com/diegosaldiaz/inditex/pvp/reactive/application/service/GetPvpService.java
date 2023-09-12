package com.diegosaldiaz.inditex.pvp.reactive.application.service;

import com.diegosaldiaz.inditex.pvp.reactive.application.domain.model.Price;
import com.diegosaldiaz.inditex.pvp.reactive.application.exception.PriceNotFoundException;
import com.diegosaldiaz.inditex.pvp.reactive.application.exception.PriorityCollisionException;
import com.diegosaldiaz.inditex.pvp.reactive.application.port.inbound.GetPvpUseCasePort;
import com.diegosaldiaz.inditex.pvp.reactive.application.port.outbound.GetHighestPriorityPricesPort;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
  public Mono<Price> apply(int brandId, long productId, LocalDateTime date) {
    return getPvpPort.apply(brandId, productId, date)
        .switchIfEmpty(Mono.error(new PriceNotFoundException(brandId, productId, date)))
        .collectList()
        .flatMap(elements ->
            elements.size() > 1
                ? Mono.error(new PriorityCollisionException(brandId, productId, date, elements.get(0).priority(), elements.size()))
                : Mono.just(elements.get(0))
            );
  }
}
