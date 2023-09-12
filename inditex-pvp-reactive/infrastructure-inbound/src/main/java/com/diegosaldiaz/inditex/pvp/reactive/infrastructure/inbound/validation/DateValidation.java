package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.validation;

import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.exception.ValidationException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.function.BiConsumer;
import org.springframework.stereotype.Component;

/**
 * TODO.
 */
@Component
public class DateValidation implements BiConsumer<String, String> {

  @Override
  public void accept(String param, String value) {
    try {
      OffsetDateTime.parse(value);
    } catch (DateTimeParseException e) {
      throw new ValidationException(param + " doesn't follow ISO-8601 format", false);
    }
  }
}