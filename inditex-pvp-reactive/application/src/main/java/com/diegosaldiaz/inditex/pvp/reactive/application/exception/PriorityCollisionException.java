package com.diegosaldiaz.inditex.pvp.reactive.application.exception;

import java.time.Instant;

/**
 * Exception that represent the situation where there are more than one Price sharing the Max Priority.
 */
public class PriorityCollisionException extends InditexPvpReactiveException {

  public PriorityCollisionException(int brandId, long productId, Instant date, int priority, int numPrices) {
    super(InditexError.PRIORITY_COLLISION, numPrices, priority, brandId, productId, date);
  }
}
