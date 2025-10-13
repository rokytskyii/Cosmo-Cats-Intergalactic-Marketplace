package com.example.cosmocats.domain.mapper;

import com.example.cosmocats.domain.model.Product;
import com.example.cosmocats.domain.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "category.id", source = "categoryId")
    Product toDomain(ProductDTO dto);

    @Mapping(target = "categoryId", source = "category.id")
    ProductDTO toDto(Product product);
}
