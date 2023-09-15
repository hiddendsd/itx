package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.adapter;

import com.diegosaldiaz.inditex.pvp.reactive.application.domain.model.Price;
import com.diegosaldiaz.inditex.pvp.reactive.application.port.outbound.GetHighestPriorityPricesPort;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.mapper.PriceEntityToDomainModelMapper;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.repository.PriceRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * GetHighestPriorityPricesAdapter.
 * Returns the list of Prices matching the input criteria (brandId, productId and date)
 */
@Service
@RequiredArgsConstructor
public class GetHighestPriorityPricesAdapter implements GetHighestPriorityPricesPort {

  private final PriceRepository priceRepository;
  private final PriceEntityToDomainModelMapper toModelMapper;

  @Override
  public Flux<Price> query(final int brandId, final long productId, final Instant date) {
    return priceRepository.searchHigherPriorityPrices(brandId, productId, date)
        .map(toModelMapper::map);
  }
}
