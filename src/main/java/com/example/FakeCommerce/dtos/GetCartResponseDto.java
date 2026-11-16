package com.example.FakeCommerce.dtos;

import com.example.FakeCommerce.schema.CartStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetCartResponseDto {
    private Long id;
    private Long userId;
    private CartStatus status;
    private List<CartItemResponseDto> items;
    private BigDecimal totalAmount;
}