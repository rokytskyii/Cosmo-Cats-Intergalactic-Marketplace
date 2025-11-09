package com.example.cosmocats.domain.controller;

import com.example.cosmocats.domain.model.Category;
import com.example.cosmocats.domain.model.Product;
import com.example.cosmocats.domain.dto.ProductDTO;
import com.example.cosmocats.domain.mapper.ProductMapper;
import com.example.cosmocats.domain.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@Validated
public class ProductController {

  private final ProductService productService;
  private final ProductMapper mapper;

  public ProductController(ProductService productService, ProductMapper mapper) {
    this.productService = productService;
    this.mapper = mapper;
  }

  @PostMapping
  public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO dto) {
    Product toSave = mapper.toDomain(dto);
    toSave.setCategory(new Category(dto.getCategoryId(), "unknown"));
    Product saved = productService.save(toSave);
    ProductDTO out = mapper.toDto(saved);
    return ResponseEntity.created(URI.create("/api/v1/products/" + saved.getId())).body(out);
  }

  @GetMapping
  public ResponseEntity<List<ProductDTO>> listProducts() {
    List<ProductDTO> dtos =
        productService.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    return ResponseEntity.ok(dtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
    return productService
        .findById(id)
        .map(p -> ResponseEntity.ok(mapper.toDto(p)))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductDTO> updateProduct(
      @PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
    Product toUpdate = mapper.toDomain(dto);
    toUpdate.setCategory(new Category(dto.getCategoryId(), "unknown"));
    Product updated = productService.update(id, toUpdate);
    if (updated == null) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(mapper.toDto(updated));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    productService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
