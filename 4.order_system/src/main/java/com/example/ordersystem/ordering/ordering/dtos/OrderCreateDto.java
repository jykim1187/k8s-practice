package com.example.ordersystem.ordering.ordering.dtos;

import jakarta.validation.constraints.NegativeOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto {
    private Long productId;
    private Integer productCount; // 주문수량
}
