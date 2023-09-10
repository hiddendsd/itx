package com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Price entity.
 */
@Entity
@Table(name = "price")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private BigInteger id; // TODO explain

  @Column
  private long productId;

  @Column
  private int brandId;

  @Column
  private Instant startDate;

  @Column
  private Instant endDate;

  @Column
  private int priority;

  @Column
  private int priceList;

  @Column(scale = 20, precision = 2)
  private BigDecimal price;

  @Column
  private String currency;
}
