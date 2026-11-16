package com.example.FakeCommerce.services;

import com.example.FakeCommerce.adapters.CartAdapter;
import com.example.FakeCommerce.dtos.AddToCartRequestDto;
import com.example.FakeCommerce.dtos.GetCartResponseDto;
import com.example.FakeCommerce.dtos.UpdateCartItemRequestDto;
import com.example.FakeCommerce.exceptions.ResourceNotFoundException;
import com.example.FakeCommerce.repositories.CartRepository;
import com.example.FakeCommerce.repositories.ProductRepository;
import com.example.FakeCommerce.repositories.UserRepository;
import com.example.FakeCommerce.schema.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartAdapter cartAdapter; // Inject the adapter

    @Transactional
    public GetCartResponseDto getActiveCartDto(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cartRepository.findByUserIdAndStatusWithDetails(userId, CartStatus.ACTIVE)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().user(user).build();
                    return cartRepository.save(newCart);
                });

        return cartAdapter.mapToGetCartResponseDto(cart);
    }

    @Transactional
    public GetCartResponseDto addToCart(Long userId, AddToCartRequestDto requestDto){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Fetch cart with deep join so products are initialized
        Cart userCart = cartRepository.findByUserIdAndStatusWithDetails(userId, CartStatus.ACTIVE)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder().user(user).build();
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + requestDto.getProductId()));

        Optional<CartItem> existingItem = userCart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        int quantityToAdd = requestDto.getQuantity() != null ? requestDto.getQuantity() : 1;

        if(existingItem.isPresent()){
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantityToAdd);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(userCart)
                    .product(product)
                    .quantity(quantityToAdd)
                    .build();

            userCart.getCartItems().add(newItem);
        }

        Cart savedCart = cartRepository.save(userCart);
        return cartAdapter.mapToGetCartResponseDto(savedCart);
    }

    @Transactional
    public GetCartResponseDto updateCartItem(Long userId, Long productId, UpdateCartItemRequestDto requestDto){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart userCart = cartRepository.findByUserIdAndStatusWithDetails(userId, CartStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Active cart not found for user id: " + userId));

        CartItem targetItem = userCart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

        switch(requestDto.getAction()){
            case INCREMENT -> targetItem.setQuantity(targetItem.getQuantity() + 1);
            case DECREMENT -> {
                if(targetItem.getQuantity() <= 1){
                    userCart.getCartItems().remove(targetItem);
                } else {
                    targetItem.setQuantity(targetItem.getQuantity() - 1);
                }
            }
        }

        Cart savedCart = cartRepository.save(userCart);
        return cartAdapter.mapToGetCartResponseDto(savedCart);
    }
}