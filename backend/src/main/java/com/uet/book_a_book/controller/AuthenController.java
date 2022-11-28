package com.uet.book_a_book.controller;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uet.book_a_book.dto.AuthenRequest;
import com.uet.book_a_book.dto.AuthenResponse;
import com.uet.book_a_book.dto.RegisterRequest;
import com.uet.book_a_book.service.AuthenService;
import com.uet.book_a_book.service.UserSevice;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/authen")
@Validated
@Slf4j
public class AuthenController {
	@Autowired
	private UserSevice userSevice;
	@Autowired
	private AuthenService authenService;

	@PostMapping("/sign_in")
	public ResponseEntity<AuthenResponse> signIn(@Valid @RequestBody AuthenRequest request) {
		return ResponseEntity.ok(authenService.signIn(request));
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
		authenService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).body("You have successfully created an account. Please verify your email!");
	}

	@GetMapping("{email}/confirm_verification/{code}")
	public ResponseEntity<String> confirmVerification(
			@PathVariable("email") @Email(message = "email field is not valid") String email,
			@PathVariable("code") @NotBlank(message = "code field cannot be blank") String code) {
		userSevice.confirmEmailVerification(email.trim(), code.trim());
		log.info("Account with email {} activated successfully.", email);
		return ResponseEntity.ok("Account activation successful");
	}

	@GetMapping("/send_email/{email}")
	public ResponseEntity<String> sendEmailVerification(
			@PathVariable("email") @Email(message = "mail field is not valid") String email) {
		userSevice.sendEmailVerification(email.trim());
		log.info("Account with email {} send activation email.", email);
		return ResponseEntity.ok("Send email to " + email + " successfully");
	}
}
