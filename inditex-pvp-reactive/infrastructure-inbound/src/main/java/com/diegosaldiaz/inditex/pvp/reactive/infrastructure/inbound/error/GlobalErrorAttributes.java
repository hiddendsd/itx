package com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.error;

import com.diegosaldiaz.inditex.pvp.reactive.application.exception.InditexPvpReactiveException;
import com.diegosaldiaz.inditex.pvp.reactive.application.exception.PriceNotFoundException;
import com.diegosaldiaz.inditex.pvp.reactive.application.exception.PriorityCollisionException;
import com.diegosaldiaz.inditex.pvp.reactive.infrastructure.inbound.exception.ValidationException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

/**
 * TODO.
 */
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

  private final List<ExceptionRule> exceptionsRules = List.of(
      new ExceptionRule(PriceNotFoundException.class, HttpStatus.NOT_FOUND),
      new ExceptionRule(PriorityCollisionException.class, HttpStatus.CONFLICT),
      new ExceptionRule(ValidationException.class, HttpStatus.BAD_REQUEST)
  );

  @Override
  public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
    Throwable error = getError(request);

    Optional<ExceptionRule> exceptionRuleOptional = exceptionsRules.stream()
        .map(exceptionRule -> exceptionRule.exceptionClass().isInstance(error) ? exceptionRule : null)
        .filter(Objects::nonNull)
        .findFirst();

    return exceptionRuleOptional.<Map<String, Object>>map(
            exceptionRule -> Map.of(
                ErrorAttributesKey.CODE.getKey(), ((InditexPvpReactiveException) error).getCode(),
                ErrorAttributesKey.MESSAGE.getKey(), error.getMessage(),
                ErrorAttributesKey.RETRYABLE.getKey(), ((InditexPvpReactiveException) error).isRetryable(),
                ErrorAttributesKey.STATUS.getKey(), exceptionRule.status()))
        .orElseGet(() -> Map.of(
            ErrorAttributesKey.CODE.getKey(), determineHttpStatus(error).value(),
            ErrorAttributesKey.MESSAGE.getKey(), error.getMessage(),
            ErrorAttributesKey.RETRYABLE.getKey(), true,
            ErrorAttributesKey.STATUS.getKey(), determineHttpStatus(error)));
  }


  private HttpStatus determineHttpStatus(Throwable error) {
    return error instanceof ResponseStatusException err
        ? HttpStatus.resolve(err.getStatusCode().value())
        : MergedAnnotations.from(error.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY)
        .get(ResponseStatus.class)
        .getValue(ErrorAttributesKey.CODE.getKey(), HttpStatus.class)
        .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
