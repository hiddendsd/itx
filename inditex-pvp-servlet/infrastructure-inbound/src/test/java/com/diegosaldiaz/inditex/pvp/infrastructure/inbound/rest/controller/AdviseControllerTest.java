package com.diegosaldiaz.inditex.pvp.infrastructure.inbound.rest.controller;

import com.diegosaldiaz.inditex.pvp.application.exception.PriceNotFoundException;
import com.diegosaldiaz.inditex.pvp.application.exception.PriorityCollisionException;
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
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.WebRequest;
import static org.assertj.core.api.Assertions.assertThat;
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
  void testPriorityCollisionHandler() {
    ResponseEntity<ErrorDto> response = controller.handlePriorityCollision(new PriorityCollisionException(1, 2, LocalDateTime.now(), 3, 4), webRequest);
    var errorDto =response.getBody();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(errorDto.getMessage()).contains("[4] prices share the max Priority [3] for Brand [1] Product [2] date[");
    assertThat(errorDto.getCode()).isEqualTo("ITX-002");
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

    var errorDto =response.getBody();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(errorDto.getMessage()).isEqualTo("'field': error. ");
    assertThat(errorDto.getCode()).isEqualTo("V-001");
    assertThat(errorDto.getRetryable()).isFalse();
  }

  @Test
  void testConstraintViolationForMissingParameter() {
    var msg = "a";
    var exception = mock(MissingServletRequestParameterException.class);
    var problemDetail = mock(ProblemDetail.class);
    when(exception.getBody()).thenReturn(problemDetail);
    when(problemDetail.getDetail()).thenReturn(msg);

    ResponseEntity<ErrorDto> response = controller.handleConstraintViolationException(exception, webRequest);

    var errorDto =response.getBody();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(errorDto.getMessage()).isEqualTo(msg);
    assertThat(errorDto.getCode()).isEqualTo("V-002");
    assertThat(errorDto.getRetryable()).isFalse();
  }
}
