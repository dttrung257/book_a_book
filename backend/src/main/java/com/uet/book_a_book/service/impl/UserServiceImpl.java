package com.uet.book_a_book.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.dto.UserDTO;
import com.uet.book_a_book.email.EmailSenderService;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.util.RoleName;
import com.uet.book_a_book.repository.UserRepository;
import com.uet.book_a_book.service.UserSevice;

@Service
public class UserServiceImpl implements UserSevice {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmailSenderService emailSenderService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<UserDTO> findAllUsers() {
		List<UserDTO> users = userRepository.findAllUsers().stream()
				.map(user -> new UserDTO(user.getFirstName(), user.getLastName(), user.getEmail(),
						user.getGender(), user.getPhoneNumber(), user.getAddress(), user.getAvatar(),
						user.getCreatedAt(), user.getUpdatedAt(), user.isLocked(), user.isEmailVerified(),
						user.getAuthorities()))
				.collect(Collectors.toList());
		return users;
	}

	@Override
	public AppUser save(AppUser user) {
		return userRepository.save(user);
	}

	@Override
	public AppUser findByEmail(String email) {
		return userRepository.findByUserEmail(email).orElse(null);
	}

	@Override
	@Transactional
	public AppUser confirmEmailVerification(String email, String code) {
		if (!userRepository.existsByEmail(email)) {
			throw new UsernameNotFoundException("Not found user with email: " + email);
		}
		AppUser user = userRepository.findUserByEmailAndVerificationCode(email, code).orElse(null);
		if (user == null || user.isLocked() || user.isEmailVerified()) {
			return user;
		}
		user.setEmailVerified(true);
		user.setEmailVerificationCode(null);
		return user;
	}

	@Override
	@Transactional
	public AppUser resendEmailVerification(String email) {
		AppUser user = userRepository.findByUserEmail(email).orElse(null);
		if (user == null) {
			throw new UsernameNotFoundException("Not found user with email: " + email);
		}
		if (user.isLocked() || user.isEmailVerified()) {
			return user;
		}
		String verificationCode = emailSenderService.generateVerificationCode();
		if (!verificationCode.equals(user.getEmailVerificationCode())) {
			user.setEmailVerificationCode(verificationCode);
			String builder = emailSenderService.buildEmailVerificationAccount(email,
					user.getFirstName() + " " + user.getLastName(), verificationCode);
			emailSenderService.sendEmail(email, builder);
		}
		return user;
	}

	@Override
	@Transactional
	public boolean changePassword(String email, String oldPassword, String newPassword) {
		AppUser user = userRepository.findByUserEmail(email).orElse(null);
		if (user == null) {
			throw new UsernameNotFoundException("Not found user with email: " + email);
		}
		if (user.isLocked()) {
			throw new IllegalStateException("Account has been locked");
		}
		if (!user.isEmailVerified()) {
			throw new IllegalStateException("Account is not activated");
		}
		if (passwordEncoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(newPassword);
			user.setUpdatedAt(new Date());
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public AppUser lockAccount(String email) {
		AppUser user = userRepository.findByUserEmail(email).orElse(null);
		if (user == null) {
			throw new UsernameNotFoundException("Not found user with email: " + email);
		}
		if (user.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(RoleName.ROLE_ADMIN))) {
			return user;
		}
		if (user.isLocked()) {
			return user;
		}
		user.setLocked(true);
		return user;
	}
	
	@Override
	public AppUser unlockAccount(String email) {
		AppUser user = userRepository.findByUserEmail(email).orElse(null);
		if (user == null) {
			throw new UsernameNotFoundException("Not found user with email: " + email);
		}
		if (user.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(RoleName.ROLE_ADMIN))) {
			return user;
		}
		if (!user.isLocked()) {
			return user;
		}
		user.setLocked(false);
		return user;
	}

	@Override
	public AppUser activeAccount(String email) {
		AppUser user = userRepository.findByUserEmail(email).orElse(null);
		if (user == null) {
			throw new UsernameNotFoundException("Not found user with email: " + email);
		}
		if (user.isEmailVerified()) {
			return user;
		}
		user.setEmailVerified(true);
		return user;
	}

	
	
	
}
