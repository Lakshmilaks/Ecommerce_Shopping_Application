package com.ecommerce.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.shopping.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
