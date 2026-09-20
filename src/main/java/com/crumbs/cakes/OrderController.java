package com.crumbs.cakes;

import com.crumbs.cakes.model.Order;
import com.crumbs.cakes.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/order")
    public Order createOrder(@RequestBody Order order) {
        return orderRepository.save(order);
    }
}
