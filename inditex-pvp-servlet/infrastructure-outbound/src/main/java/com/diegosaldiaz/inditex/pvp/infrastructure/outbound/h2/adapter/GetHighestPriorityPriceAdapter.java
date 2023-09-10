package com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.adapter;

import com.diegosaldiaz.inditex.pvp.application.domain.model.Price;
import com.diegosaldiaz.inditex.pvp.application.port.outbound.GetHighestPriorityPricePort;
import com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.mapper.PriceEntityToDomainModelMapper;
import com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.repository.PriceRepository;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * TODO.
 */
@Service
@RequiredArgsConstructor
public class GetHighestPriorityPriceAdapter implements GetHighestPriorityPricePort {

  private final PriceRepository priceRepository;
  private final PriceEntityToDomainModelMapper toModelMapper;

  @Override
  public Optional<Price> apply(final int brandId, final long productId, final Instant date) {
    return priceRepository
        .findFirstByBrandIdAndProductIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(brandId, productId, date, date)
        .map(toModelMapper::map);
  }
}
