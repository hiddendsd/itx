package com.diegosaldiaz.inditex.pvp.infrastructure.inbound.rest.controller;

import com.diegosaldiaz.inditex.pvp.application.exception.PriceNotFoundException;
import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.dto.ErrorDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdviseControllerTest {

  @Mock
  private WebRequest webRequest;

  private final AdviseController controller = new AdviseController();

  @Test
  void testPriceNotFoundHandler() {
    ResponseEntity<ErrorDto> response = controller.handlePriceNotFound(new PriceNotFoundException(1, 2, LocalDateTime.now()), webRequest);
    var errorDto =response.getBody();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(errorDto.getMessage()).contains("Price not found error for Brand [1] Product [2] date[");
    assertThat(errorDto.getCode()).isEqualTo("ITX-001");
    assertThat(errorDto.getRetryable()).isTrue();
  }

  @Test
  void testMethodArgumentNotValidExceptionWithFieldErrors() {
    String fieldName = "field name";
    String errorMsg = "error message";
    FieldError fieldError = new FieldError(fieldName, fieldName, errorMsg);
    List<FieldError> fieldErrorList = List.of(fieldError);
    MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
    when(exception.getFieldErrors()).thenReturn(fieldErrorList);

    ResponseEntity<ErrorDto> response = controller.handleMethodArgumentNotValidException(exception, webRequest);

    var errorDto =response.getBody();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(errorDto.getMessage()).isEqualTo(String.format("'%s': %s. ", fieldName, errorMsg));
    assertThat(errorDto.getCode()).isEqualTo("V-003");
    assertThat(errorDto.getRetryable()).isFalse();
  }

  @Test
  void testMethodArgumentNotValidExceptionWithoutFieldErrors() {
    String objectName = "field name";
    String errorMsg = "error message";
    ObjectError error = new ObjectError(objectName, errorMsg);
    List<ObjectError> errorList = List.of(error);
    MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
    when(exception.getAllErrors()).thenReturn(errorList);

    ResponseEntity<ErrorDto> response = controller.handleMethodArgumentNotValidException(exception, webRequest);

    var errorDto =response.getBody();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(errorDto.getMessage()).isEqualTo(String.format("'%s': %s. ", objectName, errorMsg));
    assertThat(errorDto.getCode()).isEqualTo("V-003");
    assertThat(errorDto.getRetryable()).isFalse();

  }

  @Test
  void testConstraintViolation() {
    ConstraintViolationException exception = mock(ConstraintViolationException.class);
    ConstraintViolation violation = mock(ConstraintViolation.class);
    Path path = mock(Path.class);
    when(exception.getConstraintViolations()).thenReturn(Set.of(violation));
    when(violation.getPropertyPath()).thenReturn(path);
    when(path.toString()).thenReturn("field");
    when(violation.getMessage()).thenReturn("error");
    ResponseEntity<ErrorDto> response = controller.handleConstraintViolationException(exception, webRequest);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("'field': error. ", response.getBody().getMessage());

    var errorDto =response.getBody();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(errorDto.getMessage()).isEqualTo("'field': error. ");
    assertThat(errorDto.getCode()).isEqualTo("V-001");
    assertThat(errorDto.getRetryable()).isFalse();
  }

  /*
   TODO

  @Test
  void testWrongParameterException() {
    String message = "message";
    ResponseEntity<ErrorDto> response = controller.handleWrongParameterException(new WrongParameterException(message), webRequest);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(message, response.getBody().getMessage());
  }

   */
}