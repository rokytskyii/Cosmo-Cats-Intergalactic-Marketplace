package com.example.cosmocats.domain.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CosmicWordValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CosmicWordCheck {
    String message() default "name must contain at least one cosmic word (e.g., star, galaxy, comet)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
