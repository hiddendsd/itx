package com.diegosaldiaz.inditex.pvp.application.exception;

import java.time.LocalDateTime;

/**
 * Exception that represent the situation where there are more than one Price sharing the Max Priority.
 */
public class PriorityCollisionException extends InditexPvpServletException {

  public PriorityCollisionException(int brandId, long productId, LocalDateTime date, int priority, int numPrices) {
    super(InditexError.PRIORITY_COLLISION, numPrices, priority, brandId, productId, date);
  }
}
