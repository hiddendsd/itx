package com.diegosaldiaz.inditex.pvp;

import com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.repository.PriceRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest(classes = InditexPvpServletMicroservice.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PriceRepositoryIT {
  private static final int BRAND_ID = 1;
  private static final long PRODUCT_ID = 35455;

  @Autowired
  private PriceRepository repository;

  @Test
  void testNoPricesFound() {
      var result = repository.findFirstByBrandIdAndProductIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(BRAND_ID, PRODUCT_ID, Instant.now(), Instant.now());
      assertThat(result).isEmpty();
  }

  @Test
  void testOnePriceInDateRange() {
    // Arrange
    var date = LocalDateTime.of(2020,6,14,10,00,00).toInstant(ZoneOffset.UTC);

    // Act
    var result = repository.findFirstByBrandIdAndProductIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(BRAND_ID, PRODUCT_ID, date, date);

    // Assert
    assertThat(result).isNotEmpty();
    assertThat(result.get().getPrice().setScale(2, RoundingMode.HALF_UP)).isEqualTo(BigDecimal.valueOf(35.5).setScale(2, RoundingMode.HALF_UP));
  }

  @Test
  void testSeveralPricesInDateRangeReturnsHighestPriority() {
    // Arrange
    var date = LocalDateTime.of(2020,6,14,16,0,0).toInstant(ZoneOffset.UTC);

    // Act
    var result = repository.findFirstByBrandIdAndProductIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(BRAND_ID, PRODUCT_ID, date, date);

    // Assert
    assertThat(result).isNotEmpty();
    assertThat(result.get().getPrice().setScale(2, RoundingMode.HALF_UP)).isEqualTo(BigDecimal.valueOf(25.45).setScale(2, RoundingMode.HALF_UP));
  }

}
