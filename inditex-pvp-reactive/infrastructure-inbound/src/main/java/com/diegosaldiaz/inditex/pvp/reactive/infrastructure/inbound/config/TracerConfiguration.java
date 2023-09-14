package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.config;

import brave.Span;
import brave.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.server.WebFilter;

/**
 * TracerConfiguration.
 * Adds the trace id as an output header.
 */
@Configuration(proxyBeanMethods = false)
@Profile("!test")
public class TracerConfiguration {

  @Bean
  WebFilter traceIdInResponseFilter(Tracer tracer) {
    return (exchange, chain) -> {
      Span currentSpan = tracer.currentSpan();
      if (currentSpan != null) {
        // putting trace id value in [traceId] response header
        exchange.getResponse().getHeaders().add("traceId", currentSpan.context().traceIdString());
      }
      return chain.filter(exchange);
    };
  }
}