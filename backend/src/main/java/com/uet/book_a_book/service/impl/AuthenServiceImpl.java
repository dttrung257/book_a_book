package com.uet.book_a_book.service.impl;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.dto.AuthenRequest;
import com.uet.book_a_book.dto.AuthenResponse;
import com.uet.book_a_book.dto.RegisterRequest;
import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.Role;
import com.uet.book_a_book.entity.constant.Gender;
import com.uet.book_a_book.entity.constant.RoleName;
import com.uet.book_a_book.exception.account.AccountAlreadyExistsException;
import com.uet.book_a_book.exception.account.NotFoundGenderException;
import com.uet.book_a_book.repository.RoleRepository;
import com.uet.book_a_book.repository.UserRepository;
import com.uet.book_a_book.security.jwt.JwtUtil;
import com.uet.book_a_book.service.AuthenService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenServiceImpl implements AuthenService {
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public AuthenResponse signIn(AuthenRequest request) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getEmail().trim(),
				request.getPassword().trim());
		Authentication authentication = authenticationManager.authenticate(token);
		AppUser user = (AppUser) authentication.getPrincipal();
		String jwtToken = jwtUtil.generateJwtToken(request.getEmail().trim());
		log.info("User id: {} logged in.", user.getId());
		return new AuthenResponse(user.getAvatar(), user.getFirstName(), user.getLastName(),
				jwtToken, user.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList()).get(0));
	}

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
