package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.handler;

import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.dto.GetPvp200ResponseDto;
import com.diegosaldiaz.inditex.pvp.reactive.application.port.inbound.GetPvpUseCasePort;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.exception.ValidationException;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.mapper.PriceDomainModelToDtoMapper;
import io.micrometer.observation.ObservationRegistry;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.observability.micrometer.Micrometer;
import reactor.core.publisher.Mono;

/**
 * Pvp request Handler.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PvpHandler {

  private final GetPvpUseCasePort getPvpPort;
  private final PriceDomainModelToDtoMapper mapper;
  private final ObservationRegistry registry;

  /**
   * Handle GET pvp requests.
   */
  public Mono<ServerResponse> get(ServerRequest request) {
    var brandId = Integer.parseInt(request.pathVariable("brandId"));
    var productId = Long.parseLong(request.pathVariable("productId"));
    var date = OffsetDateTime.parse(request.queryParam("date")
        .orElseThrow(() -> new ValidationException("Missing mandatory query field 'date'", false)))
        .toInstant();

    return getPvpPort.apply(brandId, productId, date)
        .map(mapper::toDto)
        .map(data -> GetPvp200ResponseDto.builder().data(data).build())
        .doOnNext(pvp -> log.info("PVP {} for request brandId[{}] brandId[{}] brandId[{}]", pvp, brandId, productId, date))
        .name("pvp.service")
        .tag("service.type", "reactive")
        .tap(Micrometer.observation(registry))
        .flatMap(pvp -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(pvp));
  }
}
