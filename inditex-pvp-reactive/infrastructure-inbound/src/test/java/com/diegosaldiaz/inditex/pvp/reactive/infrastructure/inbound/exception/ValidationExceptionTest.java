package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.exception;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class ValidationExceptionTest {

  @Test
  void testConstructor() {
    var msg = "MSG";
    var retryable = true;
    var ex = new ValidationException(msg, retryable);

    assertThat(ex.getCode()).isEqualTo("V-001");
    assertThat(ex).hasMessage(msg);
    assertThat(ex.isRetryable()).isEqualTo(retryable);
  }
}