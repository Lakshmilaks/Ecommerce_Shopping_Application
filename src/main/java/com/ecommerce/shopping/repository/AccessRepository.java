package com.ecommerce.shopping.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.shopping.entity.AccessToken;
import com.ecommerce.shopping.entity.User;

public interface AccessRepository extends JpaRepository<AccessToken, Integer>{

	Optional<AccessToken> findByAccessToken(String at);

	List<AccessToken> findByUserAndIsBlockedAndAccessTokenNot(User user, boolean b, String accessToken);

	List<AccessToken> findByUserAndIsBlocked(User user, boolean b);




	

}