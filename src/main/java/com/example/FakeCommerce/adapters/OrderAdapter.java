package com.example.FakeCommerce.adapters;

import com.example.FakeCommerce.dtos.GetOrderResponseDto;
import com.example.FakeCommerce.dtos.OrderItemResponseDto;
import com.example.FakeCommerce.schema.Order;
import com.example.FakeCommerce.schema.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderAdapter {

    public OrderItemResponseDto mapToOrderItemResponseDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        BigDecimal unitPrice = orderItem.getUnitPrice() != null ? orderItem.getUnitPrice() : BigDecimal.ZERO;
        int quantity = orderItem.getQuantity() != null ? orderItem.getQuantity() : 0;
        BigDecimal subTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

        return OrderItemResponseDto.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProduct() != null ? orderItem.getProduct().getId() : null)
                .productName(orderItem.getProduct() != null ? orderItem.getProduct().getTitle() : null)
                .vendorId(orderItem.getVendor() != null ? orderItem.getVendor().getId() : null)
                .storeName(orderItem.getVendor() != null ? orderItem.getVendor().getStoreName() : null)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .subTotal(subTotal)
                .build();
    }

    public GetOrderResponseDto mapToOrderResponseDto(Order order) {
        if (order == null) {
            return null;
        }

        List<OrderItemResponseDto> itemResponseDtos = List.of();
        if (order.getOrderItems() != null) {
            itemResponseDtos = order.getOrderItems().stream()
                    .map(this::mapToOrderItemResponseDto)
                    .collect(Collectors.toList());
        }

        return GetOrderResponseDto.builder()
                .id(order.getId())
                .userId(order.getUser() != null ? order.getUser().getId() : null)
                .totalAmount(order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO)
                .status(order.getStatus())
                .shippingAddress(order.getShippingAddress())
                .items(itemResponseDtos)
                .createdAt(order.getCreatedAt())
                .build();
    }

    public List<GetOrderResponseDto> mapToOrderResponseDtoList(List<Order> orders) {
        if (orders == null) {
            return List.of();
        }
        return orders.stream()
                .map(this::mapToOrderResponseDto)
                .collect(Collectors.toList());
    }
}