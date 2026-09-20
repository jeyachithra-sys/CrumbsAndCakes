package com.crumbs.cakes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.crumbs.cakes.model.BirthdayCake;

public interface BirthdayCakeRepository extends JpaRepository<BirthdayCake, Integer> {}
