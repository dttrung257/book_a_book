package com.uet.book_a_book.service;

import java.util.List;

import com.uet.book_a_book.domain.AppUser;
import com.uet.book_a_book.dto.UserDTO;

public interface UserSevice {
	List<UserDTO> findAllUsers();
	AppUser save(AppUser user);
	AppUser findByEmail(String email);
	AppUser changeUserStatus(String email, String statusName, boolean status);
	String confirmEmailVerification(String email, String code);
	String resendEmailVerification(String email);
}
