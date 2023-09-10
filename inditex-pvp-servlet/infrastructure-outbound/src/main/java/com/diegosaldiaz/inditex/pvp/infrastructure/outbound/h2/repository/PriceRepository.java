package com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.repository;

import com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.entity.PriceEntity;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Price Entity Repository.
 */
@Repository
public interface PriceRepository extends CrudRepository<PriceEntity, BigInteger> {

  /**
   * Returns the PriceEntity belonging to a given Product and a given Brand having higher priority on a given date.
   *
   * @param brandId int BrandID
   * @param productId long ProductId
   * @param start Instant TODO
   * @param end Instant TODO
   * @return Optional PriceEntity with the selected price or Optional.empty if there is no price matching the input criterias
   */
  Optional<PriceEntity> findFirstByBrandIdAndProductIdAndStartDateBeforeAndEndDateAfterOrderByPriorityDesc(int brandId, long productId,
      Instant start, Instant end);
}
