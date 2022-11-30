package com.uet.book_a_book.controller;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.uet.book_a_book.dto.user.UserDTO;
import com.uet.book_a_book.dto.user.UserInfo;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.constant.Const;
import com.uet.book_a_book.service.ResetPasswordTokenService;
import com.uet.book_a_book.service.UserSevice;
import com.uet.book_a_book.validator.IdConstraint;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Validated
@Slf4j
public class UserController {
	@Autowired
	private UserSevice userSevice;
	@Autowired
	private ResetPasswordTokenService resetPasswordTokenService;

	@GetMapping("/users")
	public ResponseEntity<UserInfo> getUserInfo() {
		return ResponseEntity.ok(userSevice.getUserInfo());
	}

	@PutMapping("/users")
	public ResponseEntity<UserInfo> updateUser(@Valid @RequestBody UpdateUser updateUser) {
		return ResponseEntity.ok(userSevice.updateUser(updateUser));
	}
	
	@PutMapping("/users/change_password")
	public ResponseEntity<String> changePassword(@Valid @RequestBody NewPassword request) {
		userSevice.changePassword(request.getOldPassword().trim(), request.getNewPassword().trim());
		return ResponseEntity.ok("Change password successfully");
	}
	
	@GetMapping("/users/forgot_password/{email}")
	public ResponseEntity<String> forgotPassword(
			@PathVariable(name = "email") @Email(message = "email field is not valid") String email) {
		resetPasswordTokenService.forgotPassword(email.trim());
		return ResponseEntity.ok("Send email successlly");
	}

	@GetMapping("/users/forgot_password/{email}/confirm_verification/{code}")
	public ResponseEntity<String> confirmResetPassword(
			@PathVariable("email") @Email(message = "email field is not valid") String email,
			@PathVariable("code") @NotBlank(message = "code field is madatory") String code) {
		return ResponseEntity.ok(resetPasswordTokenService.getResetPasswordToken(email.trim(), code.trim()));
	}

	@PutMapping("/users/forgot_password/reset_password")
	public ResponseEntity<String> resetPassword(@RequestBody ResetPassword resetPassword) {
		resetPasswordTokenService.resetPassword(resetPassword.getEmail().trim(), resetPassword.getResetToken().trim(),
				resetPassword.getNewPassword().trim());
		return ResponseEntity.ok("Reset password successfully");
	}

	@PostMapping("/manage/users")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<UserDTO> createAccount(@Valid @RequestBody RegisterRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userSevice.createAccount(request));
	}

	@GetMapping("manage/users/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<UserDTO> getUserById(@PathVariable(name = "id", required = true) @IdConstraint String id) {
		return ResponseEntity.ok(userSevice.getUserById(UUID.fromString(id)));
	}

	@GetMapping("manage/users")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Page<UserDTO>> getUsers(
			@RequestParam(name = "name", required = false, defaultValue = "") String name,
			@RequestParam(name = "page", required = false, defaultValue = Const.DEFAULT_PAGE_NUMBER) @Min(value = 0) Integer page,
			@RequestParam(name = "size", required = false, defaultValue = Const.DEFAULT_PAGE_SIZE) @Min(value = 1) Integer size) {
		return ResponseEntity.ok(userSevice.getUsers(name.trim(), page, size));
	}

	@PutMapping("/manage/users/{id}/status")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> updateStatus(@PathVariable(name = "id", required = true) @IdConstraint String id,
			@Valid @RequestBody UpdateUserStatus updateUserStatus) {
		return ResponseEntity.ok(userSevice.updateUserStatus(updateUserStatus, UUID.fromString(id)));
	}

	@PutMapping("/manage/users/{id}/password")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> updateUserPassword(
			@PathVariable(name = "id", required = true) @IdConstraint String id,
			@Valid @RequestBody AdmResetPassword adminResetPassword) {
		userSevice.updateUserPassword(UUID.fromString(id), adminResetPassword.getNewPassword().trim());
		return ResponseEntity.ok("Reset user id " + id + " password successfully");
	}

	@DeleteMapping("/manage/users/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	ResponseEntity<String> deleteUser(@PathVariable(name = "id", required = true) @IdConstraint String id) {
		userSevice.deleteUser(UUID.fromString(id));
		log.info("Admin id: {} deleted user id: {}.",
				((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(), id);
		return ResponseEntity.ok("Delete user id: " + id + " successfully");
	}
}
