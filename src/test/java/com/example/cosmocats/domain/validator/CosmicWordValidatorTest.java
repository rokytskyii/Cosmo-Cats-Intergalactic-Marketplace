package com.example.cosmocats.domain.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class CosmicWordValidatorTest {

  private CosmicWordValidator validator;

  @BeforeEach
  void setUp() {
    validator = new CosmicWordValidator();
  }

  @Test
  void isValid_ShouldReturnTrue_WhenContainsCosmicWord() {
    assertTrue(validator.isValid("Star Product", null));
    assertTrue(validator.isValid("Galaxy Milk", null));
    assertTrue(validator.isValid("Cosmic Yarn", null));
    assertTrue(validator.isValid("Planet Toy", null));
  }

  @Test
  void isValid_ShouldReturnFalse_WhenNoCosmicWord() {
    assertFalse(validator.isValid("Regular Product", null));
    assertFalse(validator.isValid("Simple Item", null));
  }

  @Test
  void isValid_ShouldReturnTrue_WhenNull() {
    assertTrue(validator.isValid(null, null));
  }

  @Test
  void isValid_ShouldBeCaseInsensitive() {
    assertTrue(validator.isValid("STAR Product", null));
    assertTrue(validator.isValid("galaxy product", null));
    assertTrue(validator.isValid("Cosmic PRODUCT", null));
  }
}
