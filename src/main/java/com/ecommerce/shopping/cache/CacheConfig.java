package com.ecommerce.shopping.cache;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ecommerce.shopping.entity.User;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Configuration
public class CacheConfig {

	   @Bean
	    Cache<String, String> otpCache(){
	      return CacheBuilder.newBuilder()
	                .expireAfterWrite(Duration.ofMinutes(5))
	                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
	                .build();
	    }

	    @Bean
	    Cache<String, User> saveUser(){
	        return CacheBuilder.newBuilder()
	                .expireAfterWrite(java.time.Duration.ofMinutes(15))
	                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
	                .build();
	    }
}
