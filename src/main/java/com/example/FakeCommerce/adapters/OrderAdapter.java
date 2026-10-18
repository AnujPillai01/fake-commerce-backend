package com.example.FakeCommerce.adapters;

import com.example.FakeCommerce.dtos.GetOrderResponseDto;
import com.example.FakeCommerce.dtos.OrderItemResponseDto;
import com.example.FakeCommerce.repositories.OrderItemRepository;
import com.example.FakeCommerce.schema.Order;
import com.example.FakeCommerce.schema.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderAdapter {

    private final OrderItemRepository orderItemRepository;

    public List<GetOrderResponseDto> mapToGetOrderResponseDtoList(List<Order> orders){
        return orders.stream()
                .map(this::mapToGetOrderResponseDto)
                .toList();
    }

    public GetOrderResponseDto mapToGetOrderResponseDto(Order order) {

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
        List<OrderItemResponseDto> orderItemResponseDtos = mapToOrderItemResponseDto(orderItems);

        return GetOrderResponseDto
                .builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .status(order.getStatus())
                .orderItems(orderItemResponseDtos)
                .build();
    }

    public List<OrderItemResponseDto> mapToOrderItemResponseDto(List<OrderItem> orderItems) {
        return orderItems.stream().map(op -> OrderItemResponseDto
                .builder()
                .productId(op.getProduct().getId())
                .quantity(op.getQuantity())
                .productName(op.getProduct().getTitle())
                .productPrice(op.getProduct().getPrice())
                .productImage(op.getProduct().getImage())
                .subTotal(op.getProduct().getPrice().multiply(BigDecimal.valueOf(op.getQuantity())))
                .build())
                .toList();
    }
}
