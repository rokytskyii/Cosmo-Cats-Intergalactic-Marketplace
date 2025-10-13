package com.example.cosmocats.domain.model;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class Order {
    private Long id;
    private List<Long> productIds;
    private Instant createdAt;
    private String status;
}
