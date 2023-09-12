package com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.mapper;

import com.diegosaldiaz.inditex.pvp.application.domain.model.Price;
import com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.entity.PriceEntity;
import java.util.stream.Stream;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper to convert PriceEntity objects into Price (domain model) objetcs.
 */
@Mapper(componentModel = "spring")
public interface PriceEntityToDomainModelMapper {

  Stream<Price> map(Stream<PriceEntity> entity);

  @Mapping(target = "validFrom", source = "startDate")
  @Mapping(target = "validTo", source = "endDate")
  @Mapping(target = "priceListId", source = "priceList")
  Price map(PriceEntity entity);
}
