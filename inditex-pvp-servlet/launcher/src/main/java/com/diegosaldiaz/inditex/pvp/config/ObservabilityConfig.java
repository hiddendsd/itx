package com.diegosaldiaz.inditex.pvp.config;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Observability Configuration.
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class ObservabilityConfig {

  private final ObservationLogsHandler handler;

  /**
   * Creates the ObservedAspect Bean.
   *
   * @param observationRegistry ObservationRegistry
   * @return ObserverAspect
   */
  @Bean
  ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
    observationRegistry.observationConfig().observationHandler(handler);
    return new ObservedAspect(observationRegistry);
  }
}
