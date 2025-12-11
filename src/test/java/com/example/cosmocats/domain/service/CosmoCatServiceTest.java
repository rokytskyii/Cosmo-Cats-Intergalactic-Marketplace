package com.example.cosmocats.domain.service;

import com.example.cosmocats.domain.aspect.FeatureToggleAspect;
import com.example.cosmocats.domain.config.FeatureToggleConfig;
import com.example.cosmocats.domain.exception.FeatureNotAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CosmoCatServiceTest {

  @Mock private FeatureToggleConfig featureToggleConfig;

  private FeatureToggleService featureToggleService;
  private CosmoCatService realCosmoCatService;
  private CosmoCatService proxiedCosmoCatService;

  @BeforeEach
  void setUp() {
    featureToggleService = new FeatureToggleService(featureToggleConfig);
    realCosmoCatService = new CosmoCatService();

    FeatureToggleAspect aspect = new FeatureToggleAspect(featureToggleService);
    AspectJProxyFactory factory = new AspectJProxyFactory(realCosmoCatService);
    factory.addAspect(aspect);
    proxiedCosmoCatService = factory.getProxy();
  }

  @Test
  void getCosmoCats_WhenFeatureEnabled_ReturnsListOfCats() {
    when(featureToggleConfig.isEnabled("cosmoCats.enabled")).thenReturn(true);

    List<String> result = proxiedCosmoCatService.getCosmoCats();

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(4, result.size());
    assertTrue(result.contains("Astro Cat"));
    assertTrue(result.contains("Galaxy Whiskers"));
    assertTrue(result.contains("Comet Tail"));
    assertTrue(result.contains("Nebula Paws"));

    verify(featureToggleConfig).isEnabled("cosmoCats.enabled");
  }

  @Test
  void getCosmoCats_WhenFeatureDisabled_ThrowsException() {
    when(featureToggleConfig.isEnabled("cosmoCats.enabled")).thenReturn(false);

    FeatureNotAvailableException exception =
        assertThrows(
            FeatureNotAvailableException.class, () -> proxiedCosmoCatService.getCosmoCats());

    assertTrue(exception.getMessage().contains("cosmoCats.enabled"));
    assertTrue(exception.getMessage().contains("disabled"));
    verify(featureToggleConfig).isEnabled("cosmoCats.enabled");
  }

  @Test
  void getKittyProducts_WhenFeatureEnabled_ReturnsListOfProducts() {
    when(featureToggleConfig.isEnabled("kittyProducts.enabled")).thenReturn(true);

    List<String> result = proxiedCosmoCatService.getKittyProducts();

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(3, result.size());
    assertTrue(result.contains("Space Litter Box"));
    assertTrue(result.contains("Zero-Gravity Cat Tree"));
    assertTrue(result.contains("Astral Scratching Post"));

    verify(featureToggleConfig).isEnabled("kittyProducts.enabled");
  }

  @Test
  void getKittyProducts_WhenFeatureDisabled_ThrowsException() {
    when(featureToggleConfig.isEnabled("kittyProducts.enabled")).thenReturn(false);

    FeatureNotAvailableException exception =
        assertThrows(
            FeatureNotAvailableException.class, () -> proxiedCosmoCatService.getKittyProducts());

    assertTrue(exception.getMessage().contains("kittyProducts.enabled"));
    assertTrue(exception.getMessage().contains("disabled"));
    verify(featureToggleConfig).isEnabled("kittyProducts.enabled");
  }

  @Test
  void getCosmoCats_DirectCall_ReturnsListOfCats() {
    List<String> result = realCosmoCatService.getCosmoCats();

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(4, result.size());
    assertEquals("Astro Cat", result.get(0));
    assertEquals("Galaxy Whiskers", result.get(1));
    assertEquals("Comet Tail", result.get(2));
    assertEquals("Nebula Paws", result.get(3));
  }

  @Test
  void getKittyProducts_DirectCall_ReturnsListOfProducts() {
    List<String> result = realCosmoCatService.getKittyProducts();

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(3, result.size());
    // Перевіряємо конкретний вміст списку
    assertEquals("Space Litter Box", result.get(0));
    assertEquals("Zero-Gravity Cat Tree", result.get(1));
    assertEquals("Astral Scratching Post", result.get(2));
  }

  @Test
  void serviceInitialization_ShouldWork() {
    assertNotNull(realCosmoCatService);
    assertNotNull(proxiedCosmoCatService);
    assertNotNull(featureToggleService);
  }

  @Test
  void getCosmoCats_ReturnsImmutableList() {
    List<String> result = realCosmoCatService.getCosmoCats();

    assertThrows(UnsupportedOperationException.class, () -> result.add("New Cat"));
  }

  @Test
  void getKittyProducts_ReturnsImmutableList() {
    List<String> result = realCosmoCatService.getKittyProducts();

    assertThrows(UnsupportedOperationException.class, () -> result.add("New Product"));
  }
}
