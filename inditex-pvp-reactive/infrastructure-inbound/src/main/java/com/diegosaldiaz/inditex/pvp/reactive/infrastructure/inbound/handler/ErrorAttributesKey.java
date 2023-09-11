package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.handler;

import lombok.Getter;

@Getter
public enum ErrorAttributesKey{
  CODE("code"),
  MESSAGE("message"),
  TIME("timestamp");

  private final String key;
  ErrorAttributesKey(String key) {
    this.key = key;
  }
}
