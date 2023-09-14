package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.route;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.handler.PvpHandler;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.validation.DateValidation;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.validation.NoNegativeIntegerValidation;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * PVP endpoints router.
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class PvpRouter {

  private final NoNegativeIntegerValidation noNegativeIntegerValidation;
  private final DateValidation dateValidation;

  /**
   * Route endpoints.
   *
   * @param pvpHandler PvpHandler
   * @return handler response
   */
  @Bean
  public RouterFunction<ServerResponse> route(PvpHandler pvpHandler) {
    return RouterFunctions.route(
        GET("/pvp-api/v1/brands/{brandId}/products/{productId}/prices/pvp")
            .and(accept(MediaType.APPLICATION_JSON)), pvpHandler::get)
        .filter((request, next) -> {
          noNegativeIntegerValidation.accept("'brandId' path param", request.pathVariable("brandId"));
          noNegativeIntegerValidation.accept("'productId' path param", request.pathVariable("productId"));
          dateValidation.accept("'date' query param", request.queryParam("date").orElse(Strings.EMPTY));
          return next.handle(request);
        });
  }
}
