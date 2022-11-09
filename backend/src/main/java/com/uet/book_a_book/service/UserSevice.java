package com.uet.book_a_book.service;

import java.util.List;

import com.uet.book_a_book.dto.user.UpdateUser;
import com.uet.book_a_book.dto.user.UserDTO;
import com.uet.book_a_book.dto.user.UserInfo;
import com.uet.book_a_book.entity.AppUser;

public interface UserSevice {
	List<UserDTO> fetchAllUsers();
	List<UserDTO> fetchByEmail(String email);
	List<UserDTO> fetchByName(String name);
	AppUser save(AppUser user);
	AppUser findByEmail(String email);
	boolean changePassword(String email, String oldPassword, String newPassword);
	UserInfo viewInformation();
	UserInfo updateUser(UpdateUser userInfo);
	AppUser lockAccount(String email);
	AppUser unlockAccount(String email);
	void activeAccount(String email);
	void confirmEmailVerification(String email, String code);
	void sendEmailVerification(String email);
	void deleteUser(String email);
}
