package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.error;

import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.config.ErrorAttributesKey;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class ErrorAttributesKeyTest {

  @Test
  void testGetKey() {
    assertThat(ErrorAttributesKey.CODE.getKey()).isEqualTo("code");
    assertThat(ErrorAttributesKey.MESSAGE.getKey()).isEqualTo("message");
    assertThat(ErrorAttributesKey.RETRYABLE.getKey()).isEqualTo("retryable");
    assertThat(ErrorAttributesKey.STATUS.getKey()).isEqualTo("status");
  }
}