package com.uet.book_a_book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uet.book_a_book.domain.AppUser;
import com.uet.book_a_book.domain.Const;
import com.uet.book_a_book.service.UserSevice;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasAnyAuthority('ROOT_ADMIN', 'ADMIN')")
public class UserController {
	@Autowired
	private UserSevice userSevice;

	@GetMapping("/all_users")
	public ResponseEntity<Object> fetchAllUsers() {
		return ResponseEntity.ok().body(userSevice.findAllUsers());
	}

	@GetMapping("/update_user")
	public ResponseEntity<Object> changeUserStatus(@RequestParam("email") String email,
													@RequestParam("status_name") String statusName, 
													@RequestParam("status") boolean status) {
		AppUser user = userSevice.findByEmail(email);
		if (user != null) {
			if (user.getAuthorities().stream()
					.anyMatch(authority -> (authority.getAuthority().equalsIgnoreCase(Const.ROLE_ROOT_ADMIN)
							|| authority.getAuthority().equals(Const.ROLE_ADMIN)))) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Insufficient permission");
			}
		}
		return ResponseEntity.ok().body(userSevice.changeUserStatus(email, statusName, status));
	}
}
