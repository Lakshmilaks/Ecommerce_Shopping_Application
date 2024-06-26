package com.ecommerce.shopping.user.mapper;

import com.ecommerce.shopping.entity.User;
import com.ecommerce.shopping.requestdto.UserRequest;
import com.ecommerce.shopping.responsedto.UserResponse;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User mapUserRequestToUser(UserRequest userRequest, User user){
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
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
