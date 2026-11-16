package com.example.FakeCommerce.repositories;

import com.example.FakeCommerce.schema.Cart;
import com.example.FakeCommerce.schema.CartStatus;
import com.example.FakeCommerce.schema.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserAndStatus(User user, CartStatus status); // FIXED: Enabled for active cart lookup
    Optional<Cart> findByUser_Id(Long userId);

    @Query("SELECT c FROM Cart c " +
            "JOIN FETCH c.user " +
            "LEFT JOIN FETCH c.cartItems ci " +
            "LEFT JOIN FETCH ci.product " +
            "WHERE c.user.id = :userId AND c.status = :status")
    Optional<Cart> findByUserIdAndStatusWithDetails(@Param("userId") Long userId, @Param("status") CartStatus status);
}