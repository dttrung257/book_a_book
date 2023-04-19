package com.uet.book_a_book.services.impl;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.dtos.RegisterRequest;
import com.uet.book_a_book.dtos.user.UpdateUser;
import com.uet.book_a_book.dtos.user.UpdateUserStatus;
import com.uet.book_a_book.dtos.user.UserDTO;
import com.uet.book_a_book.dtos.user.UserInfo;
import com.uet.book_a_book.email.EmailSenderService;
import com.uet.book_a_book.models.AppUser;
import com.uet.book_a_book.models.Order;
import com.uet.book_a_book.models.Role;
import com.uet.book_a_book.models.constant.Const;
import com.uet.book_a_book.models.constant.Gender;
import com.uet.book_a_book.models.constant.OrderStatus;
import com.uet.book_a_book.models.constant.RoleName;
import com.uet.book_a_book.models.constant.UserStatus;
import com.uet.book_a_book.exception.account.AccountAlreadyActivatedException;
import com.uet.book_a_book.exception.account.AccountAlreadyExistsException;
import com.uet.book_a_book.exception.account.AccountNotActivatedException;
import com.uet.book_a_book.exception.account.CannotDeleteAdminAccountException;
import com.uet.book_a_book.exception.account.CannotLockAdminAccountException;
import com.uet.book_a_book.exception.account.CannotResetPasswordException;
import com.uet.book_a_book.exception.account.IncorrectEmailVerificationCodeException;
import com.uet.book_a_book.exception.account.IncorrectOldPasswordException;
import com.uet.book_a_book.exception.account.LockedAccountException;
import com.uet.book_a_book.exception.account.NotFoundAccountException;
import com.uet.book_a_book.exception.account.NotFoundGenderException;
import com.uet.book_a_book.exception.account.NotFoundUserStatusException;
import com.uet.book_a_book.exception.order.CannotDeleteShippingOrderException;
import com.uet.book_a_book.mapper.UserMapper;
import com.uet.book_a_book.repositories.OrderRepository;
import com.uet.book_a_book.repositories.RoleRepository;
import com.uet.book_a_book.repositories.UserRepository;
import com.uet.book_a_book.services.UserSevice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserSevice {
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final OrderRepository orderRepository;
	private final EmailSenderService emailSenderService;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;
	
	/** Get user by id. **/
	@Transactional(readOnly = true)
	@Override
	public UserDTO getUserById(UUID id) {
		Optional<AppUser> user = userRepository.findById(id);
		if (user.isEmpty()) {
			throw new NotFoundAccountException("Not found account id: " + id);
		}
		return userMapper.mapToUserDTO(user.get());
	}

	/** Get user by name or email. **/
	@Transactional(readOnly = true)
	@Override
	public Page<UserDTO> getUsers(String name, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<UserDTO> userDTOs = userRepository.findAll().stream().map(u -> userMapper.mapToUserDTO(u)).collect(Collectors.toList());
		if (!name.equals("") && name != null) {
			userDTOs = userDTOs.stream().filter(uDTO -> ((uDTO.getFirstName() + " " + uDTO.getLastName()).toLowerCase().contains(name.toLowerCase())
					|| uDTO.getEmail().toLowerCase().contains(name.toLowerCase()))).collect(Collectors.toList());
		}
		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), userDTOs.size());
		if (start <= userDTOs.size()) {
			return new PageImpl<>(userDTOs.subList(start, end), pageable, userDTOs.size());
		}
		return new PageImpl<>(new ArrayList<>(), pageable, userDTOs.size());
	}
	
	/** Get user information. **/
	@Override
	public UserInfo getUserInfo() {
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userMapper.mapToUserInfo(user);
	}

	@Override
	public AppUser save(AppUser user) {
		return userRepository.save(user);
	}

	@Override
	public AppUser findByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}
	
	
	/** Update account verification. **/
	@Transactional
	@Override
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
		userRepository.save(user);
	}

	/** Send activation email. **/
	@Transactional
	@Override
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
			userRepository.save(user);
			String builder = emailSenderService.buildEmailVerificationAccount(email,
					user.getFirstName() + " " + user.getLastName(), verificationCode);
			emailSenderService.sendEmail(email, builder);
		}
	}

	/** Update user password. **/
	@Transactional
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
			log.info("User id: {} changed password.", user.getId());
			return;
		} else {
			throw new IncorrectOldPasswordException(
					"The current password is incorrect, the password cannot be changed");
		}
	}

	/** Update user information **/
	@Transactional
	@Override
	public UserInfo updateUser(UpdateUser updateUser) {
		AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		user.setFirstName(updateUser.getFirstName().trim());
		user.setLastName(updateUser.getLastName().trim());
		if (updateUser.getGender().trim().equalsIgnoreCase(Gender.GENDER_MALE)
				|| updateUser.getGender().equalsIgnoreCase(Gender.GENDER_FEMALE)
				|| updateUser.getGender().equalsIgnoreCase(Gender.GENDER_OTHER)) {
			user.setGender(updateUser.getGender().trim().toUpperCase());
		} else {
			throw new NotFoundGenderException("Not found gender: " + updateUser.getGender());
		}
		if (updateUser.getPhoneNumber() != null && updateUser.getPhoneNumber().trim().matches(Const.PHONE_NUMBER_REGEX)) {
			user.setPhoneNumber(updateUser.getPhoneNumber().trim());
		} else {
			user.setPhoneNumber(null);
		}
		if (updateUser.getAddress() != null && updateUser.getAddress().trim() != "") {
			user.setAddress(updateUser.getAddress().trim());
		} else {
			user.setAddress(null);
		}
		if (updateUser.getAvatar() != null && updateUser.getAvatar().trim() != "") {
			user.setAvatar(updateUser.getAvatar().trim());
		} else {
			user.setAvatar(null);
		}
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
		log.info("User id: {} updated information.", user.getId());
		return userInfo;
	}
	
	/** Adm create account. **/
	@Transactional
	@Override
	public UserDTO createAccount(RegisterRequest request) {
		if (userRepository.findByEmail(request.getEmail().trim()).orElse(null) != null) {
			throw new AccountAlreadyExistsException(String.format("User with email %s already exists", request.getEmail()));
		}
		AppUser user = new AppUser();
		user.setEmail(request.getEmail().trim());
		user.setPassword(passwordEncoder.encode(request.getPassword().trim()));
		user.setFirstName(request.getFirstName().trim());
		user.setLastName(request.getLastName().trim());
		user.setCreatedAt(new Date());
		if (request.getGender().trim().equalsIgnoreCase(Gender.GENDER_MALE) 
				|| request.getGender().equalsIgnoreCase(Gender.GENDER_FEMALE)
				|| request.getGender().equalsIgnoreCase(Gender.GENDER_OTHER)) {
			user.setGender(request.getGender().trim().toUpperCase());
		} else {
			throw new NotFoundGenderException("Not found gender: " + request.getGender());
		}
		Role roleUser = roleRepository.findByRoleName(RoleName.ROLE_USER);
		user.setRole(roleUser);
		user.setEmailVerified(true);
		userRepository.save(user);
		log.info("Admin id: {} create account id: {}.",
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(), user.getId());
		return userMapper.mapToUserDTO(user); 
	}

	/** Adm lock user account **/
	@Transactional
	private void lockAccount(UUID id) {
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
		log.info("Admin id: {} lock account id: {}.", 
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(), user.getId());
	}

	/** Adm unlock user account. */
	@Transactional
	private void unlockAccount(UUID id) {
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
		log.info("Admin id: {} unlock account id: {}.", 
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(), user.getId());
	}

	/** Adm activate account. **/
	@Transactional
	private void activeAccount(UUID id) {
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
		log.info("Admin id: {} activate account id: {}.", 
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(), user.getId());
	}
	
	/** Adm update user status. **/
	@Transactional
	@Override
	public String updateUserStatus(UpdateUserStatus updateUserStatus, UUID id) {
		if (updateUserStatus.getStatus().trim().equalsIgnoreCase(UserStatus.STATUS_LOCKED)) {
			if (updateUserStatus.getState()) {
				lockAccount(id);
				return "Lock user id: " + id + " successfully";
			} else {
				unlockAccount(id);
				return "Unlock user id: " + id + " successfully";
			}
		} else if (updateUserStatus.getStatus().trim().equalsIgnoreCase(UserStatus.STATUS_ACTIVATED)) {
			if (updateUserStatus.getState()) {
				activeAccount(id);
				return "Activate user id: " + id + " successfully";
			} else {
				return "System does not support this feature";
			}
		} else {
			throw new NotFoundUserStatusException("Not found user status: " + updateUserStatus.getStatus());
		}
		
	}
	
	/** Adm reset password. **/
	@Transactional
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
		log.info("Admin id: {} reset password of account id: {}.", 
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(), user.getId());
	}

	/** Adm delete user. **/
	@Transactional
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
		orders.forEach(o -> {
			// If user has a shipping order, can't delete user.
			if (o.getStatus().equals(OrderStatus.STATUS_SHIPPING)) {
				throw new CannotDeleteShippingOrderException("Can't delete user while shipping");
			}
		});
		orders.forEach(o -> {
			// Don't delete success order.
			if (o.getStatus().equals(OrderStatus.STATUS_SUCCESS)) {
				o.setUser(null);
				orderRepository.save(o);
			}
		});
		userRepository.delete(user);
	}

}
