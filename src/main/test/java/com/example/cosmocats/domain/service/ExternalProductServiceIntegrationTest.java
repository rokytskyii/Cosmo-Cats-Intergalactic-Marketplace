package com.example.cosmocats.domain.service;

import com.example.cosmocats.domain.dto.ExternalProductDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class ExternalProductServiceIntegrationTest {

    @Container
    static GenericContainer<?> wiremockContainer = new GenericContainer<>("wiremock/wiremock:latest")
            .withExposedPorts(8080)
            .withCommand("--verbose", "--global-response-templating");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        String baseUrl = "http://" + wiremockContainer.getHost() + ":" + wiremockContainer.getMappedPort(8080);
        registry.add("external.products.api.url", () -> baseUrl);
    }

    @Autowired
    private ExternalProductService externalProductService;

    @Test
    void shouldFetchExternalProducts() {
        List<ExternalProductDTO> products = externalProductService.getAllExternalProducts();

        assertThat(products).isNotEmpty();
        assertThat(products).hasSize(3);
        assertThat(products.get(0).getTitle()).isEqualTo("Cosmic Cat Food");
        assertThat(products.get(0).getPrice()).isEqualTo(15.99);
    }

    @Test
    void shouldFetchExternalProductById() {
        var product = externalProductService.getExternalProductById(1L);

        assertThat(product).isPresent();
        assertThat(product.get().getTitle()).isEqualTo("Cosmic Cat Food");
        assertThat(product.get().getCategory()).isEqualTo("pet-supplies");
    }
}