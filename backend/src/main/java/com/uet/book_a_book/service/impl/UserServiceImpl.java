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

import com.uet.book_a_book.dto.RegisterRequest;
import com.uet.book_a_book.dto.user.UpdateUser;
import com.uet.book_a_book.dto.user.UpdateUserStatus;
import com.uet.book_a_book.dto.user.UserDTO;
import com.uet.book_a_book.dto.user.UserInfo;
import com.uet.book_a_book.email.EmailSenderService;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.Order;
import com.uet.book_a_book.entity.Role;
import com.uet.book_a_book.entity.constant.Const;
import com.uet.book_a_book.entity.constant.Gender;
import com.uet.book_a_book.entity.constant.OrderStatus;
import com.uet.book_a_book.entity.constant.RoleName;
import com.uet.book_a_book.entity.constant.UserStatus;
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
import com.uet.book_a_book.repository.OrderRepository;
import com.uet.book_a_book.repository.RoleRepository;
import com.uet.book_a_book.repository.UserRepository;
import com.uet.book_a_book.service.UserSevice;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserSevice {
	@Autowired
	private RoleRepository roleRepository;
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

	/** Get user by name or email. **/
	@Override
	public Page<UserDTO> getUsers(String name, Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size);
		List<UserDTO> userDTOs = userRepository.findAll().stream().map(u -> userMapper.mapToUserDTO(u)).collect(Collectors.toList());
		if (!name.equals("") && name != null) {
			userDTOs = userDTOs.stream().filter(uDTO -> ((uDTO.getFirstName() + " " + uDTO.getLastName()).toLowerCase().contains(name.toLowerCase())
					|| uDTO.getEmail().toLowerCase().contains(name.toLowerCase()))).collect(Collectors.toList());
		}
		Integer start = (int) pageable.getOffset();
		Integer end = Math.min((start + pageable.getPageSize()), userDTOs.size());
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

	/** Update user password. **/
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
	@Override
	public String updateUserStatus(UpdateUserStatus updateUserStatus, UUID id) {
		if (updateUserStatus.getStatus().trim().equalsIgnoreCase(UserStatus.STATUS_LOCKED)) {
			if (updateUserStatus.getState() == true) {
				lockAccount(id);
				return "Lock user id: " + id + " successfully";
			} else {
				unlockAccount(id);
				return "Unlock user id: " + id + " successfully";
			}
		} else if (updateUserStatus.getStatus().trim().equalsIgnoreCase(UserStatus.STATUS_ACTIVATED)) {
			if (updateUserStatus.getState() == true) {
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

}
