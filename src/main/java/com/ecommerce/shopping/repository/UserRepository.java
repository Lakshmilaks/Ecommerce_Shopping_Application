package com.ecommerce.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.shopping.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
