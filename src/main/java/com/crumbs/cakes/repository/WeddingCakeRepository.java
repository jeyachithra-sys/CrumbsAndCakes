package com.crumbs.cakes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.crumbs.cakes.model.WeddingCake;

public interface WeddingCakeRepository extends JpaRepository<WeddingCake, Integer> {}
