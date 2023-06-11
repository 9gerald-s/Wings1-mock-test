package com.fresco.ecommerce.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fresco.ecommerce.models.User;
import com.fresco.ecommerce.repo.UserRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {
	@Value("${jwt.secret}")
	private String secret;
	@Value("${jwt.token.validity}")
	private long expiration;
	
	@Autowired
	UserRepo userRepo;

	public User getUser(final String token) {
		String username = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
		return userRepo.findByUsername(username).get();
	}

	public String generateToken(String username) {
		Claims claims = Jwts.claims().setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration));
		return Jwts.builder().setClaims(claims).setSubject(username).signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}

	public void validateToken(final String token) {
		Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
	}
}
