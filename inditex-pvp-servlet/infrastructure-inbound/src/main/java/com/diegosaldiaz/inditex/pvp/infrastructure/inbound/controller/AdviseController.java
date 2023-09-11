package com.diegosaldiaz.inditex.pvp.infrastructure.inbound.controller;

import com.diegosaldiaz.inditex.pvp.application.exception.PriceNotFoundException;
import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.dto.ErrorDto;
import jakarta.validation.ConstraintViolationException;
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
public class AdviseController {

  /**
   * Handle response when a NotImplementedException has been raised at any point.
   *
   * @param exception NotImplementedException raised
   * @param webRequest WebRequest causing the exception
   * @return ResponseEntity with ErrorDto
   */
  @ExceptionHandler(PriceNotFoundException.class)
  public final ResponseEntity<ErrorDto> handlePriceNotFound(PriceNotFoundException exception,
      WebRequest webRequest) {
    final ErrorDto errorDto = ErrorDto.builder().message(exception.getMessage()).build();
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(errorDto);
  }

  /**
   * Handle response when a MethodArgumentNotValidException has been raised at any point.
   *
   * @param exception MethodArgumentNotValidException raised
   * @param webRequest WebRequest causing the exception
   * @return ResponseEntity with ErrorDto
   *         "buId": 16,
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public final ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,
      WebRequest webRequest) {

    final StringBuilder msg = new StringBuilder();

    if (exception.getFieldErrors().isEmpty()) {
      exception.getAllErrors().forEach(e -> msg.append(composeValidationErrorMessage(e.getObjectName(), e.getDefaultMessage())));
    } else {
      exception.getFieldErrors().forEach(fe -> msg.append(composeValidationErrorMessage(fe.getField(), fe.getDefaultMessage())));
    }
    final ErrorDto errorDto = ErrorDto.builder().message(msg.toString()).build();
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorDto);
  }

  /**
   * Handle response when a ConstraintViolationException has been raised at any point.
   *
   * @param exception ConstraintViolationException raised
   * @param webRequest WebRequest causing the exception
   * @return ResponseEntity with ErrorDto
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public final ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException exception,
      WebRequest webRequest) {
    final StringBuilder msg = new StringBuilder();
    exception.getConstraintViolations().forEach(
        violation -> msg.append(composeValidationErrorMessage(violation.getPropertyPath().toString(), violation.getMessage())));
    final ErrorDto errorDto = ErrorDto.builder().message(msg.toString()).build();
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorDto);
  }

  /**
   * Handle response when a MissingServletRequestParameterException has been raised at any point.
   *
   * @param exception MissingServletRequestParameterException raised
   * @param webRequest WebRequest causing the exception
   * @return ResponseEntity with ErrorDto
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public final ResponseEntity<ErrorDto> handleConstraintViolationException(MissingServletRequestParameterException exception,
      WebRequest webRequest) {
    final ErrorDto errorDto = ErrorDto.builder().message(exception.getBody().getDetail()).build();
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorDto);
  }

  private String composeValidationErrorMessage(String object, String message) {
    return String.format("'%s': %s. ", object, message);
  }

}

