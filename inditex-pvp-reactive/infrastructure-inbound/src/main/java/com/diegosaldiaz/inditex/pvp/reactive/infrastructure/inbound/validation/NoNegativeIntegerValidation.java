package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.validation;

import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.exception.ValidationException;
import java.util.function.BiConsumer;
import org.springframework.stereotype.Component;

/**
 * TODO.
 */
@Component
public class NoNegativeIntegerValidation implements BiConsumer<String, String> {

  @Override
  public void accept(String param, String value) {
    try {
      var v = Long.parseLong(value);
      if (v < 0) {
        throw new ValidationException(param + " can not be negative", false);
      }
    } catch (NumberFormatException e) {
      throw new ValidationException(param + " is not a number", false);
    }
  }
}
