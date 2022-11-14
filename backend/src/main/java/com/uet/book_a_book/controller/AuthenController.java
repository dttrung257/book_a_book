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

import com.uet.book_a_book.dto.AuthenRequest;
import com.uet.book_a_book.dto.AuthenResponse;
import com.uet.book_a_book.dto.RegisterRequest;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.Role;
import com.uet.book_a_book.entity.constant.Gender;
import com.uet.book_a_book.entity.constant.RoleName;
import com.uet.book_a_book.exception.account.AccountAlreadyExistsException;
import com.uet.book_a_book.exception.account.NotFoundGenderException;
import com.uet.book_a_book.security.jwt.JwtUtil;
import com.uet.book_a_book.service.RoleService;
import com.uet.book_a_book.service.UserSevice;

@RestController
@RequestMapping("/api/authen")
@Validated
public class AuthenController {
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

	@PostMapping("/sign_in")
	public ResponseEntity<Object> signIn(@Valid @RequestBody AuthenRequest request) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getEmail(),
				request.getPassword());
		Authentication authentication = authenticationManager.authenticate(token);
		AppUser user = (AppUser) authentication.getPrincipal();
		String jwtToken = jwtUtil.generateJwtToken(request.getEmail());
		return ResponseEntity.ok(new AuthenResponse(user.getAvatar(), user.getFirstName(), user.getLastName(),
				jwtToken, user.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList()).get(0)));
	}

	@PostMapping("/register")
	public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest request) {
		if (userSevice.findByEmail(request.getEmail()) != null) {
			throw new AccountAlreadyExistsException(
					String.format("User with email %s already exists", request.getEmail()));
		}
		AppUser user = new AppUser();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setFirstName(request.getFirstName().trim());
		user.setLastName(request.getLastName().trim());
		if (request.getGender().equalsIgnoreCase(Gender.GENDER_MALE) 
				|| request.getGender().equalsIgnoreCase(Gender.GENDER_FEMALE)
				|| request.getGender().equalsIgnoreCase(Gender.GENDER_OTHER)) {
			user.setGender(request.getGender().toUpperCase());
		} else {
			throw new NotFoundGenderException("Not found gender: " + request.getGender());
		}
		user.setCreatedAt(new Date());
		Role roleUser = roleService.findByRoleName(RoleName.ROLE_USER);
		user.setRole(roleUser);

		userSevice.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body("You have successfully created an account. Please verify your email!");
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