package com.crumbs.cakes.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Shopping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String customer_name;
    private String customer_email;
    private String customer_phone;

    @Column(columnDefinition = "TEXT")
    private String customer_address;

    private String payment_method;

    @Enumerated(EnumType.STRING)
    private OrderStatus order_status = OrderStatus.Pending;

    private LocalDateTime created_at = LocalDateTime.now();

    // Enum for order status
    public enum OrderStatus {
        Pending, Shipped, Delivered, Cancelled
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCustomer_name() { return customer_name; }
    public void setCustomer_name(String customer_name) { this.customer_name = customer_name; }

    public String getCustomer_email() { return customer_email; }
    public void setCustomer_email(String customer_email) { this.customer_email = customer_email; }

    public String getCustomer_phone() { return customer_phone; }
    public void setCustomer_phone(String customer_phone) { this.customer_phone = customer_phone; }

    public String getCustomer_address() { return customer_address; }
    public void setCustomer_address(String customer_address) { this.customer_address = customer_address; }

    public String getPayment_method() { return payment_method; }
    public void setPayment_method(String payment_method) { this.payment_method = payment_method; }

    public OrderStatus getOrder_status() { return order_status; }
    public void setOrder_status(OrderStatus order_status) { this.order_status = order_status; }

    public LocalDateTime getCreated_at() { return created_at; }
    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }
}
