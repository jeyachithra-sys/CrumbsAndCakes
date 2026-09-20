package com.crumbs.cakes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "customcake")
public class CustomCake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private double price;
    private String description;
    private String image_src;

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImage_src() { return image_src; }
    public void setImage_src(String image_src) { this.image_src = image_src; }
}
