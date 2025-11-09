package com.example.cosmocats.domain.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Mock private HttpServletRequest request;

  @Test
  void handleExternalServiceException_ShouldReturnServiceUnavailable() {
    ExternalServiceException ex = new ExternalServiceException("External service down");
    when(request.getRequestURI()).thenReturn("/api/products");

    ResponseEntity<ApiErrorResponse> response = handler.handleExternalServiceException(ex, request);

    assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(503, response.getBody().getStatus());
  }

  @Test
  void handleConstraintViolation_ShouldReturnBadRequest() {
    Set<ConstraintViolation<?>> violations = new HashSet<>();
    ConstraintViolationException ex = new ConstraintViolationException(violations);
    when(request.getRequestURI()).thenReturn("/api/products");

    ResponseEntity<ApiErrorResponse> response = handler.handleConstraintViolation(ex, request);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  void handleAllExceptions_ShouldReturnInternalServerError() {
    Exception ex = new RuntimeException("Unexpected error");
    when(request.getRequestURI()).thenReturn("/api/products");

    ResponseEntity<ApiErrorResponse> response = handler.handleAll(ex, request);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
}
