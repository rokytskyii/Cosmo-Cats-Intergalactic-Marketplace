package com.example.cosmocats.domain.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    List<String> details =
        ex.getBindingResult().getFieldErrors().stream()
            .map(fe -> "Field '" + fe.getField() + "': " + fe.getDefaultMessage())
            .collect(Collectors.toList());

    String msg = "Validation failed for object '" + ex.getBindingResult().getObjectName() + "'";
    ApiErrorResponse body =
        new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            msg,
            request.getRequestURI(),
            details);
    return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
      ConstraintViolationException ex, HttpServletRequest request) {
    List<String> details =
        ex.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.toList());

    ApiErrorResponse body =
        new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            "Validation failed (constraint violations)",
            request.getRequestURI(),
            details);
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ExternalServiceException.class)
  public ResponseEntity<ApiErrorResponse> handleExternalServiceException(
      ExternalServiceException ex, HttpServletRequest request) {
    ApiErrorResponse body =
        new ApiErrorResponse(
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI(),
            List.of("External product service is temporarily unavailable"));
    return new ResponseEntity<>(body, HttpStatus.SERVICE_UNAVAILABLE);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleAll(Exception ex, HttpServletRequest request) {
    ApiErrorResponse body =
        new ApiErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI(),
            null);
    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(FeatureNotAvailableException.class)
  public ResponseEntity<ApiErrorResponse> handleFeatureNotAvailableException(
      FeatureNotAvailableException ex, HttpServletRequest request) {
    ApiErrorResponse body =
        new ApiErrorResponse(
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
            ex.getMessage(),
            request.getRequestURI(),
            List.of("This feature is temporarily unavailable"));
    return new ResponseEntity<>(body, HttpStatus.SERVICE_UNAVAILABLE);
  }
}
