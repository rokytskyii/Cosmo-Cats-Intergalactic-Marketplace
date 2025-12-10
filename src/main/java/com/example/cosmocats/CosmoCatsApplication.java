package com.example.cosmocats;

import com.example.cosmocats.domain.config.FeatureToggleConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FeatureToggleConfig.class)
public class CosmoCatsApplication {
  public static void main(String[] args) {
    SpringApplication.run(CosmoCatsApplication.class, args);
  }
}
