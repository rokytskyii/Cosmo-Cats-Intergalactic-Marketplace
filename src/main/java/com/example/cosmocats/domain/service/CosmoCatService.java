package com.example.cosmocats.domain.service;

import com.example.cosmocats.domain.aspect.FeatureToggle;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CosmoCatService {

  @FeatureToggle("cosmoCats.enabled")
  public List<String> getCosmoCats() {
    return List.of("Astro Cat", "Galaxy Whiskers", "Comet Tail", "Nebula Paws");
  }

  @FeatureToggle("kittyProducts.enabled")
  public List<String> getKittyProducts() {
    return List.of("Space Litter Box", "Zero-Gravity Cat Tree", "Astral Scratching Post");
  }
}
