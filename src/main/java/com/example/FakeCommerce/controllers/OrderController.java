package com.example.FakeCommerce.controllers;

import com.example.FakeCommerce.dtos.*;
import com.example.FakeCommerce.schema.Order;
import com.example.FakeCommerce.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<GetOrderResponseDto>>> getAllOrders(){
        List<GetOrderResponseDto> orders = orderService.getAllOrders();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponseDto.success("Orders fetched successfully",orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<GetOrderResponseDto>> getOrderById(@PathVariable Long id){
        GetOrderResponseDto order = orderService.getOrderById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponseDto.success("Order fetched successfully",order));
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<ApiResponseDto<GetOrderSummaryResponseDto>> getOrderSummary(@PathVariable Long id){
        GetOrderSummaryResponseDto responseDto = orderService.getOrderSummary(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success("Order summary fetched successfully",responseDto));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto<GetOrderResponseDto>> createOrder(@RequestBody CreateOrderRequestDto requestDto){
        GetOrderResponseDto order = orderService.createOrder(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success("Order created successfully",order));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<GetOrderResponseDto>> updateOrder(@PathVariable Long id, @RequestBody UpdateOrderRequestDto requestDto){
        GetOrderResponseDto order = orderService.updateOrder(id, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success("Order updated successfully",order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteOrder(@PathVariable Long id){
        orderService.deleteOrder(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponseDto.success("Order deleted successfully",null));
    }
}
