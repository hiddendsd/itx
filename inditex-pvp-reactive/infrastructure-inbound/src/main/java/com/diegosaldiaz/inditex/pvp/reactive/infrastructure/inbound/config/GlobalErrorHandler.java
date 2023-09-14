package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.config;

import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.dto.ErrorDto;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Global Error Handler.
 */
@Component
@Order(-2)
@Slf4j
public class GlobalErrorHandler extends AbstractErrorWebExceptionHandler {

  /**
   * Constructor.
   *
   * @param globalErrorAttributes GlobalErrorAttributes
   * @param applicationContext ApplicationContext
   * @param serverCodecConfigurer ServerCodecConfigurer
   */
  public GlobalErrorHandler(GlobalErrorAttributes globalErrorAttributes, ApplicationContext applicationContext,
                            ServerCodecConfigurer serverCodecConfigurer) {
    super(globalErrorAttributes, new WebProperties.Resources(), applicationContext);
    super.setMessageWriters(serverCodecConfigurer.getWriters());
    super.setMessageReaders(serverCodecConfigurer.getReaders());
  }

  @Override
  protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
    return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
  }

  private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {

    final Map<String, Object> errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());

    ErrorDto errorDto = ErrorDto.builder()
        .code(errorPropertiesMap.get(ErrorAttributesKey.CODE.getKey()).toString())
        .message(errorPropertiesMap.get(ErrorAttributesKey.MESSAGE.getKey()).toString())
        .retryable((Boolean) errorPropertiesMap.get(ErrorAttributesKey.RETRYABLE.getKey()))
        .build();
    log.warn("Error: {}. Request details: {}", errorDto, request);
    return ServerResponse.status((HttpStatusCode) errorPropertiesMap.get(ErrorAttributesKey.STATUS.getKey()))
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(errorDto));
  }
}
