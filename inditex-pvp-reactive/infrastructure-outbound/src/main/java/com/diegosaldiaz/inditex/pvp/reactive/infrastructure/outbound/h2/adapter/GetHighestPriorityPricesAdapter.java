package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.adapter;

import com.diegosaldiaz.inditex.pvp.reactive.application.domain.model.Price;
import com.diegosaldiaz.inditex.pvp.reactive.application.port.outbound.GetHighestPriorityPricesPort;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.mapper.PriceEntityToDomainModelMapper;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.repository.PriceRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * TODO.
 */
@Service
@RequiredArgsConstructor
public class GetHighestPriorityPricesAdapter implements GetHighestPriorityPricesPort {

  private final PriceRepository priceRepository;
  private final PriceEntityToDomainModelMapper toModelMapper;

  @Override
  public Flux<Price> apply(final int brandId, final long productId, final LocalDateTime date) {
    return priceRepository.searchHigherPriorityPrices(brandId, productId, date)
        .map(toModelMapper::map);
  }
}
