package com.project.movie_reservation_system.service;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.project.movie_reservation_system.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

	private final SecretKey secretKey;

	public JwtService()
    {
        KeyGenerator kg = getKeyGenerator();
        secretKey = kg.generateKey();
    }

	public KeyGenerator getKeyGenerator() {
		try {
			KeyGenerator kg = KeyGenerator.getInstance("HmacSHA256");
			kg.init(256);
			return kg;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public String generateJwtToken(User user) {
		return Jwts.builder().subject(user.getUsername()).claims(Map.of("ROLES", user.getAuthorities()))
				.issuedAt(new Date()).expiration(getExpiryDateForJwt()).signWith(secretKey).compact();
	}

	public Date getExpiryDateForJwt() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, 15);
		return calendar.getTime();
	}

	public Claims extractAllClaims(String jwt) {
		return (Claims) Jwts.parser().verifyWith(secretKey).build().parse(jwt).getPayload();
	}

	public String extractUserNameFromJwt(String jwt) {
		return extractAllClaims(jwt).getSubject();
	}

}
