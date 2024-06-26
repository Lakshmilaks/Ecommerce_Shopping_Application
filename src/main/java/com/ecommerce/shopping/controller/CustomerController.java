package com.ecommerce.shopping.controller;

import com.ecommerce.shopping.responsedto.UserResponse;
import com.ecommerce.shopping.service.CustomerService;
import com.ecommerce.shopping.utility.ResponseStructure;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    //------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/customers/{customerId}")
    public ResponseEntity<ResponseStructure<UserResponse>> findCustomer(@Valid @PathVariable Long customerId) {
        return customerService.findCustomer(customerId);
    }
    //------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/customers")
    public ResponseEntity<ResponseStructure<List<UserResponse>>> findCustomers() {
        return customerService.findCustomers();
    }
}
