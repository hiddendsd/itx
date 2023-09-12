package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.validation;

import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.exception.ValidationException;
import java.util.function.BiConsumer;
import org.springframework.stereotype.Component;

/**
 * Validates whether a String can be converted to a no negative integer number.
 */
@Component
public class NoNegativeIntegerValidation implements BiConsumer<String, String> {

  /**
   * Validates whether a String can be converted to a no negative integer number.
   *
   * @param param The object identifier
   * @param value The value that want to be validated
   * @throws ValidationException when the value can not be converted to a positive number without decimals.
   */
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
