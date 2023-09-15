package com.diegosaldiaz.inditex.pvp.application.service;

import com.diegosaldiaz.inditex.pvp.application.domain.model.Price;
import com.diegosaldiaz.inditex.pvp.application.exception.PriceNotFoundException;
import com.diegosaldiaz.inditex.pvp.application.exception.PriorityCollisionException;
import com.diegosaldiaz.inditex.pvp.application.port.outbound.GetHighestPriorityPricesPort;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GetPvpServiceTest {
  private static final int BRAND_ID = 1;
  private static final long PRODUCT_ID = 2;
  private static final LocalDateTime DATE = LocalDateTime.now();

  @Test
  void testGetPvp() {
    // Arrange
    var expectedPrice = new Price(BRAND_ID, PRODUCT_ID, DATE, DATE, 3, 4, BigDecimal.ONE, "EUR");
    final GetHighestPriorityPricesPort port = (a, b, c) -> Stream.of(expectedPrice);
    var service = new GetPvpService(port);

    // Act
    var result = service.query(BRAND_ID, PRODUCT_ID, DATE);

    // Assert
    assertThat(result).isSameAs(expectedPrice);
  }

  @Test
  void testGetPvpNoMatchedConditions() {
    // Arrange
    GetHighestPriorityPricesPort port = (a, b, c) -> Stream.empty();
    var service = new GetPvpService(port);

    // Act & Assert
    assertThatThrownBy(() -> service.query(BRAND_ID, PRODUCT_ID, DATE))
        .isInstanceOf(PriceNotFoundException.class);
  }

  @Test
  void testMaxPriorityCollision() {
    // Arrange
    GetHighestPriorityPricesPort port = (a, b, c) -> Stream.of(
        new Price(BRAND_ID, PRODUCT_ID, DATE, DATE, 3, 4, BigDecimal.ONE, "EUR"),
        new Price(BRAND_ID, PRODUCT_ID, DATE, DATE, 3, 1, BigDecimal.ONE, "EUR"),
        new Price(BRAND_ID, PRODUCT_ID, DATE, DATE, 3, 4, BigDecimal.ONE, "EUR"));
    var service = new GetPvpService(port);

    // Act & Assert
    assertThatThrownBy(() -> service.query(BRAND_ID, PRODUCT_ID, DATE))
        .isInstanceOf(PriorityCollisionException.class);
  }
}