package com.uet.book_a_book.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.model.AppUser;
import com.uet.book_a_book.repository.UserRepository;
import com.uet.book_a_book.service.UserSevice;

@Service
public class UserServiceImpl implements UserSevice {
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<AppUser> findAll() {
		return userRepository.findAll();
	}

	@Override
	public AppUser save(AppUser user) {
		return userRepository.save(user);
	}

	@Override
	public AppUser findByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}
}
