package com.ecommerce.shopping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.shopping.entity.RefreshToken;

public interface RefreshRepo extends JpaRepository<RefreshToken, Integer> {

	 Optional<RefreshToken> findByRefreshToken(String rt);

}
