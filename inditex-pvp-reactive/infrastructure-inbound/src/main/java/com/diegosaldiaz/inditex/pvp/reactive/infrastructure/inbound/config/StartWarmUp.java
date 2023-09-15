package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.config;

import com.diegosaldiaz.inditex.pvp.reactive.application.port.inbound.GetPvpUseCasePort;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Triggers a query to make sure all beans are loaded when the user make the first real API call.
 * The complete warm up might be configured out of the service, as an external call.
 * So this partial warm up has an example purpose.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StartWarmUp implements ApplicationListener<ApplicationReadyEvent> {
  private final GetPvpUseCasePort getPvpPort;

  /**
   * Handle an ApplicationReady event.
   *
   * @param event the event to respond to
   */
  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {

    getPvpPort.query(Integer.MAX_VALUE, Long.MAX_VALUE, OffsetDateTime.now().plusYears(1000).toInstant())
        .doOnError(e -> log.debug("Completed Service Warm Up."))
        .onErrorComplete()
        .subscribe();
  }
}
