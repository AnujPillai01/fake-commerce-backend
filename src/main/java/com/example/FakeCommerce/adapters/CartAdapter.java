package com.example.FakeCommerce.adapters;

import com.example.FakeCommerce.dtos.CartItemResponseDto;
import com.example.FakeCommerce.dtos.GetCartResponseDto;
import com.example.FakeCommerce.schema.Cart;
import com.example.FakeCommerce.schema.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartAdapter {

    public CartItemResponseDto mapToCartItemResponseDto(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }

        BigDecimal productPrice = cartItem.getProduct() != null && cartItem.getProduct().getPrice() != null
                ? cartItem.getProduct().getPrice()
                : BigDecimal.ZERO;

        int quantity = cartItem.getQuantity() != null ? cartItem.getQuantity() : 0;
        BigDecimal subTotal = productPrice.multiply(BigDecimal.valueOf(quantity));

        return CartItemResponseDto.builder()
                .id(cartItem.getId())
                .productId(cartItem.getProduct() != null ? cartItem.getProduct().getId() : null)
                .productName(cartItem.getProduct() != null ? cartItem.getProduct().getTitle() : null)
                .productPrice(productPrice)
                .quantity(quantity)
                .subTotal(subTotal)
                .build();
    }

    public GetCartResponseDto mapToGetCartResponseDto(Cart cart) {
        if (cart == null) {
            return null;
        }

        List<CartItemResponseDto> itemResponseDtos = List.of();
        BigDecimal totalAmount = BigDecimal.ZERO;

        if (cart.getCartItems() != null) {
            itemResponseDtos = cart.getCartItems().stream()
                    .map(this::mapToCartItemResponseDto)
                    .collect(Collectors.toList());

            totalAmount = itemResponseDtos.stream()
                    .map(CartItemResponseDto::getSubTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        return GetCartResponseDto.builder()
                .id(cart.getId())
                .userId(cart.getUser() != null ? cart.getUser().getId() : null)
                .status(cart.getStatus())
                .items(itemResponseDtos)
                .totalAmount(totalAmount)
                .build();
    }
}