package com.example.cosmocats.domain.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "feature")
public class FeatureToggleConfig {

  private final Map<String, Boolean> toggles;

  public FeatureToggleConfig(Map<String, Boolean> toggles) {
    this.toggles = toggles;
  }

  public Map<String, Boolean> getToggles() {
    return toggles;
  }

  public Boolean isEnabled(String featureName) {
    return toggles.getOrDefault(featureName, false);
  }
}
