package com.uet.book_a_book.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.dto.user.UpdateUser;
import com.uet.book_a_book.dto.user.UserDTO;
import com.uet.book_a_book.dto.user.UserInfo;
import com.uet.book_a_book.email.EmailSenderService;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.util.Gender;
import com.uet.book_a_book.entity.util.RoleName;
import com.uet.book_a_book.exception.account.AccountAlreadyActivatedException;
import com.uet.book_a_book.exception.account.AccountNotActivatedException;
import com.uet.book_a_book.exception.account.CannotDeleteAdminAccountException;
import com.uet.book_a_book.exception.account.CannotResetPasswordException;
import com.uet.book_a_book.exception.account.IncorrectEmailVerificationCodeException;
import com.uet.book_a_book.exception.account.IncorrectOldPasswordException;
import com.uet.book_a_book.exception.account.LockedAccountException;
import com.uet.book_a_book.exception.account.NotFoundAccountException;
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

	private List<UserDTO> mapUserToUserDTO(List<AppUser> users) {
		List<UserDTO> userDTOs = users.stream()
				.map(user -> new UserDTO(user.getFirstName(), user.getLastName(), user.getEmail(), user.getGender(),
						user.getPhoneNumber(), user.getAddress(), user.getAvatar(), user.getCreatedAt(),
						user.getUpdatedAt(), user.isLocked(), user.isEmailVerified(), 
						user.getAuthorities().stream().map(auth -> auth.getAuthority())
											.collect(Collectors.toList()).get(0)))
				.collect(Collectors.toList());
		return userDTOs;
	}

	@Override
	public Page<UserDTO> fetchAllUsers(Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<UserDTO> userDTOs = mapUserToUserDTO(userRepository.fetchAllUsers());
		Integer start = (int) pageable.getOffset();
		Integer end = Math.min((start + pageable.getPageSize()), userDTOs.size());
		if (start <= userDTOs.size()) {
			return new PageImpl<>(userDTOs.subList(start, end), pageable, userDTOs.size());
		}
		return new PageImpl<>(new ArrayList<>(), pageable, userDTOs.size());
	}

	@Override
	public Page<UserDTO> fetchByEmail(String email, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<UserDTO> userDTOs = mapUserToUserDTO(userRepository.fetchByEmail(email));
		Integer start = (int) pageable.getOffset();
		Integer end = Math.min((start + pageable.getPageSize()), userDTOs.size());
		if (start <= userDTOs.size()) {
			return new PageImpl<>(userDTOs.subList(start, end), pageable, userDTOs.size());
		}
		return new PageImpl<>(new ArrayList<>(), pageable, userDTOs.size());
	}

	@Override
	public Page<UserDTO> fetchByName(String name, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<UserDTO> userDTOs = mapUserToUserDTO(userRepository.fetchByName(name));
		Integer start = (int) pageable.getOffset();
		Integer end = Math.min((start + pageable.getPageSize()), userDTOs.size());
		if (start <= userDTOs.size()) {
			return new PageImpl<>(userDTOs.subList(start, end), pageable, userDTOs.size());
		}
		return new PageImpl<>(new ArrayList<>(), pageable, userDTOs.size());
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
	public void confirmEmailVerification(String email, String code) {
		AppUser user = userRepository.findByUserEmail(email).orElse(null);
		if (user == null) {
			throw new NotFoundAccountException(String.format("Not found user with email: %s", email));
		}
		if (user.isLocked()) {
			throw new LockedAccountException(String.format("Account with email: %s has been locked", email));
		}
		if (user.isEmailVerified()) {
			throw new AccountAlreadyActivatedException(
					String.format("Account with email: %s already activated", email));
		}
		if (!user.getEmailVerificationCode().equals(code)) {
			throw new IncorrectEmailVerificationCodeException(
					String.format("Incorrect code to activate account with email: %s", email));
		}
		user.setEmailVerified(true);
		user.setEmailVerificationCode(null);
	}

	@Override
	@Transactional
	public void sendEmailVerification(String email) {
		AppUser user = userRepository.findByUserEmail(email).orElse(null);
		if (user == null) {
			throw new NotFoundAccountException("Not found user with email: " + email);
		}
		if (user.isLocked()) {
			throw new LockedAccountException(String.format("Account with email: %s has been locked", email));
		}
		if (user.isEmailVerified()) {
			throw new AccountAlreadyActivatedException(
					String.format("Account with email: %s already activated", email));
		}
		String verificationCode = emailSenderService.generateVerificationCode();
		if (!verificationCode.equals(user.getEmailVerificationCode())) {
			user.setEmailVerificationCode(verificationCode);
			String builder = emailSenderService.buildEmailVerificationAccount(email,
					user.getFirstName() + " " + user.getLastName(), verificationCode);
			emailSenderService.sendEmail(email, builder);
		}
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.isLocked()) {
			throw new LockedAccountException(String.format("Account with email: %s has been locked", user.getEmail()));
		}
		if (!user.isEmailVerified()) {
			throw new AccountNotActivatedException(
					String.format("Account with email: %s not activated", user.getEmail()));
		}
		if (passwordEncoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(passwordEncoder.encode(newPassword));
			user.setUpdatedAt(new Date());
			userRepository.save(user);
			return;
		} else {
			throw new IncorrectOldPasswordException(
					"The current password is incorrect, the password cannot be reversed");
		}
	}

	@Override
	public UserInfo viewInformation() {
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserInfo userInfo = new UserInfo();
		userInfo.setFirstName(user.getFirstName());
		userInfo.setLastName(user.getLastName());
		userInfo.setEmail(user.getEmail());
		userInfo.setGender(user.getGender());
		userInfo.setPhoneNumber(user.getPhoneNumber());
		userInfo.setAddress(user.getAddress());
		userInfo.setAvatar(user.getAvatar());
		return userInfo;
	}

	@Override
	public UserInfo updateUser(UpdateUser updateUser) {
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		user.setFirstName(updateUser.getFirstName());
		user.setLastName(updateUser.getLastName());
		if (updateUser.getGender().equalsIgnoreCase(Gender.GENDER_MALE)
				|| updateUser.getGender().equalsIgnoreCase(Gender.GENDER_FEMALE)) {
			user.setGender(updateUser.getGender().toUpperCase());
		}
		user.setPhoneNumber(updateUser.getPhoneNumber());
		user.setAddress(updateUser.getAddress());
		user.setAvatar(updateUser.getAvatar());
		user.setUpdatedAt(new Date());
		userRepository.save(user);
		UserInfo userInfo = new UserInfo();
		userInfo.setFirstName(user.getFirstName());
		userInfo.setLastName(user.getLastName());
		userInfo.setEmail(user.getEmail());
		userInfo.setGender(user.getGender());
		userInfo.setPhoneNumber(user.getPhoneNumber());
		userInfo.setAddress(user.getAddress());
		userInfo.setAvatar(user.getAvatar());
		return userInfo;
	}

	@Override
	@Transactional
	public AppUser lockAccount(String email) {
		AppUser user = userRepository.findByUserEmail(email).orElse(null);
		if (user == null) {
			throw new NotFoundAccountException("Not found user with email: " + email);
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
	@Transactional
	public AppUser unlockAccount(String email) {
		AppUser user = userRepository.findByUserEmail(email).orElse(null);
		if (user == null) {
			throw new NotFoundAccountException("Not found user with email: " + email);
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
	@Transactional
	public void activeAccount(String email) {
		AppUser user = userRepository.findByUserEmail(email).orElse(null);
		if (user == null) {
			throw new NotFoundAccountException("Not found user with email: " + email);
		}
		if (user.isEmailVerified()) {
			throw new AccountAlreadyActivatedException(
					String.format("Account with email: %s already activated", email));
		}
		user.setEmailVerified(true);
		user.setEmailVerificationCode(null);
	}

	@Override
	public void deleteUser(String email) {
		AppUser user = userRepository.findByUserEmail(email).orElse(null);
		if (user == null) {
			throw new NotFoundAccountException("Not found user with email: " + email);
		}
		if (user.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(RoleName.ROLE_ADMIN))) {
			throw new CannotDeleteAdminAccountException("Cannot delete admin account");
		}
		userRepository.delete(user);
	}

	@Override
	public void resetUserPassword(String email, String newPassword) {
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.getEmail().equals(email)) {
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
			return;
		}
		AppUser appUser = userRepository.findByUserEmail(email).orElse(null);
		if (appUser == null) {
			throw new NotFoundAccountException("Not found user with email: " + email);
		}
		if (appUser.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(RoleName.ROLE_ADMIN))) {
			throw new CannotResetPasswordException("Cannot reset password of admin account with email: " + email);
		}
		appUser.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(appUser);
	}

}
