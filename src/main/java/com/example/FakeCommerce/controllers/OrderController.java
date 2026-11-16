package com.example.FakeCommerce.controllers;

import com.example.FakeCommerce.dtos.ApiResponseDto;
import com.example.FakeCommerce.dtos.CheckoutRequestDto;
import com.example.FakeCommerce.dtos.GetOrderResponseDto;
import com.example.FakeCommerce.dtos.UpdateOrderStatusDto;
import com.example.FakeCommerce.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/user/{userId}/checkout")
    public ResponseEntity<ApiResponseDto<GetOrderResponseDto>> checkoutCart(
            @PathVariable Long userId,
            @RequestBody CheckoutRequestDto requestDto) {
        GetOrderResponseDto orderResponse = orderService.checkoutCart(userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("Cart checked out successfully", orderResponse));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDto<List<GetOrderResponseDto>>> getOrdersByUser(@PathVariable Long userId) {
        List<GetOrderResponseDto> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(ApiResponseDto.success("User orders fetched successfully", orders));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponseDto<GetOrderResponseDto>> getOrderById(@PathVariable Long orderId) {
        GetOrderResponseDto order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(ApiResponseDto.success("Order fetched successfully", order));
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponseDto<GetOrderResponseDto>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusDto requestDto) {
        GetOrderResponseDto updatedOrder = orderService.updateOrderStatus(orderId, requestDto);
        return ResponseEntity.ok(ApiResponseDto.success("Order status updated successfully", updatedOrder));
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<ApiResponseDto<List<GetOrderResponseDto>>> getOrdersByVendor(@PathVariable Long vendorId) {
        List<GetOrderResponseDto> vendorOrders = orderService.getOrdersByVendor(vendorId);
        return ResponseEntity.ok(ApiResponseDto.success("Vendor orders fetched successfully", vendorOrders));
    }
}