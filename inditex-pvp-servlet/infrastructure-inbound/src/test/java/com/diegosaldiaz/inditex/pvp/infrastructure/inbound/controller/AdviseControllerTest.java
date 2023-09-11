package com.diegosaldiaz.inditex.pvp.infrastructure.inbound.controller;

import com.diegosaldiaz.inditex.pvp.application.exception.PriceNotFoundException;
import com.diegosaldiaz.inditex.pvp.infrastructure.inbound.dto.ErrorDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
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
    ResponseEntity<ErrorDto> response = controller.handlePriceNotFound(new PriceNotFoundException(), webRequest);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    // TODO assertEquals(MSG, response.getBody().getMessage());
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

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(String.format("'%s': %s. ", fieldName, errorMsg), response.getBody().getMessage());
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

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(String.format("'%s': %s. ", objectName, errorMsg), response.getBody().getMessage());
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