package com.example.cosmocats.domain.repository;

import com.example.cosmocats.BaseIntegrationTest;
import com.example.cosmocats.domain.model.Category;
import com.example.cosmocats.domain.model.Product;
import com.example.cosmocats.domain.projection.ProductSummaryProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
class ProductRepositoryIT extends BaseIntegrationTest {

    @Autowired private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void shouldSaveAndFindProduct() {
        Category category = new Category(null, "Space Food");
        Category savedCat = categoryRepository.save(category);

        Product product = new Product(null, "Test Product", "Desc", 100.0, savedCat);
        Product saved = productRepository.save(product);

        assertThat(saved.getId()).isNotNull();
        assertThat(productRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void shouldExecuteCustomProjectionQuery() {
        Category cat = categoryRepository.save(new Category(null, "Luxury"));
        productRepository.save(new Product(null, "Cheap", "Desc", 5.0, cat));
        productRepository.save(new Product(null, "Expensive", "Desc", 100.0, cat));

        List<ProductSummaryProjection> result = productRepository.findExpensiveProducts(50.0);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Expensive");
    }
}