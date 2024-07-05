package com.ecommerce.shopping.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.shopping.entity.RefreshToken;
import com.ecommerce.shopping.entity.User;

public interface RefreshRepo extends JpaRepository<RefreshToken, Integer> {

	Optional<RefreshToken> findByRefreshToken(String rt);

	List<RefreshToken> findByUserAndIsBlockedAndRefreshTokenNot(User user, boolean b, String refreshToken);

	List<RefreshToken> findByUserAndIsBlocked(User user, boolean b);

}
