package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.validation;

import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.exception.ValidationException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.function.BiConsumer;
import org.springframework.stereotype.Component;

/**
 * Validates if a String can be converted to a OffsetDateTime object.
 */
@Component
public class DateValidation implements BiConsumer<String, String> {

  /**
   * Validates if a String can be converted to a OffsetDateTime object.
   *
   * @param param The object identifier
   * @param value The value that want to be validated
   * @throws ValidationException when the value can not be converted to OffsetDateTime.
   */
  @Override
  public void accept(String param, String value) {
    try {
      OffsetDateTime.parse(value);
    } catch (DateTimeParseException e) {
      throw new ValidationException(param + " doesn't follow ISO-8601 format", false);
    }
  }
}