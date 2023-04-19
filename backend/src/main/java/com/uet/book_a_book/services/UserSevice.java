package com.uet.book_a_book.services;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.uet.book_a_book.dtos.RegisterRequest;
import com.uet.book_a_book.dtos.user.UpdateUser;
import com.uet.book_a_book.dtos.user.UpdateUserStatus;
import com.uet.book_a_book.dtos.user.UserDTO;
import com.uet.book_a_book.dtos.user.UserInfo;
import com.uet.book_a_book.models.AppUser;

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
