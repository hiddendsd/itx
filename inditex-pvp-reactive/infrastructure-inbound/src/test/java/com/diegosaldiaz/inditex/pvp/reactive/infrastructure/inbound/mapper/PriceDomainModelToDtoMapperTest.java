package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.OffsetDateTime;
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
    var instant = Instant.parse("2020-01-02T03:04:05Z");
    OffsetDateTime result = mapper.mapTime(instant);

    assertThat(result.getYear()).isEqualTo(2020);
    assertThat(result.getMonthValue()).isEqualTo(1);
    assertThat(result.getDayOfMonth()).isEqualTo(2);
    assertThat(result.getHour()).isEqualTo(3);
    assertThat(result.getMinute()).isEqualTo(4);
    assertThat(result.getSecond()).isEqualTo(5);
  }
}
