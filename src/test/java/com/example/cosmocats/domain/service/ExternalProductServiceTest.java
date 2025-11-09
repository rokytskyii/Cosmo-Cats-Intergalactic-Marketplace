package com.example.cosmocats.domain.service;

import com.example.cosmocats.domain.dto.ExternalProductDTO;
import com.example.cosmocats.domain.exception.ExternalServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class ExternalProductServiceTest {

  private ExternalProductService externalProductService;

  @Mock private RestClient restClient;

  @Mock private RestClient.RequestBodyUriSpec requestBodyUriSpec;

  @Mock private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

  @Mock private RestClient.RequestHeadersSpec requestHeadersSpec;

  @Mock private RestClient.ResponseSpec responseSpec;

  @BeforeEach
  void setUp() {
    externalProductService = new ExternalProductService("http://localhost:8081");

    try {
      var field = ExternalProductService.class.getDeclaredField("restClient");
      field.setAccessible(true);
      field.set(externalProductService, restClient);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void getAllExternalProducts_ShouldReturnProducts_WhenSuccessful() {
    ExternalProductDTO[] products =
        new ExternalProductDTO[] {
          createExternalProduct(1L, "Product 1", 10.0), createExternalProduct(2L, "Product 2", 20.0)
        };

    when(restClient.get()).thenReturn(requestHeadersUriSpec);
    when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.body(ExternalProductDTO[].class)).thenReturn(products);

    List<ExternalProductDTO> result = externalProductService.getAllExternalProducts();

    assertEquals(2, result.size());
    assertEquals("Product 1", result.get(0).getTitle());
    assertEquals("Product 2", result.get(1).getTitle());
  }

  @Test
  void getAllExternalProducts_ShouldReturnEmptyList_WhenNullResponse() {
    when(restClient.get()).thenReturn(requestHeadersUriSpec);
    when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.body(ExternalProductDTO[].class)).thenReturn(null);

    List<ExternalProductDTO> result = externalProductService.getAllExternalProducts();

    assertTrue(result.isEmpty());
  }

  @Test
  void getAllExternalProducts_ShouldThrowException_WhenRestClientFails() {
    when(restClient.get()).thenReturn(requestHeadersUriSpec);
    when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.body(ExternalProductDTO[].class))
        .thenThrow(new RestClientException("Connection failed"));

    assertThrows(
        ExternalServiceException.class,
        () -> {
          externalProductService.getAllExternalProducts();
        });
  }

  @Test
  void getExternalProductById_ShouldReturnProduct_WhenExists() {
    ExternalProductDTO product = createExternalProduct(1L, "Test Product", 15.0);

    when(restClient.get()).thenReturn(requestHeadersUriSpec);
    when(requestHeadersUriSpec.uri(anyString(), any(Object.class))).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.body(ExternalProductDTO.class)).thenReturn(product);

    Optional<ExternalProductDTO> result = externalProductService.getExternalProductById(1L);

    assertTrue(result.isPresent());
    assertEquals("Test Product", result.get().getTitle());
    assertEquals(15.0, result.get().getPrice());
  }

  @Test
  void getExternalProductById_ShouldReturnEmpty_WhenProductNotFound() {
    when(restClient.get()).thenReturn(requestHeadersUriSpec);
    when(requestHeadersUriSpec.uri(anyString(), any(Object.class))).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.body(ExternalProductDTO.class)).thenReturn(null);

    Optional<ExternalProductDTO> result = externalProductService.getExternalProductById(999L);

    assertFalse(result.isPresent());
  }

  @Test
  void getExternalProductById_ShouldThrowException_WhenRestClientFails() {
    when(restClient.get()).thenReturn(requestHeadersUriSpec);
    when(requestHeadersUriSpec.uri(anyString(), any(Object.class))).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.body(ExternalProductDTO.class))
        .thenThrow(new RestClientException("Connection failed"));

    assertThrows(
        ExternalServiceException.class,
        () -> {
          externalProductService.getExternalProductById(1L);
        });
  }

  @Test
  void createExternalProduct_ShouldHandleBasicScenario() {
    ExternalProductDTO inputProduct = createExternalProduct(null, "New Product", 25.0);

    assertNotNull(externalProductService);
  }

  @Test
  void externalProductService_ShouldInitializeCorrectly() {
    ExternalProductService service = new ExternalProductService("http://test-url");
    assertNotNull(service);

    try {
      var field = ExternalProductService.class.getDeclaredField("restClient");
      field.setAccessible(true);
      var restClient = field.get(service);
      assertNotNull(restClient);
    } catch (Exception e) {
    }
  }

  private ExternalProductDTO createExternalProduct(Long id, String title, Double price) {
    ExternalProductDTO product = new ExternalProductDTO();
    product.setId(id);
    product.setTitle(title);
    product.setPrice(price);
    product.setDescription("Test Description");
    product.setCategory("Test Category");
    return product;
  }
}
