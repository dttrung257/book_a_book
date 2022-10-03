package com.uet.book_a_book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uet.book_a_book.service.UserSevice;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
	@Autowired
	private UserSevice userSevice;
	
	@Value("${my.server.security.secretKey}")
	private String secretKey;
	
	@GetMapping("/all-users")
	public ResponseEntity<Object> fetchAllUsers() {
		return ResponseEntity.ok().body(userSevice.findAll());
	}
	
	@GetMapping("/test")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String test() {
		return secretKey;
	}
}
