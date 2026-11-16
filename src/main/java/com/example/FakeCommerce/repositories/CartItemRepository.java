package com.example.FakeCommerce.repositories;

import com.example.FakeCommerce.schema.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> { // FIXED: Integer changed to Long matching BaseEntity ID
    Optional<CartItem> findByCart_IdAndProduct_Id(Long cartId, Long productId);
}