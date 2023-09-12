package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.exception;

import com.diegosaldiaz.inditex.pvp.reactive.application.exception.InditexPvpReactiveException;

/**
 * TODO.
 */
public class ValidationException extends InditexPvpReactiveException {
  private static final String CODE = "V-001";

  public ValidationException(String message, boolean retryable) {
    super(CODE, message, retryable);
  }
}
