package com.example.FakeCommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDto {
    private Long id;
    private Long productId;
    private String productName;
    private Long vendorId;
    private String storeName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subTotal;
}