package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.repository;

import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.entity.PriceEntity;
import java.math.BigInteger;
import java.time.LocalDateTime;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PriceRepository extends ReactiveCrudRepository<PriceEntity, Long> {

  @Query("""
    SELECT * 
    FROM PRICE 
    WHERE brand_id = :brandId 
        AND product_id = :productId 
        AND start_date <= :date 
        AND end_date >= :date 
    ORDER BY PRIORITY DESC 
    LIMIT 1
    """)
  Mono<PriceEntity> findByBrandIdAndProductIdAndStartDateLessThanEqual(int brandId, long productId, LocalDateTime date);
}
