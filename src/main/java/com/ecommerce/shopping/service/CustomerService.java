package com.ecommerce.shopping.service;

import com.ecommerce.shopping.responsedto.UserResponse;
import com.ecommerce.shopping.utility.ResponseStructure;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {
    ResponseEntity<ResponseStructure<UserResponse>> findCustomer(Long customerId);

    ResponseEntity<ResponseStructure<List<UserResponse>>> findCustomers();
//    Demo commit add for testing
}
