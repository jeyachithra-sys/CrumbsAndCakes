package com.crumbs.cakes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.crumbs.cakes.model.CustomCake;

public interface CustomCakeRepository extends JpaRepository<CustomCake, Integer> {}
