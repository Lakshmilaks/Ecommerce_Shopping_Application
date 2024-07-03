package com.ecommerce.shopping.responsedto;


import com.ecommerce.shopping.enums.UserRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
   
	private Long userId;
    private String username;
    private String email;
    private UserRole userRole;
}
