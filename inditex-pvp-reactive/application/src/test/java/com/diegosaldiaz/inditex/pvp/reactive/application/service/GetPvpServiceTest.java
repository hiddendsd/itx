package com.diegosaldiaz.inditex.pvp.reactive.application.service;

import com.diegosaldiaz.inditex.pvp.reactive.application.domain.model.Price;
import com.diegosaldiaz.inditex.pvp.reactive.application.exception.PriceNotFoundException;
import com.diegosaldiaz.inditex.pvp.reactive.application.exception.PriorityCollisionException;
import com.diegosaldiaz.inditex.pvp.reactive.application.port.outbound.GetHighestPriorityPricesPort;
import java.math.BigDecimal;
import java.time.Instant;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetPvpServiceTest {

  private static final int BRAND_ID = 1;
  private static final long PRODUCT_ID = 2;
  private static final Instant DATE = Instant.now();

  @Mock
  private GetHighestPriorityPricesPort getPvpPort;

  @InjectMocks
  private GetPvpService service;

  @Test
  void testNotFound() {
    GetHighestPriorityPricesPort port = (a, b, c) -> Flux.empty();
    var service = new GetPvpService(port);

    var result = service.query(BRAND_ID, PRODUCT_ID, DATE);

    StepVerifier.create(result)
        .verifyError(PriceNotFoundException.class);
  }

  @Test
  void testGetPvp() {
    // Arrange
    var expectedPrice = new Price(BRAND_ID, PRODUCT_ID, DATE, DATE, 3, 4, BigDecimal.ONE, "EUR");
    final GetHighestPriorityPricesPort port = (a, b, c) -> Flux.just(expectedPrice);
    var service = new GetPvpService(port);

    // Act
    var result = service.query(BRAND_ID, PRODUCT_ID, DATE);

    // Assert
    StepVerifier.create(result)
        .expectNext(expectedPrice)
        .verifyComplete();
  }

  @Test
  void testMaxPriorityCollision() {
    // Arrange
    GetHighestPriorityPricesPort port = (a, b, c) -> Flux.just(
        new Price(BRAND_ID, PRODUCT_ID, DATE, DATE, 3, 4, BigDecimal.ONE, "EUR"),
        new Price(BRAND_ID, PRODUCT_ID, DATE, DATE, 3, 1, BigDecimal.ONE, "EUR"),
        new Price(BRAND_ID, PRODUCT_ID, DATE, DATE, 3, 4, BigDecimal.ONE, "EUR"));

    var service = new GetPvpService(port);

    // Act & Assert
    var result = service.query(BRAND_ID, PRODUCT_ID, DATE);

    StepVerifier.create(result)
        .verifyError(PriorityCollisionException.class);
  }

}