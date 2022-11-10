package com.uet.book_a_book.controller;

import java.util.Date;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uet.book_a_book.dto.AuthenticationRequest;
import com.uet.book_a_book.dto.AuthenticationResponse;
import com.uet.book_a_book.dto.RegisterRequest;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.Role;
import com.uet.book_a_book.entity.util.RoleName;
import com.uet.book_a_book.exception.account.AccountAlreadyExistsException;
import com.uet.book_a_book.security.jwt.JwtUtil;
import com.uet.book_a_book.service.RoleService;
import com.uet.book_a_book.service.UserSevice;

@RestController
@RequestMapping("/api/authen")
@Validated
public class AuthenticationController {
	@Autowired
	private UserSevice userSevice;
	@Autowired
	private RoleService roleService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtUtil jwtUtil;
//	@Autowired
//	private EmailValidator emailValidator;
//	@Autowired
//	private EmailSenderService emailSenderService;

	@PostMapping("/sign_in")
	public ResponseEntity<Object> signIn(@Valid @RequestBody AuthenticationRequest request) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getEmail(),
				request.getPassword());
		Authentication authentication = authenticationManager.authenticate(token);
		AppUser user = (AppUser) authentication.getPrincipal();
		String jwtToken = jwtUtil.generateJwtToken(request.getEmail());
		return ResponseEntity.ok(new AuthenticationResponse(user.getAvatar(), user.getFirstName(), user.getLastName(),
				jwtToken, user.getAuthorities().stream().toList().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList())));
	}

	@PostMapping("/register")
	public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest request) {
		if (userSevice.findByEmail(request.getEmail()) != null) {
			throw new AccountAlreadyExistsException(
					String.format("User with email %s already exists", request.getEmail()));
		}
//		if (!emailValidator.checkEmailExists(request.getEmail())) {
//			throw new EmailNotExistsOnTheInternetException(
//					String.format("Email %s does not exist on the internet", request.getEmail()));
//		}
		AppUser user = new AppUser();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setCreatedAt(new Date());
		Role roleUser = roleService.findByRoleName(RoleName.ROLE_USER);
		user.setRole(roleUser);

//		String verificationCode = emailSenderService.generateVerificationCode();
//		user.setEmailVerificationCode(verificationCode);
//		String emailBody = emailSenderService.buildEmailVerificationAccount(request.getEmail(),
//				request.getFirstName() + " " + request.getLastName(), verificationCode);
//		emailSenderService.sendEmail(request.getEmail(), emailBody);

		userSevice.save(user);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body("You have successfully created an account. Please verify your email!");
	}

	@GetMapping("{email}/confirm_verification/{code}")
	public ResponseEntity<Object> confirmVerification(
			@PathVariable("email") @Email(message = "Email field is not valid") String email,
			@PathVariable("code") @NotBlank(message = "Varification code field cannot be blank") String code) {
		userSevice.confirmEmailVerification(email, code);
		return ResponseEntity.ok("Account activation successful");
	}

	@GetMapping("/send_email/{email}")
	public ResponseEntity<Object> resendEmailVerification(
			@PathVariable("email") @Email(message = "Email field is not valid") String email) {
		userSevice.sendEmailVerification(email);
		return ResponseEntity.ok("Send email to " + email + " successfully");
	}
}
