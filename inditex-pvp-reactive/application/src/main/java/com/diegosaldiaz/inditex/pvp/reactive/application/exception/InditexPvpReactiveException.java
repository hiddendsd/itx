package com.diegosaldiaz.inditex.pvp.reactive.application.exception;

import lombok.Getter;

/**
 * Global Exception for the microservice.
 * All custom exceptions in this project must extend it.
 */
@Getter
public class InditexPvpReactiveException extends RuntimeException {

  private final String code;
  private final boolean retryable;

  /**
   * Constructor.
   *
   * @param error InditexError
   * @param args Arguments
   */
  public InditexPvpReactiveException(InditexError error, Object... args) {
    super(String.format(error.getMessage(), args));
    this.code = error.getCode();
    this.retryable = error.isRetryable();
  }

  /**
   * Constructor.
   *
   * @param code String
   * @param message String
   * @param retryable boolean
   */
  public InditexPvpReactiveException(String code, String message, boolean retryable) {
    super(message);
    this.code = code;
    this.retryable = retryable;
  }
}
