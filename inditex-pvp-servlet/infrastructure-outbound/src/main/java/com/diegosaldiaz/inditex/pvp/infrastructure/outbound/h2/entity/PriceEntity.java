package com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Price entity.
 */
@Entity
@Table(name = "price")
@Getter
@Setter
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
  private LocalDateTime startDate;

  @Column
  private LocalDateTime endDate;

  @Column
  private int priority;

  @Column
  private int priceList;

  @Column(scale = 20, precision = 2)
  private BigDecimal price;

  @Column
  private String currency;

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final PriceEntity that = (PriceEntity) o;
    if (id != null && that.id != null) {
      return Objects.equals(id, that.id);
    }
    return false;
  }

  @Override
  public int hashCode() {
    final int prime = 59;
    return prime + (id == null ? 43 : id.hashCode());
  }

  @Override
  public String toString() {
    return "PriceEntity{"
        + "id=" + id
        + ", productId=" + productId
        + ", brandId=" + brandId
        + ", startDate=" + startDate
        + ", endDate=" + endDate
        + ", priority=" + priority
        + ", priceList=" + priceList
        + ", price=" + price
        + ", currency='" + currency + '\''
        + '}';
  }
}
