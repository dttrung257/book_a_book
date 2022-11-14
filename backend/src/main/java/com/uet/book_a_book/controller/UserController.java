package com.uet.book_a_book.controller;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

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
import com.uet.book_a_book.dto.user.AdminResetPassword;
import com.uet.book_a_book.dto.user.NewPassword;
import com.uet.book_a_book.dto.user.ResetPassword;
import com.uet.book_a_book.dto.user.UpdateUser;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.ResetPasswordToken;
import com.uet.book_a_book.entity.Role;
import com.uet.book_a_book.entity.constant.ResetPasswordUtil;
import com.uet.book_a_book.entity.constant.RoleName;
import com.uet.book_a_book.exception.account.CannotLockAdminAccountException;
import com.uet.book_a_book.exception.account.IncorrectResetPasswordCodeException;
import com.uet.book_a_book.exception.account.NotFoundResetPasswordTokenException;
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
			@PathVariable(name = "email") @Email(message = "Email field is not valid") String email) {
		resetPasswordTokenService.forgotPassword(email);
		return ResponseEntity.ok("Email sent successlly");
	}

	@GetMapping("/user/forgot_password/{email}/confirm_verification/{code}")
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

	@PutMapping("/user/forgot_password/reset_password")
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

	@PutMapping("/user/change_password")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	public ResponseEntity<Object> changePassword(@Valid @RequestBody NewPassword request) {
		userSevice.changePassword(request.getOldPassword(), request.getNewPassword());
		return ResponseEntity.ok("Change password successfully");
	}
	
	@GetMapping("/user/user_information")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	public ResponseEntity<Object> viewUserInformation() {
		return ResponseEntity.ok(userSevice.viewInformation());
	}
	
	@PutMapping("/user/update_user")
	@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
	public ResponseEntity<Object> updateUser(@Valid @RequestBody UpdateUser updateUser) {
		return ResponseEntity.ok(userSevice.updateUser(updateUser));
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

	@GetMapping("manage_user/fetch_all_users")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> fetchAllUsers(
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0, message = "Page field must be in integer format greater than or equal to 0") String page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1, message = "Size field must be in integer format greater than or equal to 1") String size) {
		return ResponseEntity.ok(userSevice.fetchAllUsers(Integer.parseInt(page), Integer.parseInt(size)));
	}
	
//	@GetMapping("manage_user/fetch_by_email")
//	@PreAuthorize("hasAuthority('ADMIN')")
//	public ResponseEntity<Object> fetchUserByEmail(
//			@RequestParam(name = "email", required = true) @NotBlank(message = "Email field cannot be blank") String email,
//			@RequestParam(name = "page", required = false, defaultValue = "0") 
//			@Min(value = 0, message = "Page field must be in integer format greater than or equal to 0") String page,
//			@RequestParam(name = "size", required = false, defaultValue = "10") 
//			@Min(value = 1, message = "Size field must be in integer format greater than or equal to 1") String size) {
//		return ResponseEntity.ok(userSevice.fetchByEmail(email, Integer.parseInt(page), Integer.parseInt(size)));
//	}
	
	@GetMapping("manage_user/fetch_by_name")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> fetchUserByName(
			@RequestParam(name = "name", required = true) @NotBlank(message = "Name field cannot be blank") String name,
			@RequestParam(name = "page", required = false, defaultValue = "0") 
			@Min(value = 0, message = "Page field must be in integer format greater than or equal to 0") String page,
			@RequestParam(name = "size", required = false, defaultValue = "10") 
			@Min(value = 1, message = "Size field must be in integer format greater than or equal to 1") String size) {
		return ResponseEntity.ok(userSevice.fetchByName(name, Integer.parseInt(page), Integer.parseInt(size)));
	}

	@PutMapping("/manage_user/lock_account/{email}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> lockAccount(
			@PathVariable(name = "email") @Email(message = "Email is not valid") String email) {
		AppUser user = userSevice.lockAccount(email);
		if (!user.isLocked()) {
			if (user.getAuthorities().stream()
					.anyMatch(authority -> authority.getAuthority().equals(RoleName.ROLE_ADMIN))) {
				throw new CannotLockAdminAccountException("Cannot lock admin account");
			}
		}
		return ResponseEntity.ok("Lock account successfully");
	}

	@PutMapping("/manage_user/unlock_account/{email}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> unlockAccount(
			@PathVariable(name = "email") @Email(message = "Email is not valid") String email) {
		userSevice.unlockAccount(email);
		return ResponseEntity.ok("Unlock account successfully");
	}

	@PutMapping("/manage_user/activate_account/{email}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> activeAccount(
			@PathVariable(name = "email") @Email(message = "Email is not valid") String email) {
		userSevice.activeAccount(email);
		return ResponseEntity.ok("Account activation success");
	}
	
	@PutMapping("/manage_user/reset_user_password")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> resetUserPassword(@Valid @RequestBody AdminResetPassword adminResetPassword) {
		userSevice.resetUserPassword(adminResetPassword.getEmail(), adminResetPassword.getNewPassword());
		return ResponseEntity.ok("Reset user's password successfully");
	}
	
	@DeleteMapping("/manage_user/delete_user/{email}")
	@PreAuthorize("hasAuthority('ADMIN')")
	ResponseEntity<Object> deleteUser(
			@PathVariable(name = "email") @Email(message = "Email is not valid") String email) {
		userSevice.deleteUser(email);
		return ResponseEntity.ok("Delete user with email " + email + " successfully");
	}
}
