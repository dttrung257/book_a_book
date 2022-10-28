package com.uet.book_a_book.entity.util;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class ResetPasswordUtil {
	private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
	
	public String generateResetToken() {
		SecureRandom secureRandom = new SecureRandom();
		byte[] bytes = new byte[24];
		secureRandom.nextBytes(bytes);
		return base64Encoder.encodeToString(bytes);
	}
	
	public String generateVerificationCode() {
		Random random = new Random();
		return String.valueOf(1000000 + random.nextInt(9000000));
	}
	
	public static void main(String[] args) {
		System.out.println((new ResetPasswordUtil()).generateResetToken());
	}
}
