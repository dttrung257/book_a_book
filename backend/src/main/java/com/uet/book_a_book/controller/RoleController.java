package com.uet.book_a_book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uet.book_a_book.email.EmailValidator;
import com.uet.book_a_book.service.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private EmailValidator emailValidator;
	
	@GetMapping("/all_roles")
	public ResponseEntity<Object> fetchAllRoles() {
		return ResponseEntity.ok(roleService.findAll());
	}
	
	@GetMapping("test_api/{email}")
	public ResponseEntity<Object> test(@PathVariable("email") String email) {
		return ResponseEntity.ok(emailValidator.checkEmailExists(email));
	}
}
