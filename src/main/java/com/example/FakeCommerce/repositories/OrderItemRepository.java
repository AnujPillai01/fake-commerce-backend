package com.example.FakeCommerce.repositories;

import com.example.FakeCommerce.schema.Order;
import com.example.FakeCommerce.schema.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    List<OrderItem> findByOrderId(Long orderId);

    @Query("SELECT oi from OrderItem oi JOIN FETCH oi.product WHERE oi.order = :order")
    List<OrderItem> findByOrderWithProduct(@Param("order") Order order);
}
