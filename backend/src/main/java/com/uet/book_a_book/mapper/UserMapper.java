package com.uet.book_a_book.mapper;

import com.uet.book_a_book.dtos.user.UserDTO;
import com.uet.book_a_book.dtos.user.UserInfo;
import com.uet.book_a_book.models.AppUser;

public interface UserMapper {
	UserDTO mapToUserDTO(AppUser user);
	UserInfo mapToUserInfo(AppUser user);
}
