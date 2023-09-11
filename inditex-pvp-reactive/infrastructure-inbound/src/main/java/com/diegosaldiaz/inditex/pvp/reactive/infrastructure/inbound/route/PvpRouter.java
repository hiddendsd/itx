package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.route;

import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.handler.PvpHandler;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class PvpRouter {
  @Bean
  public RouterFunction<ServerResponse> route(PvpHandler pvpHandler) {
    return RouterFunctions.route(GET("/brands/{brandId}/products/{productId}/prices/pvp").and(accept(MediaType.APPLICATION_JSON)), pvpHandler::get);
  }
}
