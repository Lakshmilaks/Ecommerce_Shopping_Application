package com.ecommerce.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.shopping.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}
