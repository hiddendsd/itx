package com.diegosaldiaz.inditex.pvp.reactive.application.exception;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class InditexPvpReactiveExceptionTest {

  @Test
  void testConstructor() {
    var code = "code";
    var msg = "msg";
    var retryble = true;
    var ex = new InditexPvpReactiveException(code, msg, retryble);

    assertThat(ex).hasMessage(msg);
    assertThat(ex.getCode()).isEqualTo(code);
    assertThat(ex.isRetryable()).isEqualTo(retryble);
  }
}