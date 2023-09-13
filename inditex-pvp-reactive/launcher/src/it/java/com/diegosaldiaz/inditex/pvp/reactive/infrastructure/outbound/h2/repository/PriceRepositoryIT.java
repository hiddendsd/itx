package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.repository;

import com.diegosaldiaz.inditex.pvp.reactive.BaseIT;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PriceRepositoryIT extends BaseIT {
  private static final int BRAND_ID = 1;
  private static final long PRODUCT_ID = 35455;
  private static final Instant START_DATE = Instant.parse("2020-06-14T00:00:00Z");
  private static final Instant END_DATE = Instant.parse("2020:12:31T23:59:59Z");

  @Autowired
  private PriceRepository repository;

  @Test
  void testNoPricesFound() {
    var result = repository.searchHigherPriorityPrices(BRAND_ID, PRODUCT_ID, Instant.now());
    StepVerifier.create(result).verifyComplete();
  }

  @Test
  void testOnePriceInDateRange() {
    // Arrange
    var date = Instant.parse("2020-06-14T10:00:00Z");
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
    var date = Instant.parse("2020-06-14T16:00:00Z");

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
    var sharingPriorityDate = Instant.parse("2021-06-15T00:00:00Z");

    // Act
    var result = repository.searchHigherPriorityPrices(BRAND_ID, PRODUCT_ID, sharingPriorityDate);

    // Assert
    StepVerifier.create(result)
        .expectNextCount(2)
        .verifyComplete();
  }

}
