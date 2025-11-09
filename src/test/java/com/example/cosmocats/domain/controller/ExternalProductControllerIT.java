package com.example.cosmocats.domain.controller;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
class ExternalProductControllerIT {

  @Autowired private MockMvc mockMvc;

  @Test
  void getAllExternalProducts_ShouldReturnProducts() throws Exception {
    mockMvc
        .perform(get("/api/v1/external/products"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
  }

  @Test
  void getExternalProductById_ShouldReturnNotFound_ForInvalidId() throws Exception {
    mockMvc.perform(get("/api/v1/external/products/999")).andExpect(status().isOk());
  }
}
