package com.example.ordersystem.ordering.ordering.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private String category;
    private Integer price;
    private int stockQuantity;
    private String imagePath;
    }

