package com.ecommerce.shopping.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ecommerce.shopping.entity.User;

import lombok.AllArgsConstructor;

public class UserDetailService implements UserDetails{

	 private final String username;
	    private final String password;
	    private final List<GrantedAuthority> grantedAuthorities;

	    public UserDetailService(User user) {
	        username = user.getUsername();
	        password = user.getPassword();
	        String role = user.getRole()!= null? user.getRole().toString() : "ROLE_USER"; // default role
	        grantedAuthorities = List.of(new SimpleGrantedAuthority(role));
//	        grantedAuthorities = List.of(new SimpleGrantedAuthority(user.getRole().toString()));
	    }

	    @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        return grantedAuthorities;
	    }

	    @Override
	    public String getPassword() {
	        return password;
	    }

	    @Override
	    public String getUsername() {
	        return username;
	    }

}
