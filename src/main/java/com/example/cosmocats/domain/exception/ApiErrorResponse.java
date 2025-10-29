package com.example.cosmocats.domain.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {
  private int status;
  private String error;
  private String message;
  private String path;
  private Instant timestamp = Instant.now();
  private List<String> details;

  public ApiErrorResponse(int status, String error, String message, String path) {
    this(status, error, message, path, Instant.now(), null);
  }

  public ApiErrorResponse(
      int status, String error, String message, String path, List<String> details) {
    this(status, error, message, path, Instant.now(), details);
  }
}
