package com.example.cosmocats.domain.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "feature")
public class FeatureToggleConfig {

  private Map<String, Boolean> toggles = new HashMap<>();

  public Map<String, Boolean> getToggles() {
    return toggles;
  }

  public void setToggles(Map<String, Boolean> toggles) {
    this.toggles = toggles;
  }

  public Boolean isEnabled(String featureName) {
    return toggles.getOrDefault(featureName, false);
  }
}
