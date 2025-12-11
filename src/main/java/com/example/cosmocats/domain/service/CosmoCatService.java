package com.example.cosmocats.domain.service;

import com.example.cosmocats.domain.aspect.FeatureToggle;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CosmoCatService {

  public static final String FEATURE_COSMO_CATS = "cosmoCats.enabled";
  public static final String FEATURE_KITTY_PRODUCTS = "kittyProducts.enabled";

  @FeatureToggle(FEATURE_COSMO_CATS)
  @PreAuthorize("hasAuthority('SCOPE_read') or hasRole('API_USER')")
  public List<String> getCosmoCats() {
    return List.of("Astro Cat", "Galaxy Whiskers", "Comet Tail", "Nebula Paws");
  }

  @FeatureToggle(FEATURE_KITTY_PRODUCTS)
  public List<String> getKittyProducts() {
    return List.of("Space Litter Box", "Zero-Gravity Cat Tree", "Astral Scratching Post");
  }
}
