package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.config;

import lombok.Getter;

/**
 * Managed Error Attributes.
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
