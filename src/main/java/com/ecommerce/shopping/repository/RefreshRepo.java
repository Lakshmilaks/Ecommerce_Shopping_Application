package com.ecommerce.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.shopping.entity.RefreshToken;

public interface RefreshRepo extends JpaRepository<RefreshToken, Integer> {

//	boolean existsByTokenAndIsBlocked(String token, boolean isBlocked);

}
