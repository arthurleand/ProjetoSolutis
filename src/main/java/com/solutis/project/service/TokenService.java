package com.solutis.project.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.solutis.project.model.UserModel;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	
	@Value("${forum.jwt.expiration}")
	private String expiration;
	@Value("${forum.jwt.secret}")
	private String secret;
	
	public String generateToken(Authentication authentication) {
		UserModel user = (UserModel) authentication.getPrincipal();
		Date nowDate = new Date();
		Date dateExpiration = new Date(nowDate.getTime()+ Long.parseLong(expiration));
		return Jwts.builder()
				.setIssuer("API Solutis")
				.setSubject(user.getId().toString())
				.setIssuedAt(nowDate)
				.setExpiration(dateExpiration)
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}

	public boolean isValidToken(String token) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}	
	}

	public Long getIdUser(String token) {
		Claims clain = Jwts.parser()
				.setSigningKey(this.secret)
				.parseClaimsJws(token)
				.getBody();
		return Long.parseLong(clain.getSubject());	
	}
}
