package com.ecommerce.shopping.user.mapper;

import com.ecommerce.shopping.entity.User;
import com.ecommerce.shopping.requestdto.UserRequest;
import com.ecommerce.shopping.responsedto.UserResponse;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
	@Autowired
	private PasswordEncoder passwordEncoder;
	
    public User mapUserRequestToUser(UserRequest userRequest, User user){
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        return  user;
    }

    public UserResponse mapUserToUserResponse(User user){
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .userRole(user.getUserRole())
                .build();
    }
}
