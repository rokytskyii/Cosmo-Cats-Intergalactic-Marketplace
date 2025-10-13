package com.example.cosmocats.domain.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class CosmicWordValidator implements ConstraintValidator<CosmicWordCheck, String> {

    private final List<String> cosmicWords = Arrays.asList("star", "galaxy", "comet", "moon", "planet", "cosmic",
            "nebula", "astral");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null)
            return true;
        String lower = value.toLowerCase();
        return cosmicWords.stream().anyMatch(lower::contains);
    }
}
