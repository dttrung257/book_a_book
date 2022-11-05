package com.uet.book_a_book.security.jwt;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@Getter
@Slf4j
//@ConfigurationPropertiesScan(basePackageClasses = MyServerProperties.class)
public class JwtUtil {
	@Value("${my.server.secretKey}")
	private String secretKey;
	
	private static final long jwtExpiredTimeMs = 24 * 60 * 60 * 1000;
	
	public String generateJwtToken(String email) {
		final SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
		return Jwts.builder()
					.setId(UUID.randomUUID().toString())
					.setSubject(email)
					.setIssuer("com.uet.book_a_book.trungdt.2022")
					.setIssuedAt(new Date())
					.setExpiration(new Date(System.currentTimeMillis() + jwtExpiredTimeMs))
					.signWith(key)
					.compact();
	}
	
	public boolean verifyJwtToken(String token) {
		final SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SignatureException e) {
			log.error("The jwt token is invalid");
		} catch (MalformedJwtException e) {
			log.error("The jwt token is malformed");
		} catch (ExpiredJwtException e) {
			log.error("The jwt token has expired");
		} catch (IllegalArgumentException e) {
			log.error("The jwt token is invalid");
		} catch (UnsupportedJwtException e) {
			log.error("The jwt token is not supported");
		}
		return false;
	}
	
	private Claims getClaims(String token) {
		final SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}
	
	public String getEmailFromToken(String token) {
		return getClaims(token).getSubject();
	}
	
	public boolean isNonExpired(String token) {
		return getClaims(token).getExpiration().after(new Date());
	}
}
