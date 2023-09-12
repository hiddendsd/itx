package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.mapper;

import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.dto.PvpDto;
import com.diegosaldiaz.inditex.pvp.reactive.application.domain.model.Price;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Price (domain) to PriceDTO mapper.
 */

@Mapper(componentModel = "spring")
public interface PriceDomainModelToDtoMapper {


  /**
   * Map a Price to a PvpDto.
   * The validFrom and validTo dates, will use the given offset
   *
   * @param price Price domain object
   * @param offset ZoneOffset
   * @return PvpDto that uses the given offset for validFrom and validTo properties.
   */
  default PvpDto toDto(Price price, ZoneOffset offset) {
    PvpDto dto = toDto(price);
    dto.setValidFrom(dto.getValidFrom().withOffsetSameLocal(offset));
    dto.setValidTo(dto.getValidTo().withOffsetSameLocal(offset));
    return dto;
  }

  @Mapping(target = "pvp", source = "price")
  PvpDto toDto(Price price);

  default OffsetDateTime mapTime(LocalDateTime instant) {
    return instant.atOffset(ZoneOffset.UTC); // TODO
  }

}
