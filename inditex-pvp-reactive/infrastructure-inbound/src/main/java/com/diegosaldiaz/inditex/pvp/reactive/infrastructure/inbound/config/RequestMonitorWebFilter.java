package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.config;

import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.context.ContextSnapshot;
import io.micrometer.observation.contextpropagation.ObservationThreadLocalAccessor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Observability Filter to work with WebFlux.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RequestMonitorWebFilter implements WebFilter {
  private final ObjectMapper objectMapper;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    long startTime = System.currentTimeMillis();
    return chain.filter(exchange)
        //Preparing context for the Tracer Span used in TracerConfiguration
        .contextWrite(context -> {
          ContextSnapshot.setThreadLocalsFrom(context, ObservationThreadLocalAccessor.KEY);
          return context;
        })
        // Logging the metrics for the API call, not really required to have this section for tracing setup
        .doFinally(signalType -> {
          long endTime = System.currentTimeMillis();
          long executionTime = endTime - startTime;
          // Extracting traceId added in TracerConfiguration Webfilter bean
          List<String> traceIds = ofNullable(exchange.getResponse().getHeaders().get("traceId")).orElse(List.of());
          MetricsLogTemplate metricsLogTemplate = new MetricsLogTemplate(
              String.join(",", traceIds),
              exchange.getLogPrefix().trim(),
              executionTime
          );
          try {
            log.debug(objectMapper.writeValueAsString(metricsLogTemplate));
          } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
          }
        });
  }

  record MetricsLogTemplate(String traceId, String logPrefix, long executionTime){}
}