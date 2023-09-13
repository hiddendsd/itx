package com.diegosaldiaz.inditex.pvp.config;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ObservationLogsHandler acts on the context pointcuts (on start, stop, error, scope open, scope close).
 */
@Component
@Slf4j
public class ObservationLogsHandler implements ObservationHandler<Observation.Context> {


  /**
   * Code executed on Start.
   *
   * @param context Observation.Context
   */
  @Override
  public void onStart(Observation.Context context) {
    /*
     * TODO. http://localhost:8081/actuator/metrics/http.server.requests
     */
    log.debug("Starting {}",  context.getName());
    context.put("time", System.currentTimeMillis());
  }

  /**
   * Code executed on Scope Opened.
   *
   * @param context Observation.Context
   */
  @Override
  public void onScopeOpened(Observation.Context context) {
    log.debug("Scope opened  {}", context.getName());
  }

  /**
   * Code executed onScopeClosed.
   *
   * @param context Observation.Context
   */
  @Override
  public void onScopeClosed(Observation.Context context) {
    log.debug("Scope closed {}", context.getName());
  }

  /**
   * Code executed On Stop.
   *
   * @param context Observation.Context
   */
  @Override
  public void onStop(Observation.Context context) {
    log.debug("Stopping {} duration {}", context.getName(), System.currentTimeMillis() - context.getOrDefault("time", 0L));
  }

  /**
   * Code executed On Error.
   *
   * @param context Observation.Context
   */
  @Override
  public void onError(Observation.Context context) {
    log.debug("Error {}", Objects.requireNonNull(context.getError()).getMessage());
  }

  /**
   * Is Support Context.
   *
   * @param context Observation.Context
   * @return boolean
   */
  @Override
  public boolean supportsContext(Observation.Context context) {
    return true;
  }
}