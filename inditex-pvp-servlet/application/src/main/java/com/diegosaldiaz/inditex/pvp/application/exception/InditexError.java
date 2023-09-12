package com.diegosaldiaz.inditex.pvp.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Centralized definition of Inditex errors.
 */
@RequiredArgsConstructor
@Getter
public enum InditexError {
  PVP_NOT_FOUND("ITX-001", "Price not found error for Brand [%s] Product [%s] date[%s]", true),
  PRIORITY_COLLISION("ITX-002", "[%s] prices share the max Priority [%s] for Brand [%s] Product [%s] date[%s]. Unable to disambiguate",
      true);

  private final String code;
  private final String message;
  private final boolean retryable;
}
