package com.uet.book_a_book.services.impl;

import java.util.Date;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.dtos.AuthRequest;
import com.uet.book_a_book.dtos.AuthResponse;
import com.uet.book_a_book.dtos.RegisterRequest;
import com.uet.book_a_book.models.AppUser;
import com.uet.book_a_book.models.Role;
import com.uet.book_a_book.models.constant.Gender;
import com.uet.book_a_book.models.constant.RoleName;
import com.uet.book_a_book.exception.account.AccountAlreadyExistsException;
import com.uet.book_a_book.exception.account.NotFoundGenderException;
import com.uet.book_a_book.repositories.RoleRepository;
import com.uet.book_a_book.repositories.UserRepository;
import com.uet.book_a_book.security.jwt.JwtUtil;
import com.uet.book_a_book.services.AuthService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Override
	public AuthResponse signIn(AuthRequest request) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getEmail().trim(),
				request.getPassword().trim());
		Authentication authentication = authenticationManager.authenticate(token);
		AppUser user = (AppUser) authentication.getPrincipal();
		final String jwtToken = jwtUtil.generateJwtToken(request.getEmail().trim());
		log.info("User id: {} logged in.", user.getId());
		return new AuthResponse(user.getAvatar(), user.getFirstName(), user.getLastName(),
				jwtToken, user.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList()).get(0));
	}

	@Transactional
	@Override
	public void register(RegisterRequest request) {
		if (userRepository.findByEmail(request.getEmail().trim()).orElse(null) != null) {
			throw new AccountAlreadyExistsException(
					String.format("User with email %s already exists", request.getEmail()));
		}
		AppUser user = new AppUser();
		user.setEmail(request.getEmail().trim());
		user.setPassword(passwordEncoder.encode(request.getPassword().trim()));
		user.setFirstName(request.getFirstName().trim());
		user.setLastName(request.getLastName().trim());
		if (request.getGender().trim().equalsIgnoreCase(Gender.GENDER_MALE) 
				|| request.getGender().equalsIgnoreCase(Gender.GENDER_FEMALE)
				|| request.getGender().equalsIgnoreCase(Gender.GENDER_OTHER)) {
			user.setGender(request.getGender().trim().toUpperCase());
		} else {
			throw new NotFoundGenderException("Not found gender: " + request.getGender());
		}
		user.setCreatedAt(new Date());
		Role roleUser = roleRepository.findByRoleName(RoleName.ROLE_USER);
		user.setRole(roleUser);
		userRepository.save(user);
		log.info("Create account id: {} successfully.", user.getId());
	}

}
