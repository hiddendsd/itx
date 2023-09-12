package com.diegosaldiaz.inditex.pvp.config;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * TODO.
 */
@Component
@Slf4j
public class ObservationLogsHandler implements ObservationHandler<Observation.Context> {

  /**
   * TODO. http://localhost:8081/actuator/metrics/http.server.requests
   */
  @Override
  public void onStart(Observation.Context context) {
    log.debug("Starting {}",  context.getName());
    context.put("time", System.currentTimeMillis());
  }

  /**
   * TODO.
   */
  @Override
  public void onScopeOpened(Observation.Context context) {
    log.debug("Scope opened  {}", context.getName());
  }

  /**
   * TODO.
   */
  @Override
  public void onScopeClosed(Observation.Context context) {
    log.debug("Scope closed {}", context.getName());
  }

  /**
   * TODO.
   */
  @Override
  public void onStop(Observation.Context context) {
    log.debug("Stopping {} duration {}", context.getName(), System.currentTimeMillis() - context.getOrDefault("time", 0L));
  }

  /**
   * TODO.
   */
  @Override
  public void onError(Observation.Context context) {
    log.debug("Error {}", Objects.requireNonNull(context.getError()).getMessage());
  }

  /**
   * TODO.
   */
  @Override
  public boolean supportsContext(Observation.Context context) {
    return true;
  }
}