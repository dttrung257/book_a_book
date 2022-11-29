package com.uet.book_a_book.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.email.EmailSenderService;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.ResetPasswordToken;
import com.uet.book_a_book.entity.constant.ResetPasswordUtil;
import com.uet.book_a_book.exception.account.AccountNotActivatedException;
import com.uet.book_a_book.exception.account.IncorrectResetPasswordCodeException;
import com.uet.book_a_book.exception.account.LockedAccountException;
import com.uet.book_a_book.exception.account.NotFoundAccountException;
import com.uet.book_a_book.exception.account.NotFoundResetPasswordTokenException;
import com.uet.book_a_book.repository.ResetPasswordTokenRepository;
import com.uet.book_a_book.repository.UserRepository;
import com.uet.book_a_book.service.ResetPasswordTokenService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResetPasswordTokenServiceImpl implements ResetPasswordTokenService {
	@Autowired
	private ResetPasswordTokenRepository resetPasswordTokenRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ResetPasswordUtil resetPasswordUtil;
	@Autowired
	private EmailSenderService emailSenderService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void save(ResetPasswordToken token) {
		resetPasswordTokenRepository.save(token);
	}

	/** Send code to reset password. **/
	@Override
	@Transactional
	public void forgotPassword(String email) {
		AppUser user = userRepository.findByEmail(email).orElse(null);
		if (user == null) {
			throw new NotFoundAccountException("Not found user with email: " + email);
		}
		if (user.isLocked()) {
			throw new LockedAccountException(String.format("Account with email: %s has been locked", email));
		}
		if (!user.isEmailVerified()) {
			throw new AccountNotActivatedException(String.format("Account with email: %s not activated", email));
		}
		ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findByEmail(email).orElse(null);
		if (resetPasswordToken == null) {
			ResetPasswordToken token = new ResetPasswordToken(null, resetPasswordUtil.generateResetToken(),
					resetPasswordUtil.generateVerificationCode(), user);

			user.setResetPasswordToken(token);
			resetPasswordTokenRepository.save(token);

			String emailBody = emailSenderService.buildEmailForgotPassword(
					user.getFirstName() + " " + user.getLastName(), token.getVerificationCode());
			emailSenderService.sendEmail(email, emailBody);

		} else {
			resetPasswordToken.setResetToken(resetPasswordUtil.generateResetToken());
			resetPasswordToken.setVerificationCode(resetPasswordUtil.generateVerificationCode());
			resetPasswordTokenRepository.save(resetPasswordToken);
			String emailBody = emailSenderService.buildEmailForgotPassword(
					user.getFirstName() + " " + user.getLastName(), resetPasswordToken.getVerificationCode());
			emailSenderService.sendEmail(email, emailBody);
		}
		log.info("User with email: {} send email forgot password.", email);
	}

	/** Get reset password token. **/
	@Override
	public String getResetPasswordToken(String email, String code) {
		AppUser user = userRepository.findByEmail(email).orElse(null);
		if (user == null) {
			throw new NotFoundAccountException("Not found user with email: " + email);
		}
		if (user.isLocked()) {
			throw new LockedAccountException(String.format("Account with email: %s has been locked", email));
		}
		if (!user.isEmailVerified()) {
			throw new AccountNotActivatedException(String.format("Account with email: %s not activated", email));
		}
		ResetPasswordToken token = resetPasswordTokenRepository.findByEmail(email).orElse(null);
		if (token == null) {
			throw new NotFoundResetPasswordTokenException(
					"Reset password token of account with email: " + email + " does not exists");
		}
		if (!token.getVerificationCode().equals(code)) {
			throw new IncorrectResetPasswordCodeException(
					"Incorrect reset password verification code of account with email: " + email);
		}
		log.info("User with email: {} get reset password token.", email);
		return token.getResetToken();
	}

	/** User reset password. **/
	@Override
	public void resetPassword(String email, String resetToken, String newPassword) {
		AppUser user = userRepository.findByEmail(email).orElse(null);
		if (user == null) {
			throw new NotFoundAccountException("Not found user with email: " + email);
		}
		if (user.isLocked()) {
			throw new LockedAccountException(String.format("Account with email: %s has been locked", email));
		}
		if (!user.isEmailVerified()) {
			throw new AccountNotActivatedException(String.format("Account with email: %s not activated", email));
		}
		ResetPasswordToken token = resetPasswordTokenRepository.findByEmail(email).orElse(null);
		if (token == null) {
			throw new NotFoundResetPasswordTokenException(
					"Reset password token of account with email: " + email + " does not exists");
		}
		if (!token.getResetToken().equals(resetToken)) {
			throw new IncorrectResetPasswordCodeException(
					"Incorrect reset password token of account with email: " + email);
		}
		if (token.getResetToken().equals(resetToken) && !passwordEncoder.matches(newPassword, user.getPassword())) {
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
			token.setResetToken(resetPasswordUtil.generateResetToken());
			token.setVerificationCode(resetPasswordUtil.generateVerificationCode());
			resetPasswordTokenRepository.save(token);
		}
		log.info("User with email: {} reset password successfully.", email);
	}
}
