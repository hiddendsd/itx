package com.diegosaldiaz.inditex.pvp.application.exception;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class InditexPvpServletExceptionTest {

  private final InditexPvpServletException target = new InditexPvpServletException(InditexError.PVP_NOT_FOUND, 1, 2, 3);

  @Test
  void testGetCode() {

    assertThat(target.getCode()).isEqualTo(InditexError.PVP_NOT_FOUND.getCode());
  }

  @Test
  void testRetryable() {
    assertThat(target.isRetryable()).isEqualTo(InditexError.PVP_NOT_FOUND.isRetryable());
  }
}
