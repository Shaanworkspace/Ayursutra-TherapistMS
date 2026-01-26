package com.therapistms.JWT;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
	@Value("${jwt.secretkey}")
	private String jwtSecretKey;

	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
	}

	public Claims getClaims(String token) {
		return Jwts.parser()
				.verifyWith(getSecretKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
	public String generateServiceToServiceToken(String serviceName) {
		return Jwts.builder()
				.subject(serviceName)
				.claim("type", "SERVICE")
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis()+1000*60*60*48)) //2 days
				.signWith(getSecretKey())
				.compact();
	}

	public boolean isServiceToken(Claims claims) {
		return "SERVICE".equals(claims.get("type"));
	}
}
