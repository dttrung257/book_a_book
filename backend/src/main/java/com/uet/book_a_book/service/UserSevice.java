package com.uet.book_a_book.service;

import java.util.List;

import com.uet.book_a_book.model.AppUser;

public interface UserSevice {
	List<AppUser> findAll();
	AppUser save(AppUser user);
	AppUser findByEmail(String email);
}
