package com.ecommerce.shopping.service;

import com.ecommerce.shopping.enums.UserRole;
import com.ecommerce.shopping.requestdto.AuthRequest;
import com.ecommerce.shopping.requestdto.OtpVerificationRequest;
import com.ecommerce.shopping.requestdto.UserRequest;
import com.ecommerce.shopping.responsedto.AuthResponse;
import com.ecommerce.shopping.responsedto.LogoutResponse;
import com.ecommerce.shopping.responsedto.UserResponse;
import com.ecommerce.shopping.utility.ResponseStructure;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
   
    ResponseEntity<ResponseStructure<UserResponse>> updateUser(UserRequest userRequest, Long userId);

    ResponseEntity<ResponseStructure<UserResponse>> findUser(Long userId);

    ResponseEntity<ResponseStructure<List<UserResponse>>> findUsers();

	ResponseEntity<ResponseStructure<UserResponse>> verifyUser(OtpVerificationRequest otpVerificationRequest);

	ResponseEntity<ResponseStructure<UserResponse>> saveUser(UserRequest userRequest, UserRole seller);

	ResponseEntity<ResponseStructure<AuthResponse>> refreshLogin(String refreshToken);

	ResponseEntity<ResponseStructure<AuthResponse>> login(AuthRequest authRequest);

	ResponseEntity<LogoutResponse> logout(String refreshToken, String accessToken);

	ResponseEntity<LogoutResponse> logoutFromOtherDevices(String refreshToken, String accessToken);

	ResponseEntity<LogoutResponse> logoutFromAllDevices(String refreshToken, String accessToken);


}
