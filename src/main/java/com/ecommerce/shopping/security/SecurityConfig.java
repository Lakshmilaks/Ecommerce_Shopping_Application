package com.ecommerce.shopping.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ecommerce.shopping.jwt.JwtService;
import com.ecommerce.shopping.repository.RefreshRepo;
import com.ecommerce.shopping.security.filter.JwtAuthFilter;
import com.ecommerce.shopping.security.filter.LoginFilter;
import com.ecommerce.shopping.security.filter.RefreshFilter;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {
	
	private final JwtService jwtService;
	private final RefreshRepo refreshRepo;
	

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}
	
	@Bean
	@Order(3)
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(csrf->csrf.disable())
				.securityMatchers(matcher -> matcher.requestMatchers("/api/v1/**"))
//				.authorizeHttpRequests(authorize-> authorize.requestMatchers("api/v1/sellers/register/**",
//                                "api/v1/customers/register/**")
//						.permitAll()
//						.anyRequest()
//						.authenticated())
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
				.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(new JwtAuthFilter(jwtService),UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	@Order(1)
	SecurityFilterChain loginFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(csrf->csrf.disable())
				.securityMatchers(matcher -> matcher.requestMatchers("/api/v1/login/**"))
				.authorizeHttpRequests(authorize-> authorize.anyRequest().permitAll())
				.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(new LoginFilter(),UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	@Order(2)
	SecurityFilterChain refreshFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(csrf->csrf.disable())
				.securityMatchers(matcher -> matcher.requestMatchers("/api/v1/refresh/**"))
				.authorizeHttpRequests(authorize-> authorize.anyRequest().permitAll())
				.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(new RefreshFilter(jwtService, refreshRepo),UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
	{
		return config.getAuthenticationManager();
	}
	
	
}