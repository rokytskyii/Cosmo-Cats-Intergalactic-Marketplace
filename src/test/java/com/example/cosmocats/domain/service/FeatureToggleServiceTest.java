package com.example.cosmocats.domain.service;

import com.example.cosmocats.domain.config.FeatureToggleConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

  @InjectMocks
  private FeatureToggleService featureToggleService;

  @Test
  void isFeatureEnabled_WhenFeatureEnabled_ReturnsTrue() {
    String featureName = "cosmoCats.enabled";
    when(featureToggleConfig.isEnabled(featureName)).thenReturn(true);

    boolean result = featureToggleService.isFeatureEnabled(featureName);

    assertTrue(result);
  }

  @Test
  void isFeatureEnabled_WhenFeatureDisabled_ReturnsFalse() {
    String featureName = "kittyProducts.enabled";
    when(featureToggleConfig.isEnabled(featureName)).thenReturn(false);

    boolean result = featureToggleService.isFeatureEnabled(featureName);

    assertFalse(result);
  }

  @Test
  void isFeatureEnabled_WhenFeatureNotConfigured_ReturnsFalse() {
    String featureName = "unknown.feature";
    when(featureToggleConfig.isEnabled(featureName)).thenReturn(false);

    boolean result = featureToggleService.isFeatureEnabled(featureName);

    assertFalse(result);
  }

  @Test
  void isFeatureEnabled_WithDifferentFeatureNames_ReturnsCorrectValues() {
    when(featureToggleConfig.isEnabled("feature1")).thenReturn(true);
    when(featureToggleConfig.isEnabled("feature2")).thenReturn(false);
    when(featureToggleConfig.isEnabled("feature3")).thenReturn(true);

    assertTrue(featureToggleService.isFeatureEnabled("feature1"));
    assertFalse(featureToggleService.isFeatureEnabled("feature2"));
    assertTrue(featureToggleService.isFeatureEnabled("feature3"));

    verify(featureToggleConfig, times(3)).isEnabled(anyString());
  }

  @Test
  void isFeatureEnabled_WithNullFeatureName_ReturnsFalse() {
    when(featureToggleConfig.isEnabled(null)).thenReturn(false);

    boolean result = featureToggleService.isFeatureEnabled(null);

    assertFalse(result);
    verify(featureToggleConfig).isEnabled(null);
  }
}
