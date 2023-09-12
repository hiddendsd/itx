package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.error;

import lombok.Getter;

/**
 * TODO.
 */
@Getter
public enum ErrorAttributesKey {
  CODE("code"),
  MESSAGE("message"),
  RETRYABLE("retryable"),
  STATUS("status");

  private final String key;

  ErrorAttributesKey(String key) {
    this.key = key;
  }
}
