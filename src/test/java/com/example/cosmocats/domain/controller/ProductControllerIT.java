package com.example.cosmocats.domain.controller;

import com.example.cosmocats.BaseIntegrationTest;
import com.example.cosmocats.domain.dto.ProductDTO;
import com.example.cosmocats.entity.Category;
import com.example.cosmocats.domain.repository.CategoryRepository;
import com.example.cosmocats.domain.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Tag("integration")
@WithMockUser(username = "astro_tester", roles = "USER")
class ProductControllerIT extends BaseIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private CategoryRepository categoryRepository;
  @Autowired private ProductRepository productRepository;

  @BeforeEach
  void setUp() {
    productRepository.deleteAll();
    categoryRepository.deleteAll();
  }

  @Test
  void getAllProducts_ShouldReturnProducts() throws Exception {
    mockMvc
        .perform(get("/api/v1/products"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
  }

  @Test
  void createProduct_ShouldCreateAndReturnProduct() throws Exception {
    Category savedCategory = categoryRepository.save(new Category(null, "Test Category"));

    ProductDTO productDTO = new ProductDTO();
    productDTO.setName("Star Galaxy Product");
    productDTO.setDescription("Integration test product");
    productDTO.setPrice(19.99);
    productDTO.setCategoryId(savedCategory.getId());

    mockMvc
        .perform(
            post("/api/v1/products")
                .with(csrf()) // Важливо для POST запитів з Security
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Star Galaxy Product"))
        .andExpect(jsonPath("$.categoryId").value(savedCategory.getId()));
  }
}
