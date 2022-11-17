package com.uet.book_a_book.mapper;

import com.uet.book_a_book.dto.user.UserDTO;
import com.uet.book_a_book.dto.user.UserInfo;
import com.uet.book_a_book.entity.AppUser;

public interface UserMapper {
	UserDTO mapToUserDTO(AppUser user);
	UserInfo mapToUserInfo(AppUser user);
}
