package com.uet.book_a_book.controller;

import java.util.Date;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uet.book_a_book.dto.RegisterRequest;
import com.uet.book_a_book.dto.user.AdmResetPassword;
import com.uet.book_a_book.dto.user.NewPassword;
import com.uet.book_a_book.dto.user.ResetPassword;
import com.uet.book_a_book.dto.user.UpdateUser;
import com.uet.book_a_book.dto.user.UpdateUserStatus;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.ResetPasswordToken;
import com.uet.book_a_book.entity.Role;
import com.uet.book_a_book.entity.constant.ResetPasswordUtil;
import com.uet.book_a_book.entity.constant.RoleName;
import com.uet.book_a_book.entity.constant.UserStatus;
import com.uet.book_a_book.exception.account.IncorrectResetPasswordCodeException;
import com.uet.book_a_book.exception.account.NotFoundResetPasswordTokenException;
import com.uet.book_a_book.exception.account.NotFoundUserStatusException;
import com.uet.book_a_book.service.ResetPasswordTokenService;
import com.uet.book_a_book.service.RoleService;
import com.uet.book_a_book.service.UserSevice;

@RestController
@RequestMapping("/api")
@Validated
public class UserController {
	@Autowired
	private UserSevice userSevice;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleService roleService;
	@Autowired
	private ResetPasswordTokenService resetPasswordTokenService;
	@Autowired
	private ResetPasswordUtil resetPasswordUtil;

	@GetMapping("/users/forgot_password/{email}")
	public ResponseEntity<Object> forgotPassword(
			@PathVariable(name = "email") @Email(message = "Email field is not valid") String email) {
		resetPasswordTokenService.forgotPassword(email);
		return ResponseEntity.ok("Email sent successlly");
	}

	@GetMapping("/users/forgot_password/{email}/confirm_verification/{code}")
	public ResponseEntity<Object> confirmResetPassword(
			@PathVariable("email") @Email(message = "Email field is not valid") String email,
			@PathVariable("code") @NotBlank(message = "Varification code field cannot be blank") String code) {
		ResetPasswordToken token = resetPasswordTokenService.getResetPasswordToken(email, code);
		if (token == null) {
			throw new NotFoundResetPasswordTokenException("Reset password token of account with email: " + email + " does not exists");
		}
		if (!token.getVerificationCode().equals(code)) {
			throw new IncorrectResetPasswordCodeException("Incorrect reset password verification code of account with email: " + email);
		}
		return ResponseEntity.ok(token.getResetToken());
	}

	@PutMapping("/users/forgot_password/reset_password")
	public ResponseEntity<Object> resetPassword(@RequestBody ResetPassword resetPassword) {
		ResetPasswordToken token = resetPasswordTokenService.resetPassword(resetPassword.getEmail(),
				resetPassword.getResetToken(), resetPassword.getNewPassword());
		if (token == null) {
			throw new NotFoundResetPasswordTokenException("Reset password token of account with email: " + resetPassword.getEmail() + " does not exists");
		}
		if (!token.getResetToken().equals(resetPassword.getResetToken())) {
			throw new IncorrectResetPasswordCodeException("Incorrect reset password token of account with email: " + resetPassword.getEmail());
		}
		token.setResetToken(resetPasswordUtil.generateResetToken());
		token.setVerificationCode(resetPasswordUtil.generateVerificationCode());
		resetPasswordTokenService.save(token);
		return ResponseEntity.ok("Reset password successfully");
	}

	@PutMapping("/users/change_password")
	public ResponseEntity<Object> changePassword(@Valid @RequestBody NewPassword request) {
		userSevice.changePassword(request.getOldPassword(), request.getNewPassword());
		return ResponseEntity.ok("Change password successfully");
	}
	
	@GetMapping("/users")
	public ResponseEntity<Object> getUserInfo() {
		return ResponseEntity.ok(userSevice.getUserInfo());
	}
	
	@PutMapping("/users")
	public ResponseEntity<Object> updateUser(@Valid @RequestBody UpdateUser updateUser) {
		return ResponseEntity.ok(userSevice.updateUser(updateUser));
	}

	@PostMapping("/manage/users")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> createAccount(@Valid @RequestBody RegisterRequest request) {
		if (userSevice.findByEmail(request.getEmail()) != null) {
			throw new BadCredentialsException(String.format("User with email %s already exists", request.getEmail()));
		}
		AppUser user = new AppUser();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setCreatedAt(new Date());
		Role roleUser = roleService.findByRoleName(RoleName.ROLE_USER);
		user.setRole(roleUser);
		user.setEmailVerified(true);

		userSevice.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body("Create account successfully");
	}

	@GetMapping("manage/users")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> getAllUsers(
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1) Integer size) {
		return ResponseEntity.ok(userSevice.getAllUsers(page, size));
	}
	
	@GetMapping("manage/users/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> getUserById(
			@PathVariable(name = "id", required = true) 
			@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", 
			message = "id field must in UUID format") String id) {
		return ResponseEntity.ok(userSevice.getUserById(UUID.fromString(id)));
	}
	
	@GetMapping("manage/users/name")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> getUsersByName(
			@RequestParam(name = "name", required = true) @NotBlank(message = "Name field cannot be blank") String name,
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1) Integer size) {
		return ResponseEntity.ok(userSevice.getUsersByName(name, page, size));
	}

	@PutMapping("/manage/users/{id}/status")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> updateStatus(
			@PathVariable(name = "id", required = true) 
			@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", 
			message = "id field must in UUID format") String id,
			@Valid @RequestBody UpdateUserStatus updateUserStatus) {
			if (updateUserStatus.getStatus().equalsIgnoreCase(UserStatus.STATUS_LOCKED)) {
				if (updateUserStatus.getState() == true) {
					userSevice.lockAccount(UUID.fromString(id));
					return ResponseEntity.ok("Lock user id: " + id + " successfully");
				} else {
					userSevice.unlockAccount(UUID.fromString(id));
					return ResponseEntity.ok("Unlock user id: " + id + " successfully");
				}
			} else if (updateUserStatus.getStatus().equalsIgnoreCase(UserStatus.STATUS_ACTIVATED)) {
				if (updateUserStatus.getState() == true) {
					userSevice.activeAccount(UUID.fromString(id));
					return ResponseEntity.ok("Activate user id: " + id + " successfully");
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The system does not support this feature");
				}
			} else {
				throw new NotFoundUserStatusException("Not found user status: " + updateUserStatus.getStatus());
			}
	}
	
	@PutMapping("/manage/users/{id}/password")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> updateUserPassword(
			@PathVariable(name = "id", required = true) 
			@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", 
			message = "id field must in UUID format") String id,
			@Valid @RequestBody AdmResetPassword adminResetPassword) {
		userSevice.updateUserPassword(UUID.fromString(id), adminResetPassword.getNewPassword());
		return ResponseEntity.ok("Reset user id " + id + " password successfully");
	}
	
	@DeleteMapping("/manage/users/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	ResponseEntity<Object> deleteUser(
			@PathVariable(name = "id", required = true) 
			@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", 
			message = "id field must in UUID format") String id) {
		userSevice.deleteUser(UUID.fromString(id));
		return ResponseEntity.ok("Delete user id: " + id + " successfully");
	}
}
