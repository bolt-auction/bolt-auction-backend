package com.neoga.platform.order.repository;

import com.neoga.platform.order.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    Optional<Orders> findByItem_Id(Long itemId);
}
