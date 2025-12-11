package com.example.cosmocats.domain.aspect;

import com.example.cosmocats.BaseIntegrationTest;
import com.example.cosmocats.domain.exception.FeatureNotAvailableException;
import com.example.cosmocats.domain.service.FeatureToggleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Import(FeatureToggleAspectTest.TestService.class)
class FeatureToggleAspectTest extends BaseIntegrationTest {

  @MockitoSpyBean private FeatureToggleService featureToggleService;

  @Autowired private TestService testService;

  @TestComponent
  @Service
  static class TestService {
    @FeatureToggle("test.feature")
    public String testMethod() {
      return "success";
    }
  }

  @Test
  void checkFeatureToggle_WhenFeatureEnabled_ProceedsNormally() {
    when(featureToggleService.isFeatureEnabled("test.feature")).thenReturn(true);

    String result = testService.testMethod();

    assertEquals("success", result);
  }

  @Test
  void checkFeatureToggle_WhenFeatureDisabled_ThrowsException() {
    when(featureToggleService.isFeatureEnabled("test.feature")).thenReturn(false);

    FeatureNotAvailableException exception =
        assertThrows(FeatureNotAvailableException.class, () -> testService.testMethod());

    assertEquals("Feature 'test.feature' is currently disabled", exception.getMessage());
  }
}
