package com.example.cosmocats.domain.service;

import com.example.cosmocats.BaseIntegrationTest;
import com.example.cosmocats.domain.exception.ExternalServiceException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(properties = {"external.products.api.url=http://invalid-host:9999"})
@Tag("integration")
class ExternalProductServiceWireMockTest extends BaseIntegrationTest {

    @Autowired private ExternalProductService externalProductService;

    @Test
    void getAllExternalProducts_ShouldThrowException_WhenInvalidUrl() {
        assertThrows(
                ExternalServiceException.class,
                () -> {
                    externalProductService.getAllExternalProducts();
                });
    }

    @Test
    void getExternalProductById_ShouldThrowException_WhenInvalidUrl() {
        assertThrows(
                ExternalServiceException.class,
                () -> {
                    externalProductService.getExternalProductById(1L);
                });
    }
}