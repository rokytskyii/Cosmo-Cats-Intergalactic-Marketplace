package com.example.cosmocats.domain.aspect;

import com.example.cosmocats.domain.exception.FeatureNotAvailableException;
import com.example.cosmocats.domain.service.FeatureToggleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeatureToggleAspectTest {

  @Mock private FeatureToggleService featureToggleService;

  private CosmoCatServiceTestProxy proxyService;

  // Test proxy class to simulate the aspect
  public static class CosmoCatServiceTestProxy {
    @FeatureToggle("test.feature")
    public String testMethod() {
      return "success";
    }
  }

  @BeforeEach
  void setUp() {
    FeatureToggleAspect aspect = new FeatureToggleAspect(featureToggleService);
    AspectJProxyFactory factory = new AspectJProxyFactory(new CosmoCatServiceTestProxy());
    factory.addAspect(aspect);
    proxyService = factory.getProxy();
  }

  @Test
  void checkFeatureToggle_WhenFeatureEnabled_ProceedsNormally() {
    // Given
    when(featureToggleService.isFeatureEnabled("test.feature")).thenReturn(true);

    // When
    String result = proxyService.testMethod();

    // Then
    assertEquals("success", result);
  }

  @Test
  void checkFeatureToggle_WhenFeatureDisabled_ThrowsException() {
    // Given
    when(featureToggleService.isFeatureEnabled("test.feature")).thenReturn(false);

    // When & Then
    FeatureNotAvailableException exception =
        assertThrows(FeatureNotAvailableException.class, () -> proxyService.testMethod());

    assertEquals("Feature 'test.feature' is currently disabled", exception.getMessage());
  }
}
