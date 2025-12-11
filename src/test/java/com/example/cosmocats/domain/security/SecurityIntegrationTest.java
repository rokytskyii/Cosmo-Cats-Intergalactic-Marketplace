package com.example.cosmocats.domain.security;

import com.example.cosmocats.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class SecurityIntegrationTest extends BaseIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void accessProtectedResource_WithoutAuth_ReturnsUnauthorized() throws Exception {
    mockMvc.perform(get("/api/v1/products")).andExpect(status().isUnauthorized());
  }

  @Test
  void accessProtectedResource_WithApiKey_ReturnsOk() throws Exception {
    mockMvc
        .perform(get("/api/v1/products").header("X-API-KEY", "COSMO_SECRET_KEY"))
        .andExpect(status().isOk());
  }
}
