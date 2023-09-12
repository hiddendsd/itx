package com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.repository;

import com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.entity.PriceEntity;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Price Entity Repository.
 */
@Repository
public interface PriceRepository extends CrudRepository<PriceEntity, BigInteger> {

  /**
   * Returns the list of PriceEntities belonging to a given Brand and a given Product and having the maximum priority on a given date.
   * Examples:
   *  Case 1: there are three prices matching the input criteria with different priorities:
   *    - Priority 0
   *    - Priotity 1
   *    - Priotiry 2 <-- Returned
   *  Case 2: there are three prices matching the input criteria with more than one having the max priority:
   *    - Priority 2 <-- Returned
   *    - Priotity 1
   *    - Priotiry 2 <-- Returned
   *
   * @param brandId int BrandID
   * @param productId long ProductId
   * @param date LocalDateTime
   * @return List of PriceEntities matchig the input criteria and sharing the max(priority)
   */
  @Query(value = """
        SELECT a.*
        FROM price a
                 LEFT OUTER JOIN price b
                                 ON a.priority < b.priority AND a.brand_id = b.brand_id AND a.product_id = b.product_id AND
                                    b.start_date <= :date AND b.end_date >= :date
        WHERE b.id IS NULL
          AND a.brand_id = :brandId
          AND a.product_id = :productId
          AND a.start_date <= :date
          AND a.end_date >= :date
        ORDER BY id;
        
      """, nativeQuery = true)
  List<PriceEntity> searchHigherPriorityPrices(int brandId, long productId, LocalDateTime date);

}
