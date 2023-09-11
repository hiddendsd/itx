package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.handler;

import com.diegosaldiaz.inditex.pvp.reactive.application.domain.model.Price;
import com.diegosaldiaz.inditex.pvp.reactive.application.port.inbound.GetPvpUseCasePort;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
@RequiredArgsConstructor
public class PvpHandler {

  private final GetPvpUseCasePort getPvpPort;

  public Mono<ServerResponse> get(ServerRequest request) {

    OffsetDateTime date = OffsetDateTime.parse(request.queryParam("date").get());
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(getPvpPort.apply(Integer.parseInt(request.pathVariable("brandId")),
            Long.parseLong(request.pathVariable("productId")),
            date.toLocalDateTime()), Price.class);
  }
}
