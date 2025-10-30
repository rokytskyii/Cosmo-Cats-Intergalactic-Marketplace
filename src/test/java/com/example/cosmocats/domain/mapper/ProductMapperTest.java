package com.example.cosmocats.domain.mapper;

import com.example.cosmocats.domain.dto.ProductDTO;
import com.example.cosmocats.domain.model.Category;
import com.example.cosmocats.domain.model.Product;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class ProductMapperTest {

    private final ProductMapper mapper = ProductMapper.INSTANCE;

    @Test
    void toDomain_ShouldMapDtoToDomain() {
        ProductDTO dto = new ProductDTO();
        dto.setId(1L);
        dto.setName("Star Product");
        dto.setDescription("Test Description");
        dto.setPrice(15.99);
        dto.setCategoryId(2L);

        Product product = mapper.toDomain(dto);

        assertNotNull(product);
        assertEquals(1L, product.getId());
        assertEquals("Star Product", product.getName());
        assertEquals("Test Description", product.getDescription());
        assertEquals(15.99, product.getPrice());
        assertNotNull(product.getCategory());
        assertEquals(2L, product.getCategory().getId());
    }

    @Test
    void toDto_ShouldMapDomainToDto() {
        Category category = new Category(3L, "Test Category");
        Product product = new Product(1L, "Galaxy Product", "Test Desc", 25.99, category);

        ProductDTO dto = mapper.toDto(product);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Galaxy Product", dto.getName());
        assertEquals("Test Desc", dto.getDescription());
        assertEquals(25.99, dto.getPrice());
        assertEquals(3L, dto.getCategoryId());
    }
}