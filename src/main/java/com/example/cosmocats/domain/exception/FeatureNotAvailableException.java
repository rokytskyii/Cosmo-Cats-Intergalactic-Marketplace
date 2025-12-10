package com.example.cosmocats.domain.exception;

public class FeatureNotAvailableException extends RuntimeException {

  private static final String MESSAGE_TEMPLATE = "Feature '%s' is currently disabled";

  public FeatureNotAvailableException(String featureName) {
    super(String.format(MESSAGE_TEMPLATE, featureName));
  }

  public FeatureNotAvailableException(String featureName, Throwable cause) {
    super(String.format(MESSAGE_TEMPLATE, featureName), cause);
  }
}