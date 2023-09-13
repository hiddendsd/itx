package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.adapter;

import com.diegosaldiaz.inditex.pvp.reactive.application.domain.model.Price;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.entity.PriceEntity;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.mapper.PriceEntityToDomainModelMapper;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.repository.PriceRepository;
import java.time.LocalDateTime;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetHighestPriorityPricesAdapterTest {

  @Mock
  private PriceRepository priceRepository;
  @Mock
  private PriceEntityToDomainModelMapper toModelMapper;

  @InjectMocks
  private GetHighestPriorityPricesAdapter adapter;

  @Test
  void testApply() {
    var brandId = 1;
    var productId = 2L;
    var date = LocalDateTime.now();
    var entity = PriceEntity.builder().build();
    var price = new Price(brandId, productId, date, date, 0, 0, null, null);
    when(priceRepository.searchHigherPriorityPrices(brandId, productId, date))
        .thenReturn(Flux.just(entity)) ;

    when(toModelMapper.map(entity)).thenReturn(price);

    var result = adapter.apply(brandId, productId, date);

    StepVerifier.create(result)
        .expectNext(price)
        .verifyComplete();
  }
}