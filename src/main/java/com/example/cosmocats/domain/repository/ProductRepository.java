package com.example.cosmocats.domain.repository;

import com.example.cosmocats.domain.model.Product;
import com.example.cosmocats.domain.projection.ProductSummaryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Custom JPQL Query з використанням Projection
    @Query("SELECT p.name as name, p.price as price, c.name as categoryName " +
            "FROM Product p JOIN p.category c " +
            "WHERE p.price > :minPrice " +
            "ORDER BY p.price DESC")
    List<ProductSummaryProjection> findExpensiveProducts(@Param("minPrice") Double minPrice);
}