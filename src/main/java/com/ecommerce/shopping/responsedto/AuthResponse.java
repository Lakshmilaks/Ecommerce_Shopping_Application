package com.ecommerce.shopping.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {

	private Long userId;
	private String username;
	private long accessExpiration;
	private long refreshExpiration;
}
