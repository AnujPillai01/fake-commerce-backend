package com.example.FakeCommerce.dtos;

import com.example.FakeCommerce.schema.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderRequestDto {
    private OrderStatus orderStatus;
    private List<OrderItemActionDto> orderItemActions;
}
