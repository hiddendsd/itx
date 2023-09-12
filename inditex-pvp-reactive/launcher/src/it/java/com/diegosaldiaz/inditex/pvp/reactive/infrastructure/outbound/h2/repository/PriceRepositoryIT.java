package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.repository;

import com.diegosaldiaz.inditex.pvp.reactive.BaseIT;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.repository.PriceRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PriceRepositoryIT extends BaseIT {
  private static final int BRAND_ID = 1;
  private static final long PRODUCT_ID = 35455;
  private static final LocalDateTime START_DATE = LocalDateTime.of(2020,6,14,0,0,0);
  private static final LocalDateTime END_DATE = LocalDateTime.of(2020,12,31,23,59,59);

  @Autowired
  private PriceRepository repository;

  @Test
  void testNoPricesFound() {
    var result = repository.searchHigherPriorityPrices(BRAND_ID, PRODUCT_ID, LocalDateTime.now());
    StepVerifier.create(result).verifyComplete();
  }

  @Test
  void testOnePriceInDateRange() {
    // Arrange
    var date = LocalDateTime.of(2020,6,14,10,0,0);

    // Act
    var result = repository.searchHigherPriorityPrices(BRAND_ID, PRODUCT_ID, date);

    // Assert
    StepVerifier.create(result)
            .consumeNextWith( price ->
              assertThat(price.getPrice().setScale(2, RoundingMode.HALF_UP)).isEqualTo(BigDecimal.valueOf(35.5).setScale(2, RoundingMode.HALF_UP))
            ).verifyComplete();
  }

  @Test
  void testSeveralPricesInDateRangeReturnsHighestPriority() {
    // Arrange
    var date = LocalDateTime.of(2020,6,14,16,0,0);

    // Act
    var result = repository.searchHigherPriorityPrices(BRAND_ID, PRODUCT_ID, date);

    // Assert
    StepVerifier.create(result)
        .consumeNextWith( price ->
            assertThat(price.getPrice().setScale(2, RoundingMode.HALF_UP)).isEqualTo(BigDecimal.valueOf(25.45).setScale(2, RoundingMode.HALF_UP))
        ).verifyComplete();
  }

  @Test
  void testDateRangeEdgeJustBefore() {
    // Arrange
    var justBefore = START_DATE.minusNanos(1);

    // Act
    var result = repository.searchHigherPriorityPrices(BRAND_ID, PRODUCT_ID, justBefore);

    // Assert
    StepVerifier.create(result).verifyComplete();
  }

  @Test
  void testDateRangeEdgeExactStartTime() {
    // Act
    var result = repository.searchHigherPriorityPrices(BRAND_ID, PRODUCT_ID, START_DATE);

    // Assert
    StepVerifier.create(result)
        .consumeNextWith( price ->
            assertThat(price.getPrice().setScale(2, RoundingMode.HALF_UP)).isEqualTo(BigDecimal.valueOf(35.50).setScale(2, RoundingMode.HALF_UP))
        ).verifyComplete();
  }

  @Test
  void testDateRangeEdgeExactEndTime() {
    // Act
    var result = repository.searchHigherPriorityPrices(BRAND_ID, PRODUCT_ID, END_DATE);

    // Assert
    StepVerifier.create(result)
        .consumeNextWith( price ->
            assertThat(price.getPrice().setScale(2, RoundingMode.HALF_UP)).isEqualTo(BigDecimal.valueOf(38.95).setScale(2, RoundingMode.HALF_UP))
        ).verifyComplete();
  }

  @Test
  void testDateRangeEdgeJustAfter() {
    // Arrange
    var justAfter = END_DATE.plusNanos(1);

    // Act
    var result = repository.searchHigherPriorityPrices(BRAND_ID, PRODUCT_ID, justAfter);

    // Assert
    StepVerifier.create(result).verifyComplete();
  }

  @Test
  void testSeveralPricesSharingMaxPriority() {
    // Arrange
    var sharingPriorityDate = LocalDateTime.of(2021,6,15,0,0,0);

    // Act
    var result = repository.searchHigherPriorityPrices(BRAND_ID, PRODUCT_ID, sharingPriorityDate);

    // Assert
    StepVerifier.create(result)
        .expectNextCount(2)
        .verifyComplete();
  }

}
