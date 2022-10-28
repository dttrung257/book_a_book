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
	
	AppUser activeAccount(String email);
	AppUser confirmEmailVerification(String email, String code);
	AppUser resendEmailVerification(String email);
}
