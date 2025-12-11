package com.example.cosmocats.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NaturalId
  @Column(name = "order_number", nullable = false, unique = true)
  private UUID orderNumber = UUID.randomUUID();

  @ManyToMany
  @JoinTable(
      name = "order_items",
      joinColumns = @JoinColumn(name = "order_id"),
      inverseJoinColumns = @JoinColumn(name = "product_id"))
  private List<Product> products;

  private Instant createdAt = Instant.now();
  private String status;
}
