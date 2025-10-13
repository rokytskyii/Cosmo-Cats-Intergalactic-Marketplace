package com.example.cosmocats.domain.dto;

import com.example.cosmocats.domain.validator.CosmicWordCheck;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductDTO {
    private Long id;

    @NotBlank(message = "name must not be blank")
    @Size(min = 3, max = 100, message = "name length must be between 3 and 100")
    @CosmicWordCheck
    private String name;

    @Size(max = 500, message = "description max length is 500")
    private String description;

    @NotNull(message = "price must be provided")
    @DecimalMin(value = "0.01", inclusive = true, message = "price must be greater than 0")
    private Double price;

    @NotNull(message = "category must be provided")
    private Long categoryId;
}
