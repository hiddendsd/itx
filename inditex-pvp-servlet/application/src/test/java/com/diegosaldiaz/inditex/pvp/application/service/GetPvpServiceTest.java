package com.diegosaldiaz.inditex.pvp.application.service;

import com.diegosaldiaz.inditex.pvp.application.domain.model.Price;
import com.diegosaldiaz.inditex.pvp.application.exception.PriceNotFoundException;
import com.diegosaldiaz.inditex.pvp.application.port.outbound.GetHighestPriorityPricePort;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GetPvpServiceTest {
  private static final int BRAND_ID = 1;
  private static final long PRODUCT_ID = 2;
  private static final Instant DATE = Instant.now();

  @Test
  void testGetPvp() {
    // Arrange
    var expectedPrice = new Price(BRAND_ID, PRODUCT_ID, DATE, DATE, 3, 4, BigDecimal.ONE, "EUR");
    final GetHighestPriorityPricePort port = (a, b, c) -> Optional.of(expectedPrice);
    var service = new GetPvpService(port);

    // Act
    var result = service.apply(BRAND_ID, PRODUCT_ID, Instant.now());

    // Assert
    assertThat(result).isSameAs(expectedPrice);
  }

  @Test
  void testGetPvpNoMatchedConditions() {
    // Arrange
    GetHighestPriorityPricePort port = (a, b, c) -> Optional.empty();
    var service = new GetPvpService(port);

    // Act & Assert
    assertThatThrownBy(() -> service.apply(BRAND_ID, PRODUCT_ID, DATE))
        .isInstanceOf(PriceNotFoundException.class);
    // TODO check message
  }
}