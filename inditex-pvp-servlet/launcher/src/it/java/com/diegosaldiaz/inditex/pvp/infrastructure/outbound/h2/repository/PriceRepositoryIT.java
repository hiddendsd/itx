package com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.diegosaldiaz.inditex.pvp.BaseIT;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
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
      assertThat(result).isEmpty();
  }

  @Test
  void testOnePriceInDateRange() {
    // Arrange
    var date = LocalDateTime.of(2020,6,14,10,0,0);

    // Act
    var result = repository.searchHigherPriorityPrices(BRAND_ID, PRODUCT_ID, date);

    // Assert
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getPrice().setScale(2, RoundingMode.HALF_UP)).isEqualTo(BigDecimal.valueOf(35.5).setScale(2, RoundingMode.HALF_UP));
  }

  @Test
  void testSeveralPricesInDateRangeReturnsHighestPriority() {
    // Arrange
    var date = LocalDateTime.of(2020,6,14,16,0,0);

    // Act
    var result = repository.searchHigherPriorityPrices(BRAND_ID, PRODUCT_ID, date);

    // Assert
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getPrice().setScale(2, RoundingMode.HALF_UP)).isEqualTo(BigDecimal.valueOf(25.45).setScale(2, RoundingMode.HALF_UP));
  }

  @Test
  void testDateRangeEdgeJustBefore() {
    // Arrange
    var justBefore = START_DATE.minusNanos(1);

    // Act
    var result = repository.searchHigherPriorityPrices(BRAND_ID, PRODUCT_ID, justBefore);

    // Assert
    assertThat(result).isEmpty();
  }

  @Test
  void testDateRangeEdgeExactStartTime() {
    // Act
    var result = repository.searchHigherPriorityPrices(BRAND_ID, PRODUCT_ID, START_DATE);

    // Assert
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getPrice().setScale(2, RoundingMode.HALF_UP)).isEqualTo(BigDecimal.valueOf(35.50).setScale(2, RoundingMode.HALF_UP));
  }

  @Test
  void testDateRangeEdgeExactEndTime() {
    // Act
    var result = repository.searchHigherPriorityPrices(BRAND_ID, PRODUCT_ID, END_DATE);

    // Assert
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getPrice().setScale(2, RoundingMode.HALF_UP)).isEqualTo(BigDecimal.valueOf(38.95).setScale(2, RoundingMode.HALF_UP));
  }

  @Test
  void testDateRangeEdgeJustAfter() {
    // Arrange
    var justAfter = END_DATE.plusNanos(1);

    // Act
    var result = repository.searchHigherPriorityPrices(BRAND_ID, PRODUCT_ID, justAfter);

    // Assert
    assertThat(result).isEmpty();
  }

  @Test
  void testSeveralPricesSharingMaxPriority() {
    // Arrange
    var sharingPriorityDate = LocalDateTime.of(2021,6,15,0,0,0);

    // Act
    var result = repository.searchHigherPriorityPrices(BRAND_ID, PRODUCT_ID, sharingPriorityDate);

    // Assert
    assertThat(result).hasSize(2);
  }

}
