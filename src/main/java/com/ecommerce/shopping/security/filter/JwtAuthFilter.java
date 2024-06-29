package com.ecommerce.shopping.security.filter;

import java.io.IOException;
import java.util.Date;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecommerce.shopping.exception.InvalidJWTException;
import com.ecommerce.shopping.exception.JwtExpiredExcepton;
import com.ecommerce.shopping.jwt.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = request.getHeader("Authorization");
		if (token != null) {
			token = token.substring(7);

			try {
				String username = jwtService.extractUserName(token);
				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, null);
					authenticationToken.setDetails(new WebAuthenticationDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
			catch (JwtException ex) {
				throw new InvalidJWTException("invalid jwt token");
			}

				try {
					Date expireDate= jwtService.extractExpirationDate(token);
				} catch (ExpiredJwtException ex) {
					throw new JwtExpiredExcepton("expired!!!");
				}

			}

			filterChain.doFilter(request, response);

		}

	}
