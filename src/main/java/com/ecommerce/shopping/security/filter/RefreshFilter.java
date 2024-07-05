package com.ecommerce.shopping.security.filter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecommerce.shopping.entity.RefreshToken;
import com.ecommerce.shopping.enums.UserRole;
import com.ecommerce.shopping.jwt.JwtService;
import com.ecommerce.shopping.repository.RefreshRepo;
import com.ecommerce.shopping.utility.FilterExceptionHandle;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
@AllArgsConstructor
public class RefreshFilter extends OncePerRequestFilter{

	private JwtService jwtService;
	private final RefreshRepo refreshRepo;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		 
		 Cookie[] cookies = request.getCookies();
	        String rt = null;
	        if (cookies != null) {
	            for (Cookie cookie : cookies) {
	                if (cookie.getName().equals("rt"))
	                    rt = cookie.getValue();
	            }
	        } else {
	            FilterExceptionHandle.handleJwtExpire(response,
	                    HttpStatus.UNAUTHORIZED.value(),
	                    "refresh token not found",
	                    "Refresh Token is not available");
	        }
	        Optional<RefreshToken> refreshToken = refreshRepo.findByRefreshToken(rt);
	        if (refreshToken.isPresent() && !refreshToken.get().isBlocked()) {
	            String username = jwtService.extractUserName(rt);
	            String userRole = jwtService.extractUserRole(rt);

	            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,null, List.of(new SimpleGrantedAuthority(userRole.toUpperCase())));
	                authenticationToken.setDetails(new WebAuthenticationDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	            }
	        } else {
	            FilterExceptionHandle.handleJwtExpire(response,
	                    HttpStatus.UNAUTHORIZED.value(),
	                    "refresh token not found",
	                    "Refresh Token is not available");
	        }

	        filterChain.doFilter(request, response);
	    }
	}