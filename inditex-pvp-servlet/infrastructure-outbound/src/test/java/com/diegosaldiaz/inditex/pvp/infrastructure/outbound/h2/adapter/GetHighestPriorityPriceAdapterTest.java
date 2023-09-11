package com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.adapter;

import com.diegosaldiaz.inditex.pvp.application.domain.model.Price;
import com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.entity.PriceEntity;
import com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.mapper.PriceEntityToDomainModelMapper;
import com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.repository.PriceRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class GetHighestPriorityPriceAdapterTest {

  private static final int BRAND_ID = 1;
  private static final long PRODUCT_ID = 2L;
  private static final LocalDateTime DATE = LocalDateTime.now();

  @Mock
  private PriceRepository priceRepository;

  @Mock
  private PriceEntityToDomainModelMapper mapper;

  @InjectMocks
  private GetHighestPriorityPriceAdapter adapter;

  @Test
  void testApply() {
    var priceEntity = PriceEntity.builder().build();
    var expectedPrice = Mockito.mock(Price.class);
    Mockito.when(priceRepository.findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(BRAND_ID, PRODUCT_ID, DATE, DATE))
        .thenReturn(Optional.of(priceEntity));
    Mockito.when(mapper.map(priceEntity))
        .thenReturn(expectedPrice);

    var result = adapter.apply(BRAND_ID, PRODUCT_ID, DATE);

    assertThat(result).containsSame(expectedPrice);
  }

  @Test
  void testApplyWhenNoPriceFound() {
    Mockito.when(priceRepository.findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(BRAND_ID, PRODUCT_ID, DATE, DATE))
        .thenReturn(Optional.empty());

    var result = adapter.apply(BRAND_ID, PRODUCT_ID, DATE);

    assertThat(result).isEmpty();
    Mockito.verify(mapper, Mockito.never()).map(any(PriceEntity.class));
  }
}
