package com.example.cosmocats.domain.controller;

import com.example.cosmocats.domain.dto.ExternalProductDTO;
import com.example.cosmocats.domain.service.ExternalProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("deprecation")
@WebMvcTest(ExternalProductController.class)
class ExternalProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExternalProductService externalProductService;

    @Test
    void getAllExternalProducts_ShouldReturnProducts() throws Exception {
        ExternalProductDTO product1 = new ExternalProductDTO();
        product1.setId(1L);
        product1.setTitle("Star Product");
        product1.setPrice(10.0);

        ExternalProductDTO product2 = new ExternalProductDTO();
        product2.setId(2L);
        product2.setTitle("Galaxy Product");
        product2.setPrice(20.0);

        List<ExternalProductDTO> products = Arrays.asList(product1, product2);
        when(externalProductService.getAllExternalProducts()).thenReturn(products);

        mockMvc.perform(get("/api/v1/external/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Star Product"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Galaxy Product"));
    }

    @Test
    void getAllExternalProducts_ShouldReturnEmptyList() throws Exception {
        when(externalProductService.getAllExternalProducts()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/external/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getExternalProductById_ShouldReturnProduct_WhenExists() throws Exception {
        ExternalProductDTO product = new ExternalProductDTO();
        product.setId(1L);
        product.setTitle("Cosmic Product");
        product.setPrice(15.0);

        when(externalProductService.getExternalProductById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/v1/external/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Cosmic Product"))
                .andExpect(jsonPath("$.price").value(15.0));
    }

    @Test
    void getExternalProductById_ShouldReturnNotFound_WhenNotExists() throws Exception {
        when(externalProductService.getExternalProductById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/external/products/999"))
                .andExpect(status().isNotFound());
    }
}