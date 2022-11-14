package com.uet.book_a_book.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.uet.book_a_book.dto.user.UpdateUser;
import com.uet.book_a_book.dto.user.UserDTO;
import com.uet.book_a_book.dto.user.UserInfo;
import com.uet.book_a_book.entity.AppUser;

public interface UserSevice {
	Page<UserDTO> getAllUsers(Integer page, Integer size);
	Page<UserDTO> getUsersByName(String name, Integer page, Integer size);
	
	AppUser save(AppUser user);
	AppUser findByEmail(String email);
	
	void changePassword(String oldPassword, String newPassword);
	UserInfo getUserInfo();
	UserInfo updateUser(UpdateUser userInfo);
	
	void lockAccount(UUID id);
	void unlockAccount(UUID id);
	void activeAccount(UUID id);
	
	void updateUserPassword(UUID id, String newPassword);
	void confirmEmailVerification(String email, String code);
	void sendEmailVerification(String email);
	
	void deleteUser(UUID id);
}
