package com.crumbs.cakes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.crumbs.cakes.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByUsername(String username);
}
