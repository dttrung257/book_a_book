package com.uet.book_a_book.controller;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uet.book_a_book.dto.NewPassword;
import com.uet.book_a_book.dto.RegisterRequest;
import com.uet.book_a_book.dto.ResetPassword;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.ResetPasswordToken;
import com.uet.book_a_book.entity.Role;
import com.uet.book_a_book.entity.util.ResetPasswordUtil;
import com.uet.book_a_book.entity.util.RoleName;
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

	@GetMapping("/user/forgot_password/{email}")
	public ResponseEntity<Object> forgotPassword(
			@PathVariable(name = "email") @Email(message = "Email is not valid") String email) {
		resetPasswordTokenService.forgotPassword(email);
		return ResponseEntity.ok("Send email success");
	}

	@GetMapping("/user/forgot_password/{email}/confirm_verification/{code}")
	public ResponseEntity<Object> confirmResetPassword(
			@PathVariable("email") @Email(message = "Email is not valid") String email,
			@PathVariable("code") @NotBlank(message = "Varification code cannot be blank") String code) {
		ResetPasswordToken token = resetPasswordTokenService.getResetPasswordToken(email, code);
		if (token == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reset token does not exist");
		}
		if (!token.getVerificationCode().equals(code)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong code");
		}
		return ResponseEntity.ok(token.getResetToken());
	}

	@PostMapping("/user/forgot_password/reset_password")
	public ResponseEntity<Object> resetPassword(@RequestBody ResetPassword resetPassword) {
		ResetPasswordToken token = resetPasswordTokenService.resetPassword(resetPassword.getEmail(),
				resetPassword.getResetToken(), resetPassword.getNewPassword());
		if (token == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reset token does not exist");
		}
		if (!token.getResetToken().equals(resetPassword.getResetToken())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong reset token");
		}
		token.setResetToken(resetPasswordUtil.generateResetToken());
		token.setVerificationCode(resetPasswordUtil.generateVerificationCode());
		resetPasswordTokenService.save(token);
		return ResponseEntity.ok("Reset password success");
	}

	@PostMapping("/user/change_password")
	public ResponseEntity<Object> changePassword(@Valid @RequestBody NewPassword request) {
		if (userSevice.changePassword(request.getEmail(), request.getOldPassword(), request.getNewPassword())) {
			return ResponseEntity.status(HttpStatus.OK).body("Change password successful");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong old password");
	}

	@PostMapping("/manage_user/create_account")
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
		return ResponseEntity.status(HttpStatus.CREATED).body("You have successfully created an account");
	}

	@GetMapping("manage_user/all_users")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> fetchAllUsers() {
		return ResponseEntity.ok().body(userSevice.findAllUsers());
	}

	@GetMapping("/manage_user/lock_account/{email}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> lockAccount(
			@PathVariable(name = "email") @Email(message = "Email is not valid") String email) {
		AppUser user = userSevice.lockAccount(email);
		if (!user.isLocked()) {
			if (user.getAuthorities().stream()
					.anyMatch(authority -> authority.getAuthority().equals(RoleName.ROLE_ADMIN))) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot lock admin account");
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body("Account lock successful");
	}

	@GetMapping("/manage_user/unlock_account/{email}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> unlockAccount(
			@PathVariable(name = "email") @Email(message = "Email is not valid") String email) {
		userSevice.unlockAccount(email);
		return ResponseEntity.status(HttpStatus.OK).body("Account unlock successful");
	}

	@GetMapping("/manage_user/active_account/{email}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> activeAccount(
			@PathVariable(name = "email") @Email(message = "Email is not valid") String email) {
		userSevice.activeAccount(email);
		return ResponseEntity.status(HttpStatus.OK).body("Account activation success");
	}
	
//	@GetMapping("/manage_user/delete_user/{email}")
//	@PreAuthorize("hasAuthority('ADMIN')")
}
