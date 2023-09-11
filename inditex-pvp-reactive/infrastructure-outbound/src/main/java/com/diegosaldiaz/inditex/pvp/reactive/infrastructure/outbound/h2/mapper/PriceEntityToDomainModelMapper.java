package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.mapper;

import com.diegosaldiaz.inditex.pvp.reactive.application.domain.model.Price;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.outbound.h2.entity.PriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper to convert PriceEntity objects into Price (domain model) objetcs.
 */
@Mapper(componentModel = "spring")
public interface PriceEntityToDomainModelMapper {

  @Mapping(target = "validFrom", source = "startDate")
  @Mapping(target = "validTo", source = "endDate")
  @Mapping(target = "priceListId", source = "priceList")
  Price map(PriceEntity entity);
}
