package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.handler;

import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.dto.GetPvp200ResponseDto;
import com.diegosaldiaz.inditex.pvp.reactive.application.port.inbound.GetPvpUseCasePort;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.mapper.PriceDomainModelToDtoMapper;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * TODO.
 */
@Component
@RequiredArgsConstructor
public class PvpHandler {

  private final GetPvpUseCasePort getPvpPort;
  private final PriceDomainModelToDtoMapper mapper;

  /**
   * TODO.
   */
  public Mono<ServerResponse> get(ServerRequest request) {
    var brandId = Integer.parseInt(request.pathVariable("brandId"));
    var productId = Long.parseLong(request.pathVariable("productId"));
    var date = OffsetDateTime.parse(request.queryParam("date").get());

    return getPvpPort.apply(brandId, productId, date.toLocalDateTime())
        .map(mapper::toDto)
        .map(data -> GetPvp200ResponseDto.builder().data(data).build())
        .flatMap(pvp -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(pvp));
  }
}
