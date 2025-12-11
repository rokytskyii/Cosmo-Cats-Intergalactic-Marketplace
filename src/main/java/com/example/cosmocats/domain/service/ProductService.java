package com.example.cosmocats.domain.service;

import com.example.cosmocats.entity.Category;
import com.example.cosmocats.entity.Product;
import com.example.cosmocats.domain.repository.CategoryRepository;
import com.example.cosmocats.domain.repository.ProductRepository;
import com.example.cosmocats.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  public ProductService(
      ProductRepository productRepository, CategoryRepository categoryRepository) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
  }

  @Transactional
  public Product save(Product p) {
    if (p.getCategory() != null && p.getCategory().getId() != null) {
      Category category =
          categoryRepository
              .findById(p.getCategory().getId())
              .orElseThrow(
                  () ->
                      new ResourceNotFoundException(
                          "Category not found with id: " + p.getCategory().getId()));
      p.setCategory(category);
    }
    return productRepository.save(p);
  }

  public Optional<Product> findById(Long id) {
    return productRepository.findById(id);
  }

  public List<Product> findAll() {
    return productRepository.findAll();
  }

  @Transactional
  public Product update(Long id, Product updated) {
    return productRepository
        .findById(id)
        .map(
            existing -> {
              existing.setName(updated.getName());
              existing.setDescription(updated.getDescription());
              existing.setPrice(updated.getPrice());
              if (updated.getCategory() != null) {
                existing.setCategory(updated.getCategory());
              }
              return productRepository.save(existing);
            })
        .orElse(null);
  }

  @Transactional
  public boolean delete(Long id) {
    if (productRepository.existsById(id)) {
      productRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
