package com.crumbs.cakes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.crumbs.cakes.model.Shopping;

@Repository
public interface ShoppingRepository extends JpaRepository<Shopping, Integer> {
}
