package com.diegosaldiaz.inditex.pvp.infrastructure.inbound.rest.controller;

import com.diegosaldiaz.inditex.pvp.application.exception.PriceNotFoundException;
import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.dto.ErrorDto;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Centralized management of API Error responses.
 */
@ControllerAdvice
@Slf4j
public class AdviseController {

  private static final String ERROR_CONSTRAINT_VIOLATION = "V-001";
  private static final String ERROR_MISSING_PARAMETER = "V-002";
  private static final String ERROR_ARGUMENT_NOT_VALID = "V-003";


  /**
   * Handle response when a NotImplementedException has been raised at any point.
   *
   * @param ex NotImplementedException raised
   * @param req WebRequest causing the exception
   * @return ResponseEntity with ErrorDto
   */
  @ExceptionHandler(PriceNotFoundException.class)
  public final ResponseEntity<ErrorDto> handlePriceNotFound(PriceNotFoundException ex, WebRequest req) {
    var errorDto = newErrorDto(ex.getMessage(), ex.getCode(), ex.isRetryable());
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(errorDto);
  }

  /**
   * Handle response when a MethodArgumentNotValidException has been raised at any point.
   *
   * @param ex MethodArgumentNotValidException raised
   * @param req WebRequest causing the exception
   * @return ResponseEntity with ErrorDto
   *         "buId": 16,
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public final ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest req) {
    final StringBuilder msg = new StringBuilder();
    if (ex.getFieldErrors().isEmpty()) {
      ex.getAllErrors().forEach(e -> msg.append(composeValidationErrorMessage(e.getObjectName(), e.getDefaultMessage())));
    } else {
      ex.getFieldErrors().forEach(fe -> msg.append(composeValidationErrorMessage(fe.getField(), fe.getDefaultMessage())));
    }
    var errorDto = newErrorDto(msg.toString(), ERROR_ARGUMENT_NOT_VALID, false);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorDto);
  }

  /**
   * Handle response when a ConstraintViolationException has been raised at any point.
   *
   * @param ex ConstraintViolationException raised
   * @param req WebRequest causing the exception
   * @return ResponseEntity with ErrorDto
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public final ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException ex, WebRequest req) {
    String msg = ex.getConstraintViolations().stream()
        .map(violation -> composeValidationErrorMessage(violation.getPropertyPath().toString(), violation.getMessage()))
        .collect(Collectors.joining());
    var errorDto = newErrorDto(msg, ERROR_CONSTRAINT_VIOLATION, false);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorDto);
  }

  /**
   * Handle response when a MissingServletRequestParameterException has been raised at any point.
   *
   * @param ex MissingServletRequestParameterException raised
   * @param req WebRequest causing the exception
   * @return ResponseEntity with ErrorDto
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public final ResponseEntity<ErrorDto> handleConstraintViolationException(MissingServletRequestParameterException ex, WebRequest req) {
    var errorDto = newErrorDto(ex.getBody().getDetail(), ERROR_MISSING_PARAMETER, false);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorDto);
  }

  private String composeValidationErrorMessage(String object, String message) {
    return String.format("'%s': %s. ", object, message);
  }

  private ErrorDto newErrorDto(String message, String code, boolean retryable) {
    return ErrorDto.builder()
        .message(message)
        .code(code)
        .retryable(retryable)
        .build();
  }
}
