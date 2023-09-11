package com.diegosaldiaz.inditex.pvp.application.exception;

import java.time.LocalDateTime;

/**
 * Exception that represent the situation where no Price has been found.
 */
public class PriceNotFoundException extends InditexPvpServletException {

  public PriceNotFoundException(int brandId, long productId, LocalDateTime date) {
    super(InditexError.PVP_NOT_FOUND, brandId, productId, date);
  }
}
