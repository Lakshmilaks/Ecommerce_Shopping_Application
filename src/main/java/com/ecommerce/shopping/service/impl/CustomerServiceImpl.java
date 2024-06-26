package com.ecommerce.shopping.service.impl;

import com.ecommerce.shopping.exception.UserNotExistException;
import com.ecommerce.shopping.repository.CustomerRepository;
import com.ecommerce.shopping.responsedto.UserResponse;
import com.ecommerce.shopping.service.CustomerService;
import com.ecommerce.shopping.user.mapper.UserMapper;
import com.ecommerce.shopping.utility.ResponseStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

      @Autowired
      private CustomerRepository customerRepository;

      @Autowired
      private UserMapper userMapper;
    //------------------------------------------------------------------------------------------------------------------------

    @Override
    public ResponseEntity<ResponseStructure<UserResponse>> findCustomer(Long customerId) {
        return customerRepository.findById(customerId).map(customer -> {
            return ResponseEntity.status(HttpStatus.FOUND).body(new ResponseStructure<UserResponse>()
                    .setStatus(HttpStatus.FOUND.value())
                    .setMessage("Customer Founded")
                    .setData(userMapper.mapUserToUserResponse(customer)));
        }).orElseThrow(() -> new UserNotExistException("CustomerId : " + customerId + ", is not exist"));
    }
    //------------------------------------------------------------------------------------------------------------------------

    @Override
    public ResponseEntity<ResponseStructure<List<UserResponse>>> findCustomers() {
        List<UserResponse> customerResponseList = customerRepository.findAll()
                .stream()
                .map(customer -> userMapper.mapUserToUserResponse(customer))
                .toList();
        return ResponseEntity.status(HttpStatus.FOUND).body(new ResponseStructure<List<UserResponse>>()
                .setMessage("Customers are Founded")
                .setData(customerResponseList));
    }
    //------------------------------------------------------------------------------------------------------------------------

}
