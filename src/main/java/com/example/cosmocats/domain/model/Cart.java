package com.example.cosmocats.domain.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Cart {
    private Long id;
    private Map<Long, Integer> items = new HashMap<>();
}
