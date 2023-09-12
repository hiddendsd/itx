package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.validation;

import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.exception.ValidationException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class NoNegativeIntegerValidationTest {

  private final NoNegativeIntegerValidation target = new NoNegativeIntegerValidation();

  @Test
  void testValidObject() {
    assertDoesNotThrow(() -> target.accept("param", "325347634"));
  }

  @Test
  void testInvalidObject() {
    assertThatThrownBy(() -> target.accept("param", "-123")).isInstanceOf(ValidationException.class);
    assertThatThrownBy(() -> target.accept("param", "sdfq")).isInstanceOf(ValidationException.class);
  }

}