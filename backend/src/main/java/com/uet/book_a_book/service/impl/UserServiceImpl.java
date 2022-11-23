package com.uet.book_a_book.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
import com.uet.book_a_book.entity.Order;
import com.uet.book_a_book.entity.constant.Gender;
import com.uet.book_a_book.entity.constant.OrderStatus;
import com.uet.book_a_book.entity.constant.RoleName;
import com.uet.book_a_book.exception.account.AccountAlreadyActivatedException;
import com.uet.book_a_book.exception.account.AccountNotActivatedException;
import com.uet.book_a_book.exception.account.CannotDeleteAdminAccountException;
import com.uet.book_a_book.exception.account.CannotLockAdminAccountException;
import com.uet.book_a_book.exception.account.CannotResetPasswordException;
import com.uet.book_a_book.exception.account.IncorrectEmailVerificationCodeException;
import com.uet.book_a_book.exception.account.IncorrectOldPasswordException;
import com.uet.book_a_book.exception.account.LockedAccountException;
import com.uet.book_a_book.exception.account.NotFoundAccountException;
import com.uet.book_a_book.exception.account.NotFoundGenderException;
import com.uet.book_a_book.exception.order.CannotDeleteShippingOrderException;
import com.uet.book_a_book.mapper.UserMapper;
import com.uet.book_a_book.repository.OrderRepository;
import com.uet.book_a_book.repository.UserRepository;
import com.uet.book_a_book.service.UserSevice;

@Service
public class UserServiceImpl implements UserSevice {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private EmailSenderService emailSenderService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserMapper userMapper;
	
	/** Get user by id. **/
	@Override
	public UserDTO getUserById(UUID id) {
		AppUser user = userRepository.findById(id).orElse(null);
		if (user == null) {
			throw new NotFoundAccountException("Not found account id: " + id);
		}
		return userMapper.mapToUserDTO(user);
	}

	/** Get all users. **/
	@Override
	public Page<UserDTO> getAllUsers(Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<UserDTO> userDTOs = userRepository.findAll().stream().map(u -> userMapper.mapToUserDTO(u)).collect(Collectors.toList());
		Integer start = (int) pageable.getOffset();
		Integer end = Math.min((start + pageable.getPageSize()), userDTOs.size());
		if (start <= userDTOs.size()) {
			return new PageImpl<>(userDTOs.subList(start, end), pageable, userDTOs.size());
		}
		return new PageImpl<>(new ArrayList<>(), pageable, userDTOs.size());
	}

	/** Get user by name or email. **/
	@Override
	public Page<UserDTO> getUsersByName(String name, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<UserDTO> userDTOs = userRepository.findByName(name.trim()).stream().map(u -> userMapper.mapToUserDTO(u)).collect(Collectors.toList());
		Integer start = (int) pageable.getOffset();
		Integer end = Math.min((start + pageable.getPageSize()), userDTOs.size());
		if (start <= userDTOs.size()) {
			return new PageImpl<>(userDTOs.subList(start, end), pageable, userDTOs.size());
		}
		return new PageImpl<>(new ArrayList<>(), pageable, userDTOs.size());
	}

	/** Save user. **/
	@Override
	public AppUser save(AppUser user) {
		return userRepository.save(user);
	}

	@Override
	public AppUser findByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}
	
	
	/** Account verification. **/
	@Override
	@Transactional
	public void confirmEmailVerification(String email, String code) {
		AppUser user = userRepository.findByEmail(email).orElse(null);
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

	/** Send activation email. **/
	@Override
	@Transactional
	public void sendEmailVerification(String email) {
		AppUser user = userRepository.findByEmail(email).orElse(null);
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

	/** User changes password. **/
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
					"The current password is incorrect, the password cannot be changed");
		}
	}

	/** Get user information. **/
	@Override
	public UserInfo getUserInfo() {
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userMapper.mapToUserInfo(user);
	}

	/** User updates information **/
	@Override
	public UserInfo updateUser(UpdateUser updateUser) {
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		user.setFirstName(updateUser.getFirstName().trim());
		user.setLastName(updateUser.getLastName().trim());
		if (updateUser.getGender().equalsIgnoreCase(Gender.GENDER_MALE)
				|| updateUser.getGender().equalsIgnoreCase(Gender.GENDER_FEMALE)
				|| updateUser.getGender().equalsIgnoreCase(Gender.GENDER_OTHER)) {
			user.setGender(updateUser.getGender().toUpperCase());
		} else {
			throw new NotFoundGenderException("Not found gender: " + updateUser.getGender());
		}
		user.setPhoneNumber(updateUser.getPhoneNumber());
		user.setAddress(updateUser.getAddress().trim());
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

	/** Lock user account **/
	@Override
	public void lockAccount(UUID id) {
		AppUser user = userRepository.findById(id).orElse(null);
		if (user == null) {
			throw new NotFoundAccountException("Not found user id: " + id);
		}
		if (user.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(RoleName.ROLE_ADMIN))) {
			throw new CannotLockAdminAccountException("Cannot lock admin account");
		}
		if (user.isLocked()) {
			return;
		}
		user.setLocked(true);
		userRepository.save(user);
	}

	/** Unlock user account. */
	@Override
	public void unlockAccount(UUID id) {
		AppUser user = userRepository.findById(id).orElse(null);
		if (user == null) {
			throw new NotFoundAccountException("Not found user id: " + id);
		}
		if (user.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(RoleName.ROLE_ADMIN))) {
			return;
		}
		if (!user.isLocked()) {
			return;
		}
		user.setLocked(false);
		userRepository.save(user);
	}

	/** Activate account. **/
	@Override
	public void activeAccount(UUID id) {
		AppUser user = userRepository.findById(id).orElse(null);
		if (user == null) {
			throw new NotFoundAccountException("Not found user id: " + id);
		}
		if (user.isEmailVerified()) {
			throw new AccountAlreadyActivatedException("Account id: " + id + " already activated");
		}
		user.setEmailVerified(true);
		user.setEmailVerificationCode(null);
		userRepository.save(user);
	}

	/** Delete user. **/
	@Override
	public void deleteUser(UUID id) {
		AppUser user = userRepository.findById(id).orElse(null);
		if (user == null) {
			throw new NotFoundAccountException("Not found user id: " + id);
		}
		if (user.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(RoleName.ROLE_ADMIN))) {
			throw new CannotDeleteAdminAccountException("Cannot delete admin account");
		}
		List<Order> orders = orderRepository.findOrdersByUserId(id);
		orders.stream().forEach(o -> {
			// If user has a shipping order, can't delete user.
			if (o.getStatus().equals(OrderStatus.STATUS_SHIPPING)) {
				throw new CannotDeleteShippingOrderException("Can't delete user while shipping");
			}
		});
		orders.stream().forEach(o -> {
			// Don't delete success order.
			if (o.getStatus().equals(OrderStatus.STATUS_SUCCESS)) {
				o.setUser(null);
				orderRepository.save(o);
			}
		});
		userRepository.delete(user);
	}

	/** Reset password. **/
	@Override
	public void updateUserPassword(UUID id, String newPassword) {
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.getId().equals(id)) {
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
			return;
		}
		AppUser appUser = userRepository.findById(id).orElse(null);
		if (appUser == null) {
			throw new NotFoundAccountException("Not found user id: " + id);
		}
		// Cannot reset password of other admin account.
		if (appUser.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(RoleName.ROLE_ADMIN))) {
			throw new CannotResetPasswordException("Cannot reset password of admin account id: " + id);
		}
		appUser.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(appUser);
	}

}
