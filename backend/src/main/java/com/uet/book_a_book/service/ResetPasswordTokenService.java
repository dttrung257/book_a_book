package com.uet.book_a_book.service;

import com.uet.book_a_book.entity.ResetPasswordToken;

public interface ResetPasswordTokenService {
	void save(ResetPasswordToken token);

	void forgotPassword(String email);

	String getResetPasswordToken(String email, String code);

	void resetPassword(String email, String resetToken, String newPassword);
}
