package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.mapper;

import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.dto.PvpDto;
import com.diegosaldiaz.inditex.pvp.reactive.application.domain.model.Price;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PriceDomainModelToDtoMapperTest {

  @Spy
  private PriceDomainModelToDtoMapper mapper;

  @Test
  void testMapTime() {
    var instant = LocalDateTime.of(2023, 9, 11, 0, 0, 0);
    OffsetDateTime result = mapper.mapTime(instant);

    assertThat(result.getYear()).isEqualTo(2023);
    assertThat(result.getMonthValue()).isEqualTo(9);
    assertThat(result.getDayOfMonth()).isEqualTo(11);
    assertThat(result.getHour()).isZero();
    assertThat(result.getMinute()).isZero();
    assertThat(result.getSecond()).isZero();
  }

  @Test
  void testToDtoWithOffset() {
    var price = new Price(0, 0, LocalDateTime.now(), LocalDateTime.now(),0, 0, null, null);
    ZoneOffset offset = ZoneOffset.UTC;
    ZoneOffset newOffset = ZoneOffset.of("+05:00");
    var pvpDto = PvpDto.builder()
        .validFrom(OffsetDateTime.now(offset))
        .validTo(OffsetDateTime.now(offset))
        .build();
    when(mapper.toDto(price)).thenReturn(pvpDto);

    var result = mapper.toDto(price, newOffset);

    assertThat(result.getValidFrom().getOffset()).isEqualTo(newOffset);
  }
}