package com.diegosaldiaz.inditex.pvp.infrastructure.inbound.rest.controller;

import com.diegosaldiaz.inditex.pvp.application.port.inbound.GetPvpUseCasePort;
import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.controller.PvpApi;
import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.dto.GetPvp200ResponseDto;
import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.rest.mapper.PriceDomainModelToDtoMapper;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * PVP Rest Controller.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class PvpController implements PvpApi {

  private final GetPvpUseCasePort getPvpPort;
  private final PriceDomainModelToDtoMapper priceMapper;

  @Override
  public ResponseEntity<GetPvp200ResponseDto> getPvp(final Integer brandId, final Long productId, final OffsetDateTime date) {
    var pvp = getPvpPort.apply(brandId, productId, date.toLocalDateTime());
    var dto = priceMapper.toDto(pvp, date.getOffset());
    var body = GetPvp200ResponseDto.builder()
        .data(dto)
        .build();
    log.info("PVP {} for request brandId[{}] brandId[{}] brandId[{}]", dto, brandId, productId, date);
    return ResponseEntity.ok(body);
  }
}
