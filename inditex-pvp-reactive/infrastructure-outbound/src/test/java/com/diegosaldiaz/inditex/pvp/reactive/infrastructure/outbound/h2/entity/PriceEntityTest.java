package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;

class PriceEntityTest {
  private static final BigInteger ID = BigInteger.ONE;
    private static final BigInteger DIFFERENT_ID = BigInteger.TWO;
    private static final int BRAND_ID = 1;
    private static final long PRODUCT_ID = 2;
    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2023, 11, 9, 14, 24, 35);
    private static final int PRIORITY = 5;
    private static final int PRICE_LIST = 6;
    private static final BigDecimal PRICE = BigDecimal.TEN;
    private static final String CURRENCY = "EUR";

    private final PriceEntity entity = newPriceEntity(ID);

    @Test
    void testEqualsSameObject() {
      assertThat(entity).isEqualTo(entity);
    }

    @Test
    void testEqualsNull() {
      assertThat(entity).isNotEqualTo(null);
    }

    @Test
    void testEqualsDifferentClass() {
      assertThat(entity).isNotEqualTo("String");
    }

    @Test
    void testEqualsOtherWithNullId() {
      var other = newPriceEntity(null);
      assertThat(entity).isNotEqualTo(other);
    }

    @Test
    void testEqualsOtherWithSamePropertiesButDifferentId() {
      var other = newPriceEntity(DIFFERENT_ID);
      assertThat(entity).isNotEqualTo(other);
    }

    @Test
    void testEqualsEntityWithIdNull() {
      var entityWithNullId = newPriceEntity(null);
      var other = newPriceEntity(ID);
      assertThat(entityWithNullId).isNotEqualTo(other);
    }

    @Test
    void testEqualsothEntityAndOtherWithNullId() {
      var entityWithNullId = newPriceEntity(null);
      var otherWithNullId = newPriceEntity(null);

      assertThat(entityWithNullId).isNotEqualTo(otherWithNullId);
    }

    @Test
    void testEqualsSameIdAndSameProperties() {
      var other = newPriceEntity(ID);
      assertThat(entity).isEqualTo(other);
    }

    @Test
    void testEqualsSameIdAndDifferentProperties() {
      var other = PriceEntity.builder()
          .id(ID)
          .brandId(BRAND_ID+1)
          .productId(PRODUCT_ID+2)
          .startDate(DATE_TIME.plusMinutes(1))
          .endDate(DATE_TIME.minusSeconds(23))
          .priority(PRIORITY - 4)
          .priceList(PRICE_LIST + 3)
          .price(PRICE.add(BigDecimal.TEN))
          .currency("USD")
          .build();

      assertThat(entity).isEqualTo(other);
    }

    @Test
    void testToString() {
      assertThat(entity).hasToString("PriceEntity{id=1, productId=2, brandId=1, startDate=2023-11-09T14:24:35, endDate=2023-11-09T14:24:35, priority=5, priceList=6, price=10, currency='EUR'}");
    }

    @Test
    void testHashCodeWhenIdNotNull() {
      assertThat(entity.hashCode()).isEqualTo(60);
    }

    @Test
    void testHashCodeWithIdNull() {
      var entityWithNullId = newPriceEntity(null);
      assertThat(entityWithNullId.hashCode()).isEqualTo(102);
    }

    @Test
    void testHashCodeIsSameWhenEqualsId() {
      var other = PriceEntity.builder().id(ID).build();
      assertThat(entity).hasSameHashCodeAs(other);
    }

    @Test
    void testProperties() {
      var target = new PriceEntity();
      target.setId(ID);
      assertThat(target.getId()).isEqualTo(ID);
      target.setBrandId(BRAND_ID);
      assertThat(target.getBrandId()).isEqualTo(BRAND_ID);
      target.setProductId(PRODUCT_ID);
      assertThat(target.getProductId()).isEqualTo(PRODUCT_ID);
      target.setStartDate(DATE_TIME);
      assertThat(target.getStartDate()).isEqualTo(DATE_TIME);
      target.setEndDate(DATE_TIME);
      assertThat(target.getEndDate()).isEqualTo(DATE_TIME);
      target.setPriority(PRIORITY);
      assertThat(target.getPriority()).isEqualTo(PRIORITY);
      target.setPriceList(PRICE_LIST);
      assertThat(target.getPriceList()).isEqualTo(PRICE_LIST);
      target.setPrice(PRICE);
      assertThat(target.getPrice()).isEqualTo(PRICE);
      target.setCurrency(CURRENCY);
      assertThat(target.getCurrency()).isEqualTo(CURRENCY);
    }

    private PriceEntity newPriceEntity(BigInteger id) {
      return PriceEntity.builder()
          .id(id)
          .brandId(BRAND_ID)
          .productId(PRODUCT_ID)
          .startDate(DATE_TIME)
          .endDate(DATE_TIME)
          .priority(PRIORITY)
          .priceList(PRICE_LIST)
          .price(PRICE)
          .currency(CURRENCY)
          .build();
    }
}