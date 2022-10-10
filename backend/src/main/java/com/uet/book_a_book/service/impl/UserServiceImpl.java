package com.uet.book_a_book.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.domain.AppUser;
import com.uet.book_a_book.domain.Status;
import com.uet.book_a_book.dto.UserDTO;
import com.uet.book_a_book.email.EmailSenderService;
import com.uet.book_a_book.exception.StatusNotFoundException;
import com.uet.book_a_book.repository.UserRepository;
import com.uet.book_a_book.service.UserSevice;

@Service
public class UserServiceImpl implements UserSevice {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmailSenderService emailSenderService;

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
	public AppUser changeUserStatus(String email, String statusName, boolean status) {
		AppUser user = userRepository.findByUserEmail(email).orElse(null);
		if (user == null) {
			throw new UsernameNotFoundException("Not found user with email: " + email);
		}
		if (statusName.equalsIgnoreCase(Status.STATUS_USER_LOCKED)) {
			user.setLocked(status);
			user.setUpdatedAt(new Date());
		} else if (statusName.equalsIgnoreCase(Status.STATUS_USER_EMAIL_VERIFIED)) {
			user.setEmailVerified(status);
			user.setUpdatedAt(new Date());
		} else {
			throw new StatusNotFoundException("User status: " + statusName + " not found");
		}
		return user;
	}

	@Override
	@Transactional
	public String confirmEmailVerification(String email, String code) {
		if (!userRepository.existsByEmail(email)) {
			throw new UsernameNotFoundException("Not found user with email: " + email);
		}
		AppUser user = userRepository.findUserByEmailAndVerificationCode(email, code).orElse(null);
		if (user == null) {
			return "WRONG_CODE";
		}
		if (user.isLocked()) {
			return "LOCKED_ACCOUNT";
		}
		if (user.isEmailVerified()) {
			return "VERIFIED";
		}
		user.setEmailVerified(true);
		return "SUCCESS";
	}

	@Override
	@Transactional
	public String resendEmailVerification(String email) {
		AppUser user = userRepository.findByUserEmail(email).orElse(null);
		if (user == null) {
			throw new UsernameNotFoundException("Not found user with email: " + email);
		}
		if (user.isLocked()) {
			return "LOCKED_ACCOUNT";
		}
		if (user.isEmailVerified()) {
			return "VERIFIED";
		}
		String verificationCode = emailSenderService.generateVerificationCode();
		if (!verificationCode.equals(user.getEmailVerificationCode())) {
			user.setEmailVerificationCode(verificationCode);
			String builder = emailSenderService.buildEmail(email,
					user.getFirstName() + " " + user.getLastName(), verificationCode);
			emailSenderService.sendEmail(email, builder);
		}
		return "SUCCESS";
	}
	
	
}
