package com.example.order.repository;

import com.example.order.entity.OrderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRequestRepository extends JpaRepository<OrderRequest, String> {
}
