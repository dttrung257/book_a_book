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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uet.book_a_book.dto.AuthenticationRequest;
import com.uet.book_a_book.dto.UserRegisterRequest;
import com.uet.book_a_book.dto.AdminRegisterRequest;
import com.uet.book_a_book.dto.response.AuthenticationResponse;
import com.uet.book_a_book.model.AppUser;
import com.uet.book_a_book.model.Const;
import com.uet.book_a_book.model.Role;
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

	@PostMapping("/sign-in")
	public ResponseEntity<Object> signIn(@Valid @RequestBody AuthenticationRequest request) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getEmail(),
				request.getPassword());
		Authentication authentication = authenticationManager.authenticate(token);
		
		//SecurityContextHolder.getContext().setAuthentication(authentication);
		AppUser user = (AppUser) authentication.getPrincipal();
		String jwtToken = jwtUtil.generateJwtToken(request.getEmail());
		return ResponseEntity.ok().body(
				new AuthenticationResponse(user.getFirstName(), user.getLastName(), jwtToken, user.getAuthorities()));
	}

	@PostMapping("/register")
	public ResponseEntity<Object> register(@Valid @RequestBody UserRegisterRequest request) {
		if (userSevice.findByEmail(request.getEmail()) != null) {
			throw new BadCredentialsException("The email is already existed.");
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
		userSevice.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body("Create account successfully");
	}
	
	@PostMapping("/admin/register")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Object> adminRegister(@Valid @RequestBody AdminRegisterRequest adminRequest) {
		if (userSevice.findByEmail(adminRequest.getEmail()) != null) {
			throw new BadCredentialsException("The email is already existed.");
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
			if (registerRole.equalsIgnoreCase(Const.ROLE_ADMIN)
					|| registerRole.equalsIgnoreCase(Const.ROLE_MANAGER)
					|| registerRole.equalsIgnoreCase(Const.ROLE_USER)) {
				Role role = roleService.findByRoleName(registerRole);
				roles.add(role);
			}
		});
		user.setRoles(roles);
		userSevice.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body("Create account from admin successfully");
	}
}
