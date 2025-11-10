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

    // Створюємо проксі з аспектом для тестування
    FeatureToggleAspect aspect = new FeatureToggleAspect(featureToggleService);
    AspectJProxyFactory factory = new AspectJProxyFactory(realCosmoCatService);
    factory.addAspect(aspect);
    proxiedCosmoCatService = factory.getProxy();
  }

  @Test
  void getCosmoCats_WhenFeatureEnabled_ReturnsListOfCats() {
    // Given - флаг увімкнений
    when(featureToggleConfig.isEnabled("cosmoCats.enabled")).thenReturn(true);

    // When - викликаємо метод через проксі
    List<String> result = proxiedCosmoCatService.getCosmoCats();

    // Then - перевіряємо результат
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(4, result.size());
    assertTrue(result.contains("Astro Cat"));
    assertTrue(result.contains("Galaxy Whiskers"));
    assertTrue(result.contains("Comet Tail"));
    assertTrue(result.contains("Nebula Paws"));

    // Перевіряємо, що мок був викликаний
    verify(featureToggleConfig).isEnabled("cosmoCats.enabled");
  }

  @Test
  void getCosmoCats_WhenFeatureDisabled_ThrowsException() {
    // Given - флаг вимкнений
    when(featureToggleConfig.isEnabled("cosmoCats.enabled")).thenReturn(false);

    // When & Then - очікуємо виключення
    FeatureNotAvailableException exception =
        assertThrows(
            FeatureNotAvailableException.class, () -> proxiedCosmoCatService.getCosmoCats());

    assertTrue(exception.getMessage().contains("cosmoCats.enabled"));
    assertTrue(exception.getMessage().contains("disabled"));
    verify(featureToggleConfig).isEnabled("cosmoCats.enabled");
  }

  @Test
  void getKittyProducts_WhenFeatureEnabled_ReturnsListOfProducts() {
    // Given - флаг увімкнений
    when(featureToggleConfig.isEnabled("kittyProducts.enabled")).thenReturn(true);

    // When - викликаємо метод через проксі
    List<String> result = proxiedCosmoCatService.getKittyProducts();

    // Then - перевіряємо результат
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
    // Given - флаг вимкнений
    when(featureToggleConfig.isEnabled("kittyProducts.enabled")).thenReturn(false);

    // When & Then - очікуємо виключення
    FeatureNotAvailableException exception =
        assertThrows(
            FeatureNotAvailableException.class, () -> proxiedCosmoCatService.getKittyProducts());

    assertTrue(exception.getMessage().contains("kittyProducts.enabled"));
    assertTrue(exception.getMessage().contains("disabled"));
    verify(featureToggleConfig).isEnabled("kittyProducts.enabled");
  }

  @Test
  void getCosmoCats_DirectCall_ReturnsListOfCats() {
    // When - викликаємо метод напряму (без аспекту)
    List<String> result = realCosmoCatService.getCosmoCats();

    // Then - перевіряємо результат
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(4, result.size());
    // Перевіряємо конкретний вміст списку
    assertEquals("Astro Cat", result.get(0));
    assertEquals("Galaxy Whiskers", result.get(1));
    assertEquals("Comet Tail", result.get(2));
    assertEquals("Nebula Paws", result.get(3));
  }

  @Test
  void getKittyProducts_DirectCall_ReturnsListOfProducts() {
    // When - викликаємо метод напряму (без аспекту)
    List<String> result = realCosmoCatService.getKittyProducts();

    // Then - перевіряємо результат
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
    // Проста перевірка, що сервіс може бути створений
    assertNotNull(realCosmoCatService);
    assertNotNull(proxiedCosmoCatService);
    assertNotNull(featureToggleService);
  }

  @Test
  void getCosmoCats_ReturnsImmutableList() {
    // When
    List<String> result = realCosmoCatService.getCosmoCats();

    // Then - перевіряємо, що список не можна змінити
    assertThrows(UnsupportedOperationException.class, () -> result.add("New Cat"));
  }

  @Test
  void getKittyProducts_ReturnsImmutableList() {
    // When
    List<String> result = realCosmoCatService.getKittyProducts();

    // Then - перевіряємо, що список не можна змінити
    assertThrows(UnsupportedOperationException.class, () -> result.add("New Product"));
  }
}
