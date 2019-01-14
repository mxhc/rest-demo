package com.smort.repositories;

import com.smort.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    OrderItem findByIdAndOrderId(Long iid, Long oid);

    void deleteByIdAndOrderId(Long iid, Long oid);
}
