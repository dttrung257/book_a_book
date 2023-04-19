package com.uet.book_a_book.mapper.impl;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.uet.book_a_book.dtos.user.UserDTO;
import com.uet.book_a_book.dtos.user.UserInfo;
import com.uet.book_a_book.models.AppUser;
import com.uet.book_a_book.mapper.UserMapper;

@Component
public class UserMapperImpl implements UserMapper {

	@Override
	public UserDTO mapToUserDTO(AppUser user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setFirstName(user.getFirstName());
		userDTO.setLastName(user.getLastName());
		userDTO.setEmail(user.getEmail());
		userDTO.setGender(user.getGender());
		userDTO.setPhoneNumber(user.getPhoneNumber());
		userDTO.setAddress(user.getAddress());
		userDTO.setAvatar(user.getAvatar());
		userDTO.setCreatedAt(user.getCreatedAt());
		userDTO.setUpdatedAt(user.getUpdatedAt());
		userDTO.setLocked(user.isLocked());
		userDTO.setEmailVerified(user.isEmailVerified());
		userDTO.setAuthority(user.getAuthorities().stream().map(auth -> auth.getAuthority())
									.collect(Collectors.toList()).get(0));
		return userDTO;
	}

	@Override
	public UserInfo mapToUserInfo(AppUser user) {
		UserInfo userInfo = new UserInfo();
		userInfo.setFirstName(user.getFirstName());
		userInfo.setLastName(user.getLastName());
		userInfo.setEmail(user.getEmail());
		userInfo.setGender(user.getGender());
		userInfo.setPhoneNumber(user.getPhoneNumber());
		userInfo.setAddress(user.getAddress());
		userInfo.setAvatar(user.getAvatar());
		return userInfo;
	}
}
