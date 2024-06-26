package com.ecommerce.shopping.responsedto;


import com.ecommerce.shopping.enums.UserRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    public UserResponse(Long userId2, String email2) {
		// TODO Auto-generated constructor stub
	}
	private Long userId;
    private String username;
    private String email;
    private UserRole userRole;
}
