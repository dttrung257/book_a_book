package com.uet.book_a_book.service;

import org.springframework.data.domain.Page;

import com.uet.book_a_book.dto.user.UpdateUser;
import com.uet.book_a_book.dto.user.UserDTO;
import com.uet.book_a_book.dto.user.UserInfo;
import com.uet.book_a_book.entity.AppUser;

public interface UserSevice {
	Page<UserDTO> fetchAllUsers(Integer page, Integer size);
	//Page<UserDTO> fetchByEmail(String email, Integer page, Integer size);
	Page<UserDTO> fetchByName(String name, Integer page, Integer size);
	AppUser save(AppUser user);
	AppUser findByEmail(String email);
	void changePassword(String oldPassword, String newPassword);
	UserInfo viewInformation();
	UserInfo updateUser(UpdateUser userInfo);
	AppUser lockAccount(String email);
	AppUser unlockAccount(String email);
	void activeAccount(String email);
	void resetUserPassword(String email, String newPassword);
	void confirmEmailVerification(String email, String code);
	void sendEmailVerification(String email);
	void deleteUser(String email);
}
