package com.example.cosmocats.domain.controller;

import com.example.cosmocats.domain.dto.ProductDTO;
import com.example.cosmocats.domain.mapper.ProductMapper;
import com.example.cosmocats.domain.model.Category;
import com.example.cosmocats.domain.model.Product;
import com.example.cosmocats.domain.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("deprecation")
@WebMvcTest(ProductController.class)
@Tag("unit")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createProduct_ShouldReturnCreated_WhenValidInput() throws Exception {
        ProductDTO inputDTO = createProductDTO(null, "Star Galaxy Product", "Description", 10.0, 1L);
        Product savedProduct = createProduct(1L, "Star Galaxy Product", "Description", 10.0, 1L);
        ProductDTO outputDTO = createProductDTO(1L, "Star Galaxy Product", "Description", 10.0, 1L);

        when(productMapper.toDomain(any(ProductDTO.class))).thenReturn(savedProduct);
        when(productService.save(any(Product.class))).thenReturn(savedProduct);
        when(productMapper.toDto(any(Product.class))).thenReturn(outputDTO);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Star Galaxy Product"))
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "/api/v1/products/1"));
    }

    @Test
    void createProduct_ShouldReturnBadRequest_WhenInvalidInput() throws Exception {
        ProductDTO invalidDTO = createProductDTO(null, "Product", "", -1.0, null);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listProducts_ShouldReturnProducts() throws Exception {
        // Given
        Product product1 = createProduct(1L, "Star Product", "Desc 1", 10.0, 1L);
        Product product2 = createProduct(2L, "Galaxy Product", "Desc 2", 20.0, 2L);
        ProductDTO dto1 = createProductDTO(1L, "Star Product", "Desc 1", 10.0, 1L);
        ProductDTO dto2 = createProductDTO(2L, "Galaxy Product", "Desc 2", 20.0, 2L);

        when(productService.findAll()).thenReturn(Arrays.asList(product1, product2));
        when(productMapper.toDto(product1)).thenReturn(dto1);
        when(productMapper.toDto(product2)).thenReturn(dto2);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void listProducts_ShouldReturnEmptyList_WhenNoProducts() throws Exception {
        when(productService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getProduct_ShouldReturnProduct_WhenExists() throws Exception {
        Product product = createProduct(1L, "Cosmic Product", "Description", 15.0, 1L);
        ProductDTO dto = createProductDTO(1L, "Cosmic Product", "Description", 15.0, 1L);

        when(productService.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Cosmic Product"));
    }

    @Test
    void getProduct_ShouldReturnNotFound_WhenNotExists() throws Exception {
        when(productService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProduct_ShouldReturnUpdatedProduct_WhenExists() throws Exception {
        ProductDTO inputDTO = createProductDTO(null, "Updated Star Product", "New Desc", 25.0, 2L);
        Product updatedProduct = createProduct(1L, "Updated Star Product", "New Desc", 25.0, 2L);
        ProductDTO outputDTO = createProductDTO(1L, "Updated Star Product", "New Desc", 25.0, 2L);

        when(productMapper.toDomain(any(ProductDTO.class))).thenReturn(updatedProduct);
        when(productService.update(eq(1L), any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.toDto(updatedProduct)).thenReturn(outputDTO);

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Star Product"));
    }

    @Test
    void updateProduct_ShouldReturnNotFound_WhenNotExists() throws Exception {
        ProductDTO inputDTO = createProductDTO(null, "Nebula Product", "New Desc", 25.0, 2L);
        Product updatedProduct = createProduct(1L, "Nebula Product", "New Desc", 25.0, 2L);

        when(productMapper.toDomain(any(ProductDTO.class))).thenReturn(updatedProduct);
        when(productService.update(eq(999L), any(Product.class))).thenReturn(null);

        mockMvc.perform(put("/api/v1/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProduct_ShouldHandleNullResponse() throws Exception {
        ProductDTO inputDTO = createProductDTO(null, "Star Product", "Desc", 10.0, 1L);
        Product productToUpdate = createProduct(1L, "Star Product", "Desc", 10.0, 1L);

        when(productMapper.toDomain(any(ProductDTO.class))).thenReturn(productToUpdate);
        when(productService.update(eq(999L), any(Product.class))).thenReturn(null);

        mockMvc.perform(put("/api/v1/products/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createProduct_ShouldReturnBadRequest_WhenNoCosmicWord() throws Exception {
        ProductDTO invalidDTO = createProductDTO(null, "Regular Product", "Description", 10.0, 1L);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0]").value(org.hamcrest.Matchers.containsString("cosmic word")));
    }

    private ProductDTO createProductDTO(Long id, String name, String description, Double price, Long categoryId) {
        ProductDTO dto = new ProductDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setDescription(description);
        dto.setPrice(price);
        dto.setCategoryId(categoryId);
        return dto;
    }

    private Product createProduct(Long id, String name, String description, Double price, Long categoryId) {
        return new Product(id, name, description, price, new Category(categoryId, "Category " + categoryId));
    }
}