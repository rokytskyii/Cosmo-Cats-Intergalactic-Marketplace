package com.example.cosmocats.domain.aspect;

import com.example.cosmocats.domain.exception.FeatureNotAvailableException;
import com.example.cosmocats.domain.service.FeatureToggleService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class FeatureToggleAspect {

  private final FeatureToggleService featureToggleService;

  public FeatureToggleAspect(FeatureToggleService featureToggleService) {
    this.featureToggleService = featureToggleService;
  }

  @Pointcut("@annotation(com.example.cosmocats.domain.aspect.FeatureToggle)")
  public void featureTogglePointcut() {}

  @Around("featureTogglePointcut()")
  public Object checkFeatureToggle(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    FeatureToggle featureToggle = method.getAnnotation(FeatureToggle.class);

    String featureName = featureToggle.value();

    if (!featureToggleService.isFeatureEnabled(featureName)) {
      throw new FeatureNotAvailableException("Feature '" + featureName + "' is currently disabled");
    }

    return joinPoint.proceed();
  }
}
