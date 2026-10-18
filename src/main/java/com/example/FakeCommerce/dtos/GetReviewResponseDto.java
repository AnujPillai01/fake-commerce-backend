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
public class GetReviewResponseDto {
    private Long id;
    private Long productId;
    private Long orderId;
    private BigDecimal rating;
    private String comment;
}
