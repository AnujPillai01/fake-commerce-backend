package com.example.FakeCommerce.services;

import com.example.FakeCommerce.adapters.OrderAdapter;
import com.example.FakeCommerce.dtos.CheckoutRequestDto;
import com.example.FakeCommerce.dtos.GetOrderResponseDto;
import com.example.FakeCommerce.dtos.UpdateOrderStatusDto;
import com.example.FakeCommerce.exceptions.ResourceNotFoundException;
import com.example.FakeCommerce.repositories.*;
import com.example.FakeCommerce.schema.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final OrderAdapter orderAdapter;

    @Transactional(rollbackFor = Exception.class)
    public GetOrderResponseDto checkoutCart(Long userId, CheckoutRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        Cart activeCart = cartRepository.findByUserIdAndStatusWithDetails(userId, CartStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("No active cart found for user ID: " + userId));

        if (activeCart.getCartItems() == null || activeCart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cannot checkout an empty cart");
        }

        // Validate inventory & deduct stock
        for (CartItem cartItem : activeCart.getCartItems()) {
            Product product = productRepository.findById(cartItem.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found ID: " + cartItem.getProduct().getId()));

            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getTitle()
                        + ". Available: " + product.getStockQuantity() + ", Requested: " + cartItem.getQuantity());
            }

            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }

        BigDecimal totalAmount = activeCart.getCartItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .user(user)
                .totalAmount(totalAmount)
                .shippingAddress(requestDto.getShippingAddress())
                .status(OrderStatus.PENDING_PAYMENT)
                .build();

        List<OrderItem> orderItems = activeCart.getCartItems().stream()
                .map(cartItem -> OrderItem.builder()
                        .order(order)
                        .product(cartItem.getProduct())
                        .vendor(cartItem.getProduct().getVendor())
                        .quantity(cartItem.getQuantity())
                        .unitPrice(cartItem.getProduct().getPrice())
                        .build())
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // Mark cart as checked out
        activeCart.setStatus(CartStatus.CHECKED_OUT);
        cartRepository.save(activeCart);

        return orderAdapter.mapToOrderResponseDto(savedOrder);
    }

    public List<GetOrderResponseDto> getOrdersByUser(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orderAdapter.mapToOrderResponseDtoList(orders);
    }

    public GetOrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        return orderAdapter.mapToOrderResponseDto(order);
    }

    @Transactional
    public GetOrderResponseDto updateOrderStatus(Long orderId, UpdateOrderStatusDto requestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        order.setStatus(requestDto.getStatus());
        Order updatedOrder = orderRepository.save(order);

        return orderAdapter.mapToOrderResponseDto(updatedOrder);
    }

    public List<GetOrderResponseDto> getOrdersByVendor(Long vendorId) {
        // Find distinct orders containing items belonging to this vendor
        List<OrderItem> vendorItems = orderItemRepository.findByVendorId(vendorId);
        List<Order> vendorOrders = vendorItems.stream()
                .map(OrderItem::getOrder)
                .distinct()
                .collect(Collectors.toList());

        return orderAdapter.mapToOrderResponseDtoList(vendorOrders);
    }
}