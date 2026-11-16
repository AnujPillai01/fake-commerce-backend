package com.example.FakeCommerce.repositories;

import com.example.FakeCommerce.schema.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p JOIN FETCH p.category JOIN FETCH p.vendor WHERE p.id = :id")
    Optional<Product> findProductWithDetails(@Param("id") Long id);

    List<Product> findByVendorId(Long vendorId);
}
