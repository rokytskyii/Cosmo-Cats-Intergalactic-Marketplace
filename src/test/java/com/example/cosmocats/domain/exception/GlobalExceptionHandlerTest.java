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
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Mock private HttpServletRequest request;

  @Test
  void handleExternalServiceException_ShouldReturnServiceUnavailable() {
    ExternalServiceException ex = new ExternalServiceException("External service down");
    when(request.getRequestURI()).thenReturn("/api/products");

    ProblemDetail response = handler.handleExternalServiceException(ex, request);

    assertNotNull(response);
    assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), response.getStatus());
    assertEquals("External Service Error", response.getTitle());
    assertEquals(URI.create("/api/products"), response.getInstance());
  }

  @Test
  void handleConstraintViolation_ShouldReturnBadRequest() {
    Set<ConstraintViolation<?>> violations = new HashSet<>();
    ConstraintViolationException ex = new ConstraintViolationException(violations);
    when(request.getRequestURI()).thenReturn("/api/products");

    ProblemDetail response = handler.handleConstraintViolation(ex, request);

    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    assertEquals("Bad Request", response.getTitle());
  }

  @Test
  void handleAllExceptions_ShouldReturnInternalServerError() {
    Exception ex = new RuntimeException("Unexpected error");
    when(request.getRequestURI()).thenReturn("/api/products");

    ProblemDetail response = handler.handleAll(ex, request);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    assertEquals("Internal Server Error", response.getTitle());
  }
}
