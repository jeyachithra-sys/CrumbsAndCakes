package com.crumbs.cakes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.crumbs.cakes.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
