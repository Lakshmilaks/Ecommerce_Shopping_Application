package com.ecommerce.shopping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.shopping.entity.AccessToken;

public interface AccessRepository extends JpaRepository<AccessToken, Integer>{




	

}