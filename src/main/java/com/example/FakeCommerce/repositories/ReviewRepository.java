package com.example.FakeCommerce.repositories;

import com.example.FakeCommerce.schema.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByProductId(Long id);

    List<Review> findAllByOrderId(Long id);

    boolean existsByOrderIdAndProductId(Long orderId, Long productId);
}
