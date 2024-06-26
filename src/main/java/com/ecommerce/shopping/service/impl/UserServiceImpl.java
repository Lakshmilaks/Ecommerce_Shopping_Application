package com.ecommerce.shopping.service.impl;

import com.ecommerce.shopping.entity.Customer;
import com.ecommerce.shopping.entity.Seller;
import com.ecommerce.shopping.entity.User;
import com.ecommerce.shopping.enums.UserRole;
import com.ecommerce.shopping.exception.UserAlreadyExistException;
import com.ecommerce.shopping.exception.UserExpiredException;
import com.ecommerce.shopping.exception.UserNotExistException;
import com.ecommerce.shopping.mailservice.MailService;
import com.ecommerce.shopping.mailservice.MessageData;
import com.ecommerce.shopping.repository.CustomerRepository;
import com.ecommerce.shopping.repository.SellerRepository;
import com.ecommerce.shopping.repository.UserRepository;
import com.ecommerce.shopping.requestdto.OtpVerificationRequest;
import com.ecommerce.shopping.requestdto.UserRequest;
import com.ecommerce.shopping.responsedto.UserResponse;
import com.ecommerce.shopping.service.UserService;
import com.ecommerce.shopping.user.mapper.UserMapper;
import com.ecommerce.shopping.utility.ResponseStructure;
import com.google.common.cache.Cache;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	private final UserMapper userMapper;

	private final SellerRepository sellerRepository;

	private final CustomerRepository customerRepository;

	private final Cache<String, User> userCache;

	private final Cache<String, String> otpCache;

	private final Random random;

	private final MailService mailService;
	
	private final MailSender mailSender;

	

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> addUser(UserRequest userRequest, UserRole userRole) {
		boolean emailExist = userRepository.existsByEmail(userRequest.getEmail());
		if (emailExist)
			throw new UserAlreadyExistException("Email : " + userRequest.getEmail() + ", is already exist");
		else {
			if (userRole.equals(UserRole.CUSTOMER)) {
				Customer customer = (Customer) userMapper.mapUserRequestToUser(userRequest, new Customer());
				customer.setUserRole(userRole);

				customer = customerRepository.save(customer);
				return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseStructure<UserResponse>()
						.setStatus(HttpStatus.CREATED.value())
						.setMessage("Customer Created")
						.setData(userMapper.mapUserToUserResponse(customer)));
			} else {
				Seller seller = (Seller) userMapper.mapUserRequestToUser(userRequest, new Seller());
				seller.setUserRole(userRole);
				seller = sellerRepository.save(seller);

				return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseStructure<UserResponse>()
						.setStatus(HttpStatus.CREATED.value())
						.setMessage("Seller Created")
						.setData(userMapper.mapUserToUserResponse(seller)));
			}
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(UserRequest userRequest, UserRole userRole) {
		User user = null;
		switch (userRole) {
		case SELLER -> user = new Seller();
		case CUSTOMER -> user = new Customer();
		}
		if (user != null) {
			user = userMapper.mapUserRequestToUser(userRequest, user);
			userCache.put(userRequest.getEmail(), user);
			int otp = random.nextInt(100000, 999999);

			MessageData messageData = new MessageData();
			messageData.setTo(user.getEmail());
			messageData.setSubject(user.getUsername());
			messageData.setSentDate(new Date());
			messageData.setText("otp: "+otp);
			try {
				mailService.sendMail(messageData);
			}catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(otp);

			otpCache.put(userRequest.getEmail(), otp + "");
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseStructure<UserResponse>()
					.setStatus(HttpStatus.ACCEPTED.value())
					.setMessage("Verify otp")
					.setData(userMapper.mapUserToUserResponse(user)));
		} else throw new UserAlreadyExistException("Bad Request");
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> verifyUser(OtpVerificationRequest otpVerificationRequest) {
		User user = userCache.getIfPresent(otpVerificationRequest.getEmail());
		String existingotp = otpCache.getIfPresent(otpVerificationRequest.getEmail());
		String requestedotp=otpVerificationRequest.getOtp();
		ResponseStructure<UserResponse> responseStructure = new ResponseStructure<>();
		if (user == null) {
		    throw new UserExpiredException("User has expired");
		}

		if (existingotp != null && existingotp.equals(requestedotp)) {
		   Long userId = user.getUserId();
		    UserResponse userResponse = new UserResponse(userId, user.getEmail());
		    
//		    user.setUsername(user.getUsername());
//		    user.setEmail(user.getEmail());
//		    user.setUserRole(user.getUserRole());
		    
	        

		    // Send confirmation email
		    sendConfirmationEmail(user.getEmail());
		    
		    responseStructure = ResponseStructure.<UserResponse>builder()
		            .status(200)
		            .message("OTP verified successfully")
		            .data(userResponse)
		            .build();

		    return new ResponseEntity<>(responseStructure, HttpStatus.OK);
		} else {
		    responseStructure = ResponseStructure.<UserResponse>builder()
		            .status(400)
		            .message("Invalid OTP")
		            .data(null)
		            .build();

		    return new ResponseEntity<>(responseStructure, HttpStatus.BAD_REQUEST);
		}
	}
		private void sendConfirmationEmail(String email) {
			// Create a simple mail message
						SimpleMailMessage mailMessage = new SimpleMailMessage();
						mailMessage.setTo(email);
						mailMessage.setSubject("Account Verification Successful");
						mailMessage.setText("Dear " + email + ",\n\nYour account has been successfully verified.\n\nBest regards,\n[Your App Name]");
					
						// Send the mail using a mail sender
						mailSender.send(mailMessage);		
	}

		

@Override
public ResponseEntity<ResponseStructure<UserResponse>> findUser(Long userId) {
	return userRepository.findById(userId).map(user -> {
		return ResponseEntity.status(HttpStatus.FOUND).body(new ResponseStructure<UserResponse>()
				.setStatus(HttpStatus.FOUND.value())
				.setMessage("User Founded")
				.setData(userMapper.mapUserToUserResponse(user)));
	}).orElseThrow(() -> new UserNotExistException("UserId : " + userId + ", is not exist"));
}

@Override
public ResponseEntity<ResponseStructure<List<UserResponse>>> findUsers() {
	List<UserResponse> userResponseList = userRepository.findAll()
			.stream()
			.map(userMapper::mapUserToUserResponse)
			.toList();
	return ResponseEntity.status(HttpStatus.FOUND).body(new ResponseStructure<List<UserResponse>>()
			.setMessage("Users are Founded")
			.setData(userResponseList));
}

@Override
public ResponseEntity<ResponseStructure<UserResponse>> updateUser(UserRequest userRequest, Long userId) {
	return userRepository.findById(userId).map(user -> {
		user = userMapper.mapUserRequestToUser(userRequest, user);
		user = userRepository.save(user);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseStructure<UserResponse>()
				.setStatus(HttpStatus.OK.value())
				.setMessage("User Updated")
				.setData(userMapper.mapUserToUserResponse(user)));
	}).orElseThrow(() -> new UserNotExistException("UserId : " + userId + ", is not exist"));
}
}
