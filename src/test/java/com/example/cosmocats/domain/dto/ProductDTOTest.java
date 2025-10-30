package com.example.cosmocats.domain.dto;

import com.example.cosmocats.domain.validator.CosmicWordCheck;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class ProductDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldValidate_WhenNameContainsCosmicWord() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Star Product");
        dto.setPrice(10.0);
        dto.setCategoryId(1L);

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldNotValidate_WhenNameDoesNotContainCosmicWord() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Regular Product");
        dto.setPrice(10.0);
        dto.setCategoryId(1L);

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("cosmic word"));
    }

    @Test
    void shouldNotValidate_WhenPriceIsNull() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Star Product");
        dto.setPrice(null);
        dto.setCategoryId(1L);

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldNotValidate_WhenPriceIsNegative() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Star Product");
        dto.setPrice(-1.0);
        dto.setCategoryId(1L);

        Set<ConstraintViolation<ProductDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }
}