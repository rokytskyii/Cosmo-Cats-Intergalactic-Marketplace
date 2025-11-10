package com.example.cosmocats.domain.service;

import com.example.cosmocats.domain.config.FeatureToggleConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureToggleServiceTest {

  @Mock private FeatureToggleConfig featureToggleConfig;

  private FeatureToggleService featureToggleService;

  @BeforeEach
  void setUp() {
    featureToggleService = new FeatureToggleService(featureToggleConfig);
  }

  @Test
  void isFeatureEnabled_WhenFeatureEnabled_ReturnsTrue() {
    // Given
    String featureName = "cosmoCats.enabled";
    when(featureToggleConfig.isEnabled(featureName)).thenReturn(true);

    // When
    boolean result = featureToggleService.isFeatureEnabled(featureName);

    // Then
    assertTrue(result);
  }

  @Test
  void isFeatureEnabled_WhenFeatureDisabled_ReturnsFalse() {
    // Given
    String featureName = "kittyProducts.enabled";
    when(featureToggleConfig.isEnabled(featureName)).thenReturn(false);

    // When
    boolean result = featureToggleService.isFeatureEnabled(featureName);

    // Then
    assertFalse(result);
  }

  @Test
  void isFeatureEnabled_WhenFeatureNotConfigured_ReturnsFalse() {
    // Given
    String featureName = "unknown.feature";
    when(featureToggleConfig.isEnabled(featureName)).thenReturn(false);

    // When
    boolean result = featureToggleService.isFeatureEnabled(featureName);

    // Then
    assertFalse(result);
  }

  // Додайте цей тест до FeatureToggleServiceTest.java
  @Test
  void isFeatureEnabled_WithDifferentFeatureNames_ReturnsCorrectValues() {
    // Given - різні флагами
    when(featureToggleConfig.isEnabled("feature1")).thenReturn(true);
    when(featureToggleConfig.isEnabled("feature2")).thenReturn(false);
    when(featureToggleConfig.isEnabled("feature3")).thenReturn(true);

    // When & Then
    assertTrue(featureToggleService.isFeatureEnabled("feature1"));
    assertFalse(featureToggleService.isFeatureEnabled("feature2"));
    assertTrue(featureToggleService.isFeatureEnabled("feature3"));

    verify(featureToggleConfig, times(3)).isEnabled(anyString());
  }

  @Test
  void isFeatureEnabled_WithNullFeatureName_ReturnsFalse() {
    // Given
    when(featureToggleConfig.isEnabled(null)).thenReturn(false);

    // When
    boolean result = featureToggleService.isFeatureEnabled(null);

    // Then
    assertFalse(result);
    verify(featureToggleConfig).isEnabled(null);
  }
}
