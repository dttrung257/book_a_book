package com.uet.book_a_book.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uet.book_a_book.dto.AuthenticationRequest;
import com.uet.book_a_book.dto.AuthenticationResponse;
import com.uet.book_a_book.dto.RegisterRequest;
import com.uet.book_a_book.email.EmailSenderService;
import com.uet.book_a_book.email.EmailValidator;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.Role;
import com.uet.book_a_book.entity.util.RoleName;
import com.uet.book_a_book.security.jwt.JwtUtil;
import com.uet.book_a_book.service.RoleService;
import com.uet.book_a_book.service.UserSevice;

@RestController
@RequestMapping("/api/authen")
public class AuthenticationController {

	private @Autowired UserSevice userSevice;
	private @Autowired RoleService roleService;
	private @Autowired AuthenticationManager authenticationManager;
	private @Autowired PasswordEncoder passwordEncoder;
	private @Autowired JwtUtil jwtUtil;
	private @Autowired EmailValidator emailValidator;
	private @Autowired EmailSenderService emailSenderService;

	@PostMapping("/sign_in")
	public ResponseEntity<Object> signIn(@Valid @RequestBody AuthenticationRequest request) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getEmail(),
				request.getPassword());
		Authentication authentication = authenticationManager.authenticate(token);
		AppUser user = (AppUser) authentication.getPrincipal();
		String jwtToken = jwtUtil.generateJwtToken(request.getEmail());
		return ResponseEntity.ok().body(
				new AuthenticationResponse(user.getFirstName(), user.getLastName(), jwtToken, user.getAuthorities()));
	}

	@PostMapping("/register")
	public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest request) {
		if (userSevice.findByEmail(request.getEmail()) != null) {
			throw new BadCredentialsException(String.format("User with email %s already exists", request.getEmail()));
		}
		if (!emailValidator.validateEmail(request.getEmail())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(String.format("Email %s does not exists on the Internet", request.getEmail()));
		}
		AppUser user = new AppUser();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setCreatedAt(new Date());
		Role roleUser = roleService.findByRoleName(RoleName.ROLE_USER);
		user.setRole(roleUser);

		String verificationCode = emailSenderService.generateVerificationCode();
		user.setEmailVerificationCode(verificationCode);
		String emailBody = emailSenderService.buildEmailVerificationAccount(request.getEmail(),
				request.getFirstName() + " " + request.getLastName(), verificationCode);
		emailSenderService.sendEmail(request.getEmail(), emailBody);

		userSevice.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body("You have successfully created an account");
	}

	@GetMapping("{email}/confirm_verification/{code}")
	public ResponseEntity<Object> confirmVerification(@PathVariable("email") String email,
			@PathVariable("code") String code) {
		AppUser user = userSevice.confirmEmailVerification(email, code);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong code");
		}
		if (user.isEmailVerified()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account has been activated");
		}
		if (user.isLocked()) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Account has been locked");
		}
		return ResponseEntity.status(HttpStatus.OK).body("Account activation success");
	}

	@GetMapping("/resend_email/{email}")
	public ResponseEntity<Object> resendEmailVerification(@PathVariable("email") String email) {
		AppUser user = userSevice.resendEmailVerification(email);
		if (user.isLocked()) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Account has been locked");
		}
		if (user.isEmailVerified()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account has been activated");
		}
		return ResponseEntity.status(HttpStatus.OK).body("Resend email to " + email + " successfully");
	}
}
