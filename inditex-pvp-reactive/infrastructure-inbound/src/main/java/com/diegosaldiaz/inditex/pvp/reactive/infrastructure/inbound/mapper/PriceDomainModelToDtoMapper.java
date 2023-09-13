package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.mapper;

import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.dto.PvpDto;
import com.diegosaldiaz.inditex.pvp.reactive.application.domain.model.Price;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Price (domain) to PriceDTO mapper.
 */

@Mapper(componentModel = "spring")
public interface PriceDomainModelToDtoMapper {

  @Mapping(target = "pvp", source = "price")
  PvpDto toDto(Price price);

  default OffsetDateTime mapTime(Instant instant) {
    return instant.atOffset(ZoneOffset.UTC);
  }

}
