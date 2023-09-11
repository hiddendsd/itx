package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.handler;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import java.util.Map;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalErrorHandler extends AbstractErrorWebExceptionHandler {
//  public GlobalErrorHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources, ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
//    super(errorAttributes, resources, applicationContext);
//    super.setMessageWriters(serverCodecConfigurer.getWriters());
//    super.setMessageReaders(serverCodecConfigurer.getReaders());
//  }

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

    int statusCode = Integer.parseInt(errorPropertiesMap.get(ErrorAttributesKey.CODE.getKey()).toString());
    return ServerResponse.status(HttpStatus.valueOf(statusCode))
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(errorPropertiesMap));
  }

//  public GlobalErrorWebExceptionHandler(GlobalErrorAttributes g, ApplicationContext applicationContext,
//                                        ServerCodecConfigurer serverCodecConfigurer) {
//    super(g, new WebProperties.Resources(), applicationContext);
//    super.setMessageWriters(serverCodecConfigurer.getWriters());
//    super.setMessageReaders(serverCodecConfigurer.getReaders());
//  }

//  @Override
//  protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
//    return RouterFunctions.route(
//        RequestPredicates.all(), this::renderErrorResponse);
//  }

//  private Mono<ServerResponse> renderErrorResponse(
//      ServerRequest request) {
//
//    Map<String, Object> errorPropertiesMap = getErrorAttributes(request,
//        ErrorAttributeOptions.defaults());
//
//    return ServerResponse.status(HttpStatus.BAD_REQUEST)
//        .contentType(MediaType.APPLICATION_JSON)
//        .body(BodyInserters.fromValue(errorPropertiesMap));
//  }
}
