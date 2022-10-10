package com.uet.book_a_book.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.uet.book_a_book.domain.AppUser;
import com.uet.book_a_book.domain.Const;
import com.uet.book_a_book.domain.Role;
import com.uet.book_a_book.dto.AdminRegisterRequest;
import com.uet.book_a_book.dto.AuthenticationRequest;
import com.uet.book_a_book.dto.UserRegisterRequest;
import com.uet.book_a_book.dto.response.AuthenticationResponse;
import com.uet.book_a_book.email.EmailSenderService;
import com.uet.book_a_book.email.EmailValidator;
import com.uet.book_a_book.security.jwt.JwtUtil;
import com.uet.book_a_book.service.RoleService;
import com.uet.book_a_book.service.UserSevice;

@RestController
@RequestMapping("/api/authenticate")
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

		// SecurityContextHolder.getContext().setAuthentication(authentication);
		AppUser user = (AppUser) authentication.getPrincipal();
		String jwtToken = jwtUtil.generateJwtToken(request.getEmail());
		return ResponseEntity.ok().body(
				new AuthenticationResponse(user.getFirstName(), user.getLastName(), jwtToken, user.getAuthorities()));
	}

	@PostMapping("/register")
	public ResponseEntity<Object> register(@Valid @RequestBody UserRegisterRequest request) {
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
		Set<Role> roles = new HashSet<>();
		Role roleUser = roleService.findByRoleName(Const.ROLE_USER);
		roles.add(roleUser);
		user.setRoles(roles);
		String verificationCode = emailSenderService.generateVerificationCode();
		user.setEmailVerificationCode(verificationCode);

		String builder = emailSenderService.buildEmail(request.getEmail(),
				request.getFirstName() + " " + request.getLastName(), verificationCode);
		emailSenderService.sendEmail(request.getEmail(), builder);
		userSevice.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body("You have successfully created an account");
	}

	@PostMapping("/admin/register")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> adminRegister(@Valid @RequestBody AdminRegisterRequest adminRequest) {
		if (userSevice.findByEmail(adminRequest.getEmail()) != null) {
			throw new BadCredentialsException(String.format("User with email %s already exists", adminRequest.getEmail()));
		}
		AppUser user = new AppUser();
		user.setEmail(adminRequest.getEmail());
		user.setPassword(passwordEncoder.encode(adminRequest.getPassword()));
		user.setFirstName(adminRequest.getFirstName());
		user.setLastName(adminRequest.getLastName());
		user.setCreatedAt(new Date());
		Set<String> registerRoles = adminRequest.getRoles();
		Set<Role> roles = new HashSet<>();
		registerRoles.forEach(registerRole -> {
			if (registerRole.equalsIgnoreCase(Const.ROLE_REPO_MANAGER)
					|| registerRole.equalsIgnoreCase(Const.ROLE_CUSTOMER_CARE_STAFF)) {
				Role role = roleService.findByRoleName(registerRole);
				roles.add(role);
			}
		});
		user.setRoles(roles);
		userSevice.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body("Create account from admin successfully");
	}

	@PostMapping("/root_admin/register")
	@PreAuthorize("hasAuthority('ROOT_ADMIN')")
	public ResponseEntity<Object> rootRegister(@Valid @RequestBody AdminRegisterRequest rootRequest) {
		if (userSevice.findByEmail(rootRequest.getEmail()) != null) {
			throw new BadCredentialsException(String.format("User with email %s already exists", rootRequest.getEmail()));
		}
		if (rootRequest.getRoles().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account needs to have permissions");
		}
		AppUser user = new AppUser();
		user.setEmail(rootRequest.getEmail());
		user.setPassword(passwordEncoder.encode(rootRequest.getPassword()));
		user.setFirstName(rootRequest.getFirstName());
		user.setLastName(rootRequest.getLastName());
		user.setCreatedAt(new Date());
		Set<String> registerRoles = rootRequest.getRoles();
		Set<Role> roles = new HashSet<>();
		registerRoles.forEach(registerRole -> {
			if (registerRole.equalsIgnoreCase(Const.ROLE_ADMIN)
					|| registerRole.equalsIgnoreCase(Const.ROLE_REPO_MANAGER)
					|| registerRole.equalsIgnoreCase(Const.ROLE_CUSTOMER_CARE_STAFF)
					|| registerRole.equalsIgnoreCase(Const.ROLE_USER)) {
				Role role = roleService.findByRoleName(registerRole);
				roles.add(role);
			}
		});
		if (roles.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account needs to have permissions");
		}
		user.setRoles(roles);
		userSevice.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body("Create account from root successfully");
	}
	
	@GetMapping("{email}/confirm_verification/{code}")
	public ResponseEntity<Object> confirmVerification(@PathVariable("email") String email,
														@PathVariable("code") String code) {
		String confirmMessage = userSevice.confirmEmailVerification(email, code);
		if (confirmMessage.equals("VERIFIED")) {
			return ResponseEntity.status(HttpStatus.OK).body("Account has been activated");
		} else if (confirmMessage.equals("WRONG_CODE")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong verification code");
		} else if (confirmMessage.equals("LOCKED_ACCOUNT")) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Account has been locked");
		}
		return ResponseEntity.status(HttpStatus.OK).body("Account activated successfully");
	}
	
	@GetMapping("/resend_email/{email}")
	public ResponseEntity<Object> resendEmailVerification(@PathVariable("email") String email) {
		String confirmMessage = userSevice.resendEmailVerification(email);
		if (confirmMessage.equals("VERIFIED")) {
			return ResponseEntity.status(HttpStatus.OK).body("Account has been activated");
		} else if (confirmMessage.equals("LOCKED_ACCOUNT")) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Account has been locked");
		}
		return ResponseEntity.status(HttpStatus.OK).body("Resend email to " + email + " successfully");
	}
}
