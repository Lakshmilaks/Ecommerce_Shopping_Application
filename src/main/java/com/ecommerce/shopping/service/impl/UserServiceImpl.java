package com.ecommerce.shopping.service.impl;

import com.ecommerce.shopping.entity.Customer;
import com.ecommerce.shopping.entity.Seller;
import com.ecommerce.shopping.entity.User;
import com.ecommerce.shopping.enums.UserRole;
import com.ecommerce.shopping.exception.InvalidOtpException;
import com.ecommerce.shopping.exception.OtpExpiredException;
import com.ecommerce.shopping.exception.UserAlreadyExistException;
import com.ecommerce.shopping.exception.UserExpiredException;
import com.ecommerce.shopping.exception.UserNotExistException;
import com.ecommerce.shopping.jwt.JwtService;
import com.ecommerce.shopping.mailservice.MailService;
import com.ecommerce.shopping.mailservice.MessageData;
import com.ecommerce.shopping.repository.CustomerRepository;
import com.ecommerce.shopping.repository.SellerRepository;
import com.ecommerce.shopping.repository.UserRepository;
import com.ecommerce.shopping.requestdto.AuthRequest;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
	
	private final AuthenticationManager authenticationManager;
	
	private final JwtService jwtService;


	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> saveUser(UserRequest userRequest, UserRole userRole) {
		boolean emailExist = userRepository.existsByEmail(userRequest.getEmail());
		if(emailExist) {
			throw new UserAlreadyExistException("Email: "+userRequest.getEmail()+" already existed!!");
		}else {
			User user = null;
			switch (userRole) {
			case SELLER -> user = new Seller();
			case CUSTOMER -> user = new Customer();
			}
			if (user != null) {
				user = userMapper.mapUserRequestToUser(userRequest, user);
				user.setUserRole(userRole);
				userCache.put(userRequest.getEmail(), user);
				int otp = random.nextInt(100000, 999999);
				otpCache.put(userRequest.getEmail(), otp + "");

				 mailSend(user.getEmail(), "OTP verification for EcommerceShoppingApp", "<h3>Welcome to Ecommerce Shopping Applicationa</h3></br><h4>Otp : " + otp + "</h4>");
				
				
				
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseStructure<UserResponse>()
						.setStatus(HttpStatus.ACCEPTED.value())
						.setMessage("otp sent!!!")
						.setData(userMapper.mapUserToUserResponse(user)));
			} else throw new UserAlreadyExistException("Bad Request");
		}
	
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

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> verifyUser(OtpVerificationRequest otpVerificationRequest) {
		User user = userCache.getIfPresent(otpVerificationRequest.getEmail());
		String existingotp = otpCache.getIfPresent(otpVerificationRequest.getEmail());
		String requestedotp=otpVerificationRequest.getOtp();
		ResponseStructure<UserResponse> responseStructure = new ResponseStructure<>();
		
		if (user == null) 
			throw new UserExpiredException("User has expired");
		else if (existingotp == null && existingotp.equals(requestedotp)) 
			throw new OtpExpiredException("Otp is expired");
		else if(!existingotp.equals(requestedotp)) 
			throw new  InvalidOtpException("Invalid otp");
		else if(existingotp.equals(requestedotp)) {

			
			String userGen = user.getEmail().split("@")[0];
			int temp = 0;
			while (userRepository.existsByEmail(userGen)) {
			    userGen += temp;
			    temp++;
			}
			user.setUsername(userGen);

			user = userRepository.save(user);
			// Send mail for confirmation
			mailSend(user.getEmail(), "Email Verification done", "Your account is create in EcommerceShoppingApp</br> Your username is : "+userGen);

			return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseStructure<UserResponse>()
					.setStatus(HttpStatus.CREATED.value())
					.setMessage(user.getUserRole() + " Created")
					.setData(userMapper.mapUserToUserResponse(user)));
		} else {
			throw new OtpExpiredException("Otp is expired");
		}
	}

	private void mailSend(String email, String subject, String text) {
		// TODO Auto-generated method stub
		 MessageData messageData = new MessageData();
	        messageData.setTo(email);
	        messageData.setSubject(subject);
	        messageData.setText(text);
	        messageData.setSentDate(new Date());
	        try {
	            mailService.sendMail(messageData);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}

	@Override
	public String login(AuthRequest authRequest) {
	
		
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authenticate.isAuthenticated())
                return jwtService.createJwtToken(authRequest.getUsername(), 1000000L);
            else
                throw new BadCredentialsException("Invalid Credentials");
       
	}
	
}





