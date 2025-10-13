package com.example.cosmocats.domain.service;

import com.example.cosmocats.domain.model.Category;
import com.example.cosmocats.domain.model.Product;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {

    private final Map<Long, Product> store = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @PostConstruct
    public void init() {
        Category cat1 = new Category(1L, "AntiGravity");
        Category cat2 = new Category(2L, "Dairy");

        save(new Product(null, "Star Yarn", "Антигравітаційні клубки ниток - Star edition", 19.99, cat1));
        save(new Product(null, "Galaxy Milk", "Космічне молоко з молекулами зоряного пилу", 9.99, cat2));
    }

    public Product save(Product p) {
        if (p.getId() == null)
            p.setId(idGen.getAndIncrement());
        store.put(p.getId(), p);
        return p;
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    public Product update(Long id, Product updated) {
        Product existing = store.get(id);
        if (existing == null)
            return null;
        updated.setId(id);
        if (updated.getCategory() == null)
            updated.setCategory(existing.getCategory());
        store.put(id, updated);
        return updated;
    }

    public boolean delete(Long id) {
        return store.remove(id) != null;
    }
}
