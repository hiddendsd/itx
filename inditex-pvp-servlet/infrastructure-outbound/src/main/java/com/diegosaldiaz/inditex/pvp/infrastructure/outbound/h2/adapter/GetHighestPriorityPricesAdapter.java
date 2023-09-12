package com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.adapter;

import com.diegosaldiaz.inditex.pvp.application.domain.model.Price;
import com.diegosaldiaz.inditex.pvp.application.port.outbound.GetHighestPriorityPricesPort;
import com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.mapper.PriceEntityToDomainModelMapper;
import com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.repository.PriceRepository;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * TODO.
 */
@Service
@RequiredArgsConstructor
public class GetHighestPriorityPricesAdapter implements GetHighestPriorityPricesPort {

  private final PriceRepository priceRepository;
  private final PriceEntityToDomainModelMapper toModelMapper;

  @Override
  public Stream<Price> apply(final int brandId, final long productId, final LocalDateTime date) {
    return priceRepository.searchHigherPriorityPrices(brandId, productId, date)
        .stream()
        .map(toModelMapper::map);
  }
}
