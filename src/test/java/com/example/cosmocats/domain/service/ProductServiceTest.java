package com.example.cosmocats.domain.service;

import com.example.cosmocats.domain.model.Category;
import com.example.cosmocats.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
        clearProductStore();
    }

    private void clearProductStore() {
        try {
            var storeField = ProductService.class.getDeclaredField("store");
            storeField.setAccessible(true);
            var store = storeField.get(productService);
            if (store instanceof java.util.Map) {
                ((java.util.Map<?, ?>) store).clear();
            }

            var idGenField = ProductService.class.getDeclaredField("idGen");
            idGenField.setAccessible(true);
            idGenField.set(productService, new java.util.concurrent.atomic.AtomicLong(1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void save_ShouldSaveProductWithGeneratedId() {
        Product product = new Product(null, "Test Product", "Test Description", 10.0,
                new Category(1L, "Test Category"));

        Product saved = productService.save(product);

        assertNotNull(saved.getId());
        assertEquals("Test Product", saved.getName());
        assertEquals(10.0, saved.getPrice());
    }

    @Test
    void save_ShouldUseExistingId_WhenProductHasId() {
        Product product = new Product(999L, "Test Product", "Test Description", 10.0,
                new Category(1L, "Test Category"));

        Product saved = productService.save(product);

        assertEquals(999L, saved.getId());
        assertEquals("Test Product", saved.getName());
    }

    @Test
    void findById_ShouldReturnProduct_WhenExists() {
        Product product = new Product(null, "Test Product", "Test Description", 10.0,
                new Category(1L, "Test Category"));
        Product saved = productService.save(product);
        Long productId = saved.getId();

        Optional<Product> found = productService.findById(productId);

        assertTrue(found.isPresent());
        assertEquals(productId, found.get().getId());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        Optional<Product> found = productService.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void findAll_ShouldReturnAllProducts() {
        productService.save(new Product(null, "Product 1", "Desc 1", 10.0, new Category(1L, "Cat1")));
        productService.save(new Product(null, "Product 2", "Desc 2", 20.0, new Category(2L, "Cat2")));

        List<Product> products = productService.findAll();

        assertEquals(2, products.size());
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoProducts() {

        List<Product> products = productService.findAll();

        assertTrue(products.isEmpty());
        assertEquals(0, products.size());
    }

    @Test
    void update_ShouldUpdateProduct_WhenExists() {
        Product original = productService.save(new Product(null, "Original", "Desc", 10.0,
                new Category(1L, "Cat1")));
        Long productId = original.getId();

        Product updated = new Product(null, "Updated", "New Desc", 15.0, new Category(2L, "Cat2"));

        Product result = productService.update(productId, updated);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Updated", result.getName());
        assertEquals("New Desc", result.getDescription());
        assertEquals(15.0, result.getPrice());
        assertEquals(2L, result.getCategory().getId());
    }

    @Test
    void update_ShouldReturnNull_WhenNotExists() {
        Product updated = new Product(null, "Updated", "Desc", 10.0, new Category(1L, "Cat1"));

        Product result = productService.update(999L, updated);

        assertNull(result);
    }

    @Test
    void update_ShouldReturnNull_WhenProductIsNull() {
        Product product = productService.save(new Product(null, "Test", "Desc", 10.0,
                new Category(1L, "Cat1")));
        Long productId = product.getId();

        Product result = productService.update(productId, null);

        assertNull(result);
    }

    @Test
    void update_ShouldPreserveCategory_WhenUpdatedProductHasNullCategory() {
        Category originalCategory = new Category(1L, "Original Category");
        Product original = productService.save(new Product(null, "Original", "Desc", 10.0, originalCategory));
        Long productId = original.getId();

        Product updated = new Product(null, "Updated", "New Desc", 15.0, null);

        Product result = productService.update(productId, updated);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Updated", result.getName());
        assertEquals(originalCategory, result.getCategory()); // Category should be preserved
    }

    @Test
    void delete_ShouldRemoveProduct_WhenExists() {
        Product product = productService.save(new Product(null, "To Delete", "Desc", 10.0,
                new Category(1L, "Cat1")));
        Long productId = product.getId();

        boolean deleted = productService.delete(productId);

        assertTrue(deleted);
        assertFalse(productService.findById(productId).isPresent());
    }

    @Test
    void delete_ShouldReturnFalse_WhenNotExists() {
        boolean deleted = productService.delete(999L);

        assertFalse(deleted);
    }

    @Test
    void init_ShouldCreateDefaultProducts() {
        clearProductStore();

        productService.init();

        List<Product> products = productService.findAll();

        assertEquals(2, products.size());
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Star Yarn")));
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Galaxy Milk")));
    }
}