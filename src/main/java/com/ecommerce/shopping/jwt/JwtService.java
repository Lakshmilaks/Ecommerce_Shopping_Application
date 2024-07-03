package com.ecommerce.shopping.jwt;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ecommerce.shopping.enums.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${application.jwt.secrete}")
	private String secret;

	private static final String ROLE="role";

	public String createJwtToken(String username,UserRole role, long expirationTimeInMillies){
		return Jwts.builder()
				.setClaims(Map.of(ROLE,"role"))
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

	public UserRole extractUserRole(String token)
	{
		String role= parseJwt(token).get("role", String.class);
		return UserRole.valueOf(role);
	}




}
