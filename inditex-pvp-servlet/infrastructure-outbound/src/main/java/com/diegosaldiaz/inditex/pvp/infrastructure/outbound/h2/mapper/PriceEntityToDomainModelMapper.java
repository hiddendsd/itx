package com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.mapper;

import com.diegosaldiaz.inditex.pvp.application.domain.model.Price;
import com.diegosaldiaz.inditex.pvp.infrastructure.outbound.h2.entity.PriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper to convert PriceEntity objects into Price (domain model) objetcs.
 */
@Mapper(componentModel = "spring")
public interface PriceEntityToDomainModelMapper {

  @Mapping(target = "validFrom", source = "startDate")
  @Mapping(target = "validTo", source = "endDate")
  Price map(PriceEntity entity);
}
