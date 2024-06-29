package com.ecommerce.shopping.jwt;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
@Service
public class JwtService {

	private String secret="6aQXwXHJPvuXkJfx1Ok9zJpOPToGcj+MideCGNe8nOsNefsOYu8uliPBEAZ9L++TK5e7/5Hf4XR8\r\nrn9GvRW4XA==";

	public String createJwtToken(String username,long expirationTimeInMillies){
		return Jwts.builder().setClaims(Map.of())
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+expirationTimeInMillies))
				.signWith(getSignatureKey(), SignatureAlgorithm.HS512)
				.compact();
	}

	private Key getSignatureKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}

	private Claims parseJwt(String token){
		return Jwts.parserBuilder()
				.setSigningKey(getSignatureKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	public String extractUserName(String token) {
		return	parseJwt(token).getSubject();
	}

	public Date extractExpirationDate(String token) {
		return parseJwt(token).getExpiration();
	}
	
	public Date extractIssuedDate(String token) {
		return parseJwt(token).getIssuedAt();
	}

}
