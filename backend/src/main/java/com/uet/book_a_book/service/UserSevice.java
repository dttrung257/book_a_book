package com.uet.book_a_book.service;

import java.util.List;

import com.uet.book_a_book.dto.UserDTO;
import com.uet.book_a_book.entity.AppUser;

public interface UserSevice {
	List<UserDTO> findAllUsers();
	AppUser save(AppUser user);
	AppUser findByEmail(String email);
	boolean changePassword(String email, String oldPassword, String newPassword);
	AppUser lockAccount(String email);
	AppUser unlockAccount(String email);
	
	void activeAccount(String email);
	void confirmEmailVerification(String email, String code);
	void resendEmailVerification(String email);
	void deleteUser(String email);
}
