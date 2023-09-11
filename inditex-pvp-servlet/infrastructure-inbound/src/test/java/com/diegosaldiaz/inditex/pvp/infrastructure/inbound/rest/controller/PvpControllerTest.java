package com.diegosaldiaz.inditex.pvp.infrastructure.inbound.rest.controller;

import com.diegosaldiaz.inditex.pvp.application.domain.model.Price;
import com.diegosaldiaz.inditex.pvp.application.port.inbound.GetPvpUseCasePort;
import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.dto.PvpDto;
import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.rest.mapper.PriceDomainModelToDtoMapper;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PvpControllerTest {

  private static final int BRAND_ID = 1;

  private static final OffsetDateTime DATE = OffsetDateTime.now();
  private static final long PRODUCT_ID = 2;
  @Mock
  private GetPvpUseCasePort port;

  @Mock
  private PriceDomainModelToDtoMapper mapper;

  @InjectMocks
  private PvpController controller;

  @Test
  void testGetPvp() {
    var price = mock(Price.class);
    var pvpDto = PvpDto.builder().build();
    when(port.apply(BRAND_ID, PRODUCT_ID, DATE.toLocalDateTime())).thenReturn(price);
    when(mapper.toDto(price, DATE.getOffset())).thenReturn(pvpDto);

    var result = controller.getPvp(BRAND_ID, PRODUCT_ID, DATE);

    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody().getData()).isSameAs(pvpDto);
  }
}
