package com.uet.book_a_book.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.uet.book_a_book.dto.RegisterRequest;
import com.uet.book_a_book.dto.user.UpdateUser;
import com.uet.book_a_book.dto.user.UpdateUserStatus;
import com.uet.book_a_book.dto.user.UserDTO;
import com.uet.book_a_book.dto.user.UserInfo;
import com.uet.book_a_book.entity.AppUser;

public interface UserSevice {
	// For users
	UserInfo getUserInfo();
	
	// For admins
	Page<UserDTO> getUsers(String name, Integer page, Integer size);

	UserDTO getUserById(UUID id);

	AppUser save(AppUser user);

	AppUser findByEmail(String email);

	// For users
	void changePassword(String oldPassword, String newPassword);

	UserInfo updateUser(UpdateUser userInfo);

	// For admins
	UserDTO createAccount(RegisterRequest request);
	
	String updateUserStatus(UpdateUserStatus updateUserStatus, UUID id);

	void updateUserPassword(UUID id, String newPassword);

	// For users
	void confirmEmailVerification(String email, String code);

	void sendEmailVerification(String email);

	// For admins
	void deleteUser(UUID id);
}
