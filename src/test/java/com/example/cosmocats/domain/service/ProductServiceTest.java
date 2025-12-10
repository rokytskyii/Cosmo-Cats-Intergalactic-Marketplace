package com.example.cosmocats.domain.service;

import com.example.cosmocats.entity.Category;
import com.example.cosmocats.entity.Product;
import com.example.cosmocats.domain.repository.CategoryRepository;
import com.example.cosmocats.domain.repository.ProductRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private ProductService productService;

  @Test
  void save_ShouldSaveProduct() {
    Category category = new Category(1L, "Test Category");
    Product productToSave = new Product(null, "Test Product", "Description", 10.0, category);
    Product savedProduct = new Product(1L, "Test Product", "Description", 10.0, category);

    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

    Product result = productService.save(productToSave);

    assertNotNull(result.getId());
    assertEquals("Test Product", result.getName());
    verify(productRepository, times(1)).save(productToSave);
  }

  @Test
  void findById_ShouldReturnProduct_WhenExists() {
    Long id = 1L;
    Product product = new Product(id, "Test", "Desc", 10.0, new Category(1L, "Cat"));
    when(productRepository.findById(id)).thenReturn(Optional.of(product));

    Optional<Product> found = productService.findById(id);

    assertTrue(found.isPresent());
    assertEquals(id, found.get().getId());
  }

  @Test
  void findById_ShouldReturnEmpty_WhenNotExists() {
    when(productRepository.findById(999L)).thenReturn(Optional.empty());

    Optional<Product> found = productService.findById(999L);

    assertFalse(found.isPresent());
  }

  @Test
  void findAll_ShouldReturnAllProducts() {
    List<Product> products = List.of(
            new Product(1L, "P1", "D1", 10.0, null),
            new Product(2L, "P2", "D2", 20.0, null)
    );
    when(productRepository.findAll()).thenReturn(products);

    List<Product> result = productService.findAll();

    assertEquals(2, result.size());
  }

  @Test
  void update_ShouldUpdateProduct_WhenExists() {
    Long id = 1L;
    Product existingProduct = new Product(id, "Old Name", "Old Desc", 10.0, new Category(1L, "Cat"));
    Product updateInfo = new Product(null, "New Name", "New Desc", 15.0, new Category(1L, "Cat"));

    when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
    when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Product result = productService.update(id, updateInfo);

    assertNotNull(result);
    assertEquals("New Name", result.getName());
    assertEquals(15.0, result.getPrice());
    verify(productRepository).save(existingProduct);
  }

  @Test
  void update_ShouldReturnNull_WhenNotExists() {
    when(productRepository.findById(999L)).thenReturn(Optional.empty());

    Product result = productService.update(999L, new Product());

    assertNull(result);
    verify(productRepository, never()).save(any());
  }

  @Test
  void delete_ShouldReturnTrue_WhenExists() {
    Long id = 1L;
    when(productRepository.existsById(id)).thenReturn(true);

    boolean deleted = productService.delete(id);

    assertTrue(deleted);
    verify(productRepository).deleteById(id);
  }

  @Test
  void delete_ShouldReturnFalse_WhenNotExists() {
    Long id = 999L;
    when(productRepository.existsById(id)).thenReturn(false);

    boolean deleted = productService.delete(id);

    assertFalse(deleted);
    verify(productRepository, never()).deleteById(any());
  }
}