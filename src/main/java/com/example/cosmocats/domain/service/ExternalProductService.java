package com.example.cosmocats.domain.service;

import com.example.cosmocats.domain.exception.ExternalServiceException;
import com.example.cosmocats.domain.dto.ExternalProductDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ExternalProductService {

  private final RestClient restClient;

  public ExternalProductService(
      @Value("${external.products.api.url:http://localhost:8081}") String baseUrl) {
    this.restClient =
        RestClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("Content-Type", "application/json")
            .defaultHeader("User-Agent", "CosmoCats-Marketplace/1.0")
            .build();
  }

  public List<ExternalProductDTO> getAllExternalProducts() {
    try {
      ExternalProductDTO[] products =
          restClient.get().uri("/products").retrieve().body(ExternalProductDTO[].class);
      return products != null ? Arrays.asList(products) : List.of();
    } catch (RestClientException e) {
      throw new ExternalServiceException(
          "Failed to fetch products from external API: " + e.getMessage());
    }
  }

  public Optional<ExternalProductDTO> getExternalProductById(Long id) {
    try {
      ExternalProductDTO product =
          restClient.get().uri("/products/{id}", id).retrieve().body(ExternalProductDTO.class);
      return Optional.ofNullable(product);
    } catch (RestClientException e) {
      throw new ExternalServiceException(
          "Failed to fetch product with id " + id + ": " + e.getMessage());
    }
  }

  public ExternalProductDTO createExternalProduct(ExternalProductDTO product) {
    try {
      return restClient
          .post()
          .uri("/products")
          .body(product)
          .retrieve()
          .body(ExternalProductDTO.class);
    } catch (RestClientException e) {
      throw new ExternalServiceException(
          "Failed to create product in external API: " + e.getMessage());
    }
  }
}
