package com.example.ordersystem.ordering.ordering.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDetailResDto {

    private Long detailId;
    private String productName;
    private int count;
}
