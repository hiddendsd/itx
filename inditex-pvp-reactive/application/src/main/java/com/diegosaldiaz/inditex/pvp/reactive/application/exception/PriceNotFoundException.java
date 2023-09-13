package com.diegosaldiaz.inditex.pvp.reactive.application.exception;

import java.time.Instant;

/**
 * Exception that represent the situation where no Price has been found.
 */
public class PriceNotFoundException extends InditexPvpReactiveException {

  public PriceNotFoundException(int brandId, long productId, Instant date) {
    super(InditexError.PVP_NOT_FOUND, brandId, productId, date);
  }
}
