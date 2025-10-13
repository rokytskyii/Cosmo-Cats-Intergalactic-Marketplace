package com.example.cosmocats.domain.dto;

import lombok.Data;

@Data
public class ExternalProductDTO {
    private Long id;
    private String title;
    private Double price;
    private String description;
    private String category;
}