package com.diegosaldiaz.inditex.pvp.application.exception;

import lombok.Getter;

/**
 * Global Exception for the microservice.
 * All custom exceptions in this project must extend it.
 */
@Getter
public class InditexPvpServletException extends RuntimeException {

  private final String code;
  private final boolean retryable;

  /**
   * Constructor.
   *
   * @param error InditexError
   * @param args Arguments
   */
  public InditexPvpServletException(InditexError error, Object... args) {
    super(String.format(error.getMessage(), args));
    this.code = error.getCode();
    this.retryable = error.isRetryable();
  }
}
