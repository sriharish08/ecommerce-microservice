package com.sri.order_service.order.repository;


import com.sri.order_service.order.entity.Order;
import com.sri.order_service.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    List<Order> findByOrderStatus(OrderStatus orderStatus);

    List<Order> findByCreatedAtBetween(LocalDateTime start,LocalDateTime end);

    boolean existsByOrderNumber(String orderNumber);
}
