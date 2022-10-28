package com.uet.book_a_book.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.email.EmailSenderService;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.ResetPasswordToken;
import com.uet.book_a_book.entity.util.ResetPasswordUtil;
import com.uet.book_a_book.exception.AccountNotActivatedException;
import com.uet.book_a_book.exception.LockedAccountException;
import com.uet.book_a_book.repository.ResetPasswordTokenRepository;
import com.uet.book_a_book.repository.UserRepository;
import com.uet.book_a_book.service.ResetPasswordTokenService;

@Service
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

	@Override
	@Transactional
	public void forgotPassword(String email) {
		AppUser user = userRepository.findByUserEmail(email).orElse(null);
		if (user == null) {
			throw new UsernameNotFoundException("Not found user with email: " + email);
		}
		if (user.isLocked()) {
			throw new LockedAccountException(String.format("Account with email: %s has been locked", email));
		}
		if (!user.isEmailVerified()) {
			throw new AccountNotActivatedException(String.format("Account with email: %s not activated", email));
		}
		ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findByUserEmail(email).orElse(null);
		if (resetPasswordToken == null) {
			ResetPasswordToken token = new ResetPasswordToken(null, resetPasswordUtil.generateResetToken(),
					resetPasswordUtil.generateVerificationCode(), user);

			user.setResetPasswordToken(token);
			resetPasswordTokenRepository.save(token);

			String emailBody = emailSenderService.buildEmailForgotPassword(user.getFirstName() + " " + user.getLastName(),
					token.getVerificationCode());
			emailSenderService.sendEmail(email, emailBody);
		} else {
			resetPasswordToken.setResetToken(resetPasswordUtil.generateResetToken());
			resetPasswordToken.setVerificationCode(resetPasswordUtil.generateVerificationCode());
			resetPasswordTokenRepository.save(resetPasswordToken);
			String emailBody = emailSenderService.buildEmailForgotPassword(user.getFirstName() + " " + user.getLastName(),
					resetPasswordToken.getVerificationCode());
			emailSenderService.sendEmail(email, emailBody);
		}
	}

	@Override
	public ResetPasswordToken getResetPasswordToken(String email, String code) {
		AppUser user = userRepository.findByUserEmail(email).orElse(null);
		if (user == null) {
			throw new UsernameNotFoundException("Not found user with email: " + email);
		}
		if (user.isLocked()) {
			throw new LockedAccountException(String.format("Account with email: %s has been locked", email));
		}
		if (!user.isEmailVerified()) {
			throw new AccountNotActivatedException(String.format("Account with email: %s not activated", email));
		}
		ResetPasswordToken token = resetPasswordTokenRepository.findByUserEmail(email).orElse(null);
		return token;
	}

	@Override
	@Transactional
	public ResetPasswordToken resetPassword(String email, String resetToken, String newPassword) {
		AppUser user = userRepository.findByUserEmail(email).orElse(null);
		if (user == null) {
			throw new UsernameNotFoundException("Not found user with email: " + email);
		}
		if (user.isLocked()) {
			throw new LockedAccountException(String.format("Account with email: %s has been locked", email));
		}
		if (!user.isEmailVerified()) {
			throw new AccountNotActivatedException(String.format("Account with email: %s not activated", email));
		}
		ResetPasswordToken token = resetPasswordTokenRepository.findByUserEmail(email).orElse(null);
		if (token.getResetToken().equals(resetToken) && !passwordEncoder.matches(newPassword, user.getPassword())) {
			user.setPassword(passwordEncoder.encode(newPassword));
		}
		return token;
	}
}
