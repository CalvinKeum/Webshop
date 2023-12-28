package com.calvinkeum.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.calvinkeum.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
