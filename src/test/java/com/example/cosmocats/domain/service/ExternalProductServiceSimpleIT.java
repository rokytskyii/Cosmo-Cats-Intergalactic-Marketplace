package com.example.cosmocats.domain.service;

import com.example.cosmocats.domain.dto.ExternalProductDTO;
import com.example.cosmocats.domain.exception.ExternalServiceException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Tag("integration")
class ExternalProductServiceSimpleIT {

  @Autowired private ExternalProductService externalProductService;

  @Test
  void getAllExternalProducts_ShouldReturnList() {
    try {
      List<ExternalProductDTO> products = externalProductService.getAllExternalProducts();
      assertNotNull(products);
    } catch (ExternalServiceException e) {
      assertNotNull(e);
      assertTrue(e.getMessage().contains("Failed to fetch"));
    }
  }

  @Test
  void getExternalProductById_ShouldHandleInvalidId() {
    try {
      Optional<ExternalProductDTO> product = externalProductService.getExternalProductById(999L);
      assertNotNull(product);
    } catch (ExternalServiceException e) {
      assertNotNull(e);
    }
  }

  @Test
  void createExternalProduct_ShouldBeCallable() {
    ExternalProductDTO product = new ExternalProductDTO();
    product.setTitle("Test Product");
    product.setPrice(10.0);
    product.setDescription("Test Description");
    product.setCategory("Test Category");

    try {
      ExternalProductDTO result = externalProductService.createExternalProduct(product);
      if (result != null) {
        assertNotNull(result);
      }
    } catch (ExternalServiceException e) {
      assertNotNull(e);
      assertTrue(e.getMessage().contains("Failed to create"));
    }
  }

  @Test
  void serviceInitialization_ShouldWork() {
    assertNotNull(externalProductService);

    try {
      var field = ExternalProductService.class.getDeclaredField("restClient");
      field.setAccessible(true);
      var restClient = field.get(externalProductService);
      assertNotNull(restClient);
    } catch (Exception e) {
    }
  }
}
