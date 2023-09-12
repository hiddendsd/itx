package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.validation;

import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.exception.ValidationException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class DateValidationTest {

  private final DateValidation target = new DateValidation();

  @Test
  void testValidObject() {
    assertDoesNotThrow(() -> target.accept("param", "2020-01-01T00:00:00Z"));
    assertDoesNotThrow(() -> target.accept("param", "2020-01-01T00:00:00+03:00"));
  }

  @Test
  void testInvalidObject() {
    assertThatThrownBy(() -> target.accept("param", "a")).isInstanceOf(ValidationException.class);
    assertThatThrownBy(() -> target.accept("param", "2020-01-01T00:00:00+3:00")).isInstanceOf(ValidationException.class);
  }
}