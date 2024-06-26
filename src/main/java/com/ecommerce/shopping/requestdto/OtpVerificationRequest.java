package com.ecommerce.shopping.requestdto;

import lombok.Getter;

@Getter
public class OtpVerificationRequest {

	private String email;
	private String otp;
}
