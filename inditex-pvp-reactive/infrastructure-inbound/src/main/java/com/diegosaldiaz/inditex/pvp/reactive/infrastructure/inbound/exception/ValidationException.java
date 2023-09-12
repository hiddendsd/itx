package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.exception;

import com.diegosaldiaz.inditex.pvp.reactive.application.exception.InditexPvpReactiveException;

/**
 * Exception thrown when a Validation error occurs.
 */
public class ValidationException extends InditexPvpReactiveException {
  private static final String CODE = "V-001";

  /**
   * Constructor.
   *
   * @param message String
   * @param retryable boolean
   */
  public ValidationException(String message, boolean retryable) {
    super(CODE, message, retryable);
  }
}
