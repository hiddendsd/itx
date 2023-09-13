package com.diegosaldiaz.inditex.pvp.infrastructure.inbound.rest.controller;

import com.diegosaldiaz.inditex.pvp.application.exception.PriceNotFoundException;
import com.diegosaldiaz.inditex.pvp.application.exception.PriorityCollisionException;
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
   * @return ResponseEntity with ErrorDto and NOT_FOUND status
   */
  @ExceptionHandler(PriceNotFoundException.class)
  public final ResponseEntity<ErrorDto> handlePriceNotFound(PriceNotFoundException ex, WebRequest req) {
    logWarn("Not Found", ex, req);
    var errorDto = newErrorDto(ex.getMessage(), ex.getCode(), ex.isRetryable());
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(errorDto);
  }

  /**
   * Handle response when a PriorityCollisionException has been raised at any point.
   *
   * @param ex PriorityCollisionException raised
   * @param req WebRequest causing the exception
   * @return ResponseEntity with ErrorDto and CONFLICT status
   */
  @ExceptionHandler(PriorityCollisionException.class)
  public final ResponseEntity<ErrorDto> handlePriorityCollision(PriorityCollisionException ex, WebRequest req) {
    logWarn("Price Collision", ex, req);
    var errorDto = newErrorDto(ex.getMessage(), ex.getCode(), ex.isRetryable());
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(errorDto);
  }

  /**
   * Handle response when a MethodArgumentNotValidException has been raised at any point.
   *
   * @param ex MethodArgumentNotValidException raised
   * @param req WebRequest causing the exception
   * @return ResponseEntity with ErrorDto and BAD_REQUEST
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public final ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest req) {
    logWarn("Not Valid Argument", ex, req);
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
   * @return ResponseEntity with ErrorDto and BAD_REQUEST
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public final ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException ex, WebRequest req) {
    logWarn("Constraint Violation", ex, req);
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
   * @return ResponseEntity with ErrorDto and BAD_REQUEST
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public final ResponseEntity<ErrorDto> handleConstraintViolationException(MissingServletRequestParameterException ex, WebRequest req) {
    logWarn("Constraint Violation", ex, req);
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

  private void logWarn(String description, Throwable ex, WebRequest req) {
    log.warn("Error: {}. Exception Details: {}. Request Details: {}", description, ex.getMessage(), req.toString());
  }
}

