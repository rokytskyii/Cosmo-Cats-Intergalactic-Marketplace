package com.example.cosmocats.domain.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidationExceptions(
          MethodArgumentNotValidException ex, HttpServletRequest request) {

    List<String> details = ex.getBindingResult().getFieldErrors().stream()
            .map(fe -> "Field '" + fe.getField() + "': " + fe.getDefaultMessage())
            .collect(Collectors.toList());

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
    problemDetail.setTitle("Bad Request");
    problemDetail.setInstance(URI.create(request.getRequestURI()));
    problemDetail.setProperty("errors", details);

    return problemDetail;
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ProblemDetail handleConstraintViolation(
          ConstraintViolationException ex, HttpServletRequest request) {

    List<String> details = ex.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.toList());

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Constraint violations");
    problemDetail.setTitle("Bad Request");
    problemDetail.setInstance(URI.create(request.getRequestURI()));
    problemDetail.setProperty("errors", details);

    return problemDetail;
  }

  @ExceptionHandler(ExternalServiceException.class)
  public ProblemDetail handleExternalServiceException(
          ExternalServiceException ex, HttpServletRequest request) {

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
    problemDetail.setTitle("External Service Error");
    problemDetail.setInstance(URI.create(request.getRequestURI()));

    return problemDetail;
  }

  @ExceptionHandler(FeatureNotAvailableException.class)
  public ProblemDetail handleFeatureNotAvailableException(
          FeatureNotAvailableException ex, HttpServletRequest request) {

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
    problemDetail.setTitle("Feature Disabled");
    problemDetail.setInstance(URI.create(request.getRequestURI()));

    return problemDetail;
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ProblemDetail handleResourceNotFoundException(
          ResourceNotFoundException ex, HttpServletRequest request) {

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    problemDetail.setTitle("Resource Not Found");
    problemDetail.setInstance(URI.create(request.getRequestURI()));

    return problemDetail;
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleAll(Exception ex, HttpServletRequest request) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    problemDetail.setTitle("Internal Server Error");
    problemDetail.setInstance(URI.create(request.getRequestURI()));

    return problemDetail;
  }
}