package com.example.cosmocats.domain.service;

import com.example.cosmocats.domain.config.FeatureToggleConfig;
import org.springframework.stereotype.Service;

@Service
public class FeatureToggleService {

  private final FeatureToggleConfig featureToggleConfig;

  public FeatureToggleService(FeatureToggleConfig featureToggleConfig) {
    this.featureToggleConfig = featureToggleConfig;
  }

  public boolean isFeatureEnabled(String featureName) {
    return featureToggleConfig.isEnabled(featureName);
  }
}
