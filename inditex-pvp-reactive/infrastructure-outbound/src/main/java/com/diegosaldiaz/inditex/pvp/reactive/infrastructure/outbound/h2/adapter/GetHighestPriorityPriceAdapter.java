package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.adapter;

import com.diegosaldiaz.inditex.pvp.reactive.application.domain.model.Price;
import com.diegosaldiaz.inditex.pvp.reactive.application.port.outbound.GetHighestPriorityPricePort;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.mapper.PriceEntityToDomainModelMapper;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.repository.PriceRepository;
import java.math.BigInteger;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetHighestPriorityPriceAdapter implements GetHighestPriorityPricePort {

  private final PriceRepository priceRepository;
  private final PriceEntityToDomainModelMapper toModelMapper;

  @Override
  public Mono<Price> apply(final int brandId, final long productId, final LocalDateTime date) {
//    return priceRepository.dsd()
//        .map(toModelMapper::map);

    //    return priceRepository.findById(2L)
//        .map(toModelMapper::map);

    return priceRepository.findByBrandIdAndProductIdAndStartDateLessThanEqual(brandId, productId, date)
        .map(toModelMapper::map);
  }
}
