package com.ecommerce.shopping.security.filter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecommerce.shopping.enums.UserRole;
import com.ecommerce.shopping.exception.InvalidJWTException;
import com.ecommerce.shopping.exception.JwtExpiredExcepton;
import com.ecommerce.shopping.jwt.JwtService;
import com.ecommerce.shopping.utility.ErrorStructure;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtService jwtService;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		 String rt = null;
	        String at = null;

	        Cookie[] cookies = request.getCookies();
	        if (cookies != null) {
	            for (Cookie cookie : cookies) {
	                if (cookie.getName().equals("rt"))
	                    rt = cookie.getName();
	                else if (cookie.getName().equals("at"))
	                    at = cookie.getName();
	            }
	        }
	        if (at != null) {
	            try {
	                Date expireDate = jwtService.extractExpirationDate(at);
	                String username = jwtService.extractUserName(at);
	                String userRole = jwtService.extractUserRole(at);

	                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	                    UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken
	                    		(username, null, List.of(new SimpleGrantedAuthority(userRole)));
	                    upat.setDetails(new WebAuthenticationDetails(request));
	                    SecurityContextHolder.getContext().setAuthentication(upat);
	                }
	            } catch (ExpiredJwtException e) {
	            	unhandledException(response,
	                        HttpStatus.UNAUTHORIZED.value(),
	                        "Failed to authenticate",
	                        "Token has already expired");
	                return;
	            } catch (JwtException e) {
	            	unhandledException(response,
	                        HttpStatus.UNAUTHORIZED.value(),
	                        "Failed to authenticate",
	                        "Invalid token");
	                return;
	            }
	        }
	        filterChain.doFilter(request, response);
	    }
	
	protected void unhandledException(HttpServletResponse response,int status,String message,String rootcause) throws ServletException, IOException {

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		ErrorStructure error = new ErrorStructure()
				.setStatus(HttpStatus.UNAUTHORIZED.value())
				.setMessage("failed to Authenticate")
				.setRootCause("The token is already expired");

		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), error);

	}

}
