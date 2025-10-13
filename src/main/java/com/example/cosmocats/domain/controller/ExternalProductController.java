package com.example.cosmocats.domain.controller;

import com.example.cosmocats.domain.dto.ExternalProductDTO;
import com.example.cosmocats.domain.service.ExternalProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/external/products")
public class ExternalProductController {

    private final ExternalProductService externalProductService;

    public ExternalProductController(ExternalProductService externalProductService) {
        this.externalProductService = externalProductService;
    }

    @GetMapping
    public ResponseEntity<List<ExternalProductDTO>> getAllExternalProducts() {
        List<ExternalProductDTO> products = externalProductService.getAllExternalProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExternalProductDTO> getExternalProductById(@PathVariable Long id) {
        return externalProductService.getExternalProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}