package com.example.FakeCommerce.controllers;

import com.example.FakeCommerce.dtos.AddToCartRequestDto;
import com.example.FakeCommerce.dtos.ApiResponseDto;
import com.example.FakeCommerce.dtos.GetCartResponseDto;
import com.example.FakeCommerce.dtos.UpdateCartItemRequestDto;
import com.example.FakeCommerce.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDto<GetCartResponseDto>> getActiveCart(@PathVariable Long userId) {
        GetCartResponseDto cart = cartService.getActiveCartDto(userId);
        return ResponseEntity.ok(ApiResponseDto.success("Active cart fetched successfully", cart));
    }

    @PostMapping("/user/{userId}/items")
    public ResponseEntity<ApiResponseDto<GetCartResponseDto>> addToCart(
            @PathVariable Long userId,
            @RequestBody AddToCartRequestDto requestDto) {
        GetCartResponseDto updatedCart = cartService.addToCart(userId, requestDto);
        return ResponseEntity.ok(ApiResponseDto.success("Item added to cart successfully", updatedCart));
    }

    @PatchMapping("/user/{userId}/items/{productId}")
    public ResponseEntity<ApiResponseDto<GetCartResponseDto>> updateCartItem(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestBody UpdateCartItemRequestDto requestDto) {
        GetCartResponseDto updatedCart = cartService.updateCartItem(userId, productId, requestDto);
        return ResponseEntity.ok(ApiResponseDto.success("Cart updated successfully", updatedCart));
    }
}