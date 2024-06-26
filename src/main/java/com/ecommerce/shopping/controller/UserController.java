package com.ecommerce.shopping.controller;

import com.ecommerce.shopping.enums.UserRole;
import com.ecommerce.shopping.requestdto.OtpVerificationRequest;
import com.ecommerce.shopping.requestdto.UserRequest;
import com.ecommerce.shopping.responsedto.UserResponse;
import com.ecommerce.shopping.service.UserService;
import com.ecommerce.shopping.utility.ResponseStructure;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sellers/register")
    public ResponseEntity<ResponseStructure<UserResponse>> addSeller(@Valid @RequestBody UserRequest userRequest) {
        return userService.addUser(userRequest, UserRole.SELLER);
    }
    @PostMapping("/sellers/registers")
    public ResponseEntity<ResponseStructure<UserResponse>> saveUser(@RequestBody UserRequest userRequest,UserRole userRole) {
        return userService.saveUser(userRequest, UserRole.SELLER);
    }
    

    @PostMapping("/customers/register")
    public ResponseEntity<ResponseStructure<UserResponse>> addCustomer(@Valid @RequestBody UserRequest userRequest) {
        return userService.addUser(userRequest, UserRole.CUSTOMER);
    }
     @PutMapping("/users/{userId}")
     public ResponseEntity<ResponseStructure<UserResponse>> updateUser(
            @Valid @RequestBody UserRequest userRequest,
            @Valid @PathVariable Long userId){
        return userService.updateUser(userRequest, userId);
     }
     
     @PostMapping("/users/otp")
     public ResponseEntity<ResponseStructure<UserResponse>> verifyUser(
             @RequestBody OtpVerificationRequest otpVerificationRequest){
         return userService.verifyUser(otpVerificationRequest);
     }
    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseStructure<UserResponse>> findUser(@Valid @PathVariable Long userId) {
        return userService.findUser(userId);
    }

    @GetMapping("/users")
    public ResponseEntity<ResponseStructure<List<UserResponse>>> findUsers() {
        return userService.findUsers();
    }


}
