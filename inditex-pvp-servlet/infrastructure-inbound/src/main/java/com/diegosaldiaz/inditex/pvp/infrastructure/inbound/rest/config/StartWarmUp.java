package com.diegosaldiaz.inditex.pvp.infrastructure.inbound.rest.config;

import com.diegosaldiaz.inditex.pvp.application.exception.PriceNotFoundException;
import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.rest.controller.PvpController;
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

  private final PvpController controller;

  /**
   * Handle an ApplicationReady event.
   *
   * @param event the event to respond to
   */
  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    try {
      controller.getPvp(Integer.MAX_VALUE, Long.MAX_VALUE, OffsetDateTime.now().plusYears(1000));
    } catch (PriceNotFoundException e) {
      log.debug("Completed Service Warm Up.");
    }
  }
}
