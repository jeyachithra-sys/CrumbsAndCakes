package com.crumbs.cakes.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String cake_names;
    private Double total_amount;
    private Integer quantity;  // new column for quantity
    private LocalDateTime created_at = LocalDateTime.now();

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCake_names() { return cake_names; }
    public void setCake_names(String cake_names) { this.cake_names = cake_names; }

    public Double getTotal_amount() { return total_amount; }
    public void setTotal_amount(Double total_amount) { this.total_amount = total_amount; }

    public Integer getQuantity() { return quantity; }  // getter for quantity
    public void setQuantity(Integer quantity) { this.quantity = quantity; }  // setter for quantity

    public LocalDateTime getCreated_at() { return created_at; }
    public void setCreated_at(LocalDateTime created_at) { this.created_at = created_at; }
}
