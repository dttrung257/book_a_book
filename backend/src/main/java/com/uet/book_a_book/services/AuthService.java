package com.uet.book_a_book.services;

import com.uet.book_a_book.dtos.AuthRequest;
import com.uet.book_a_book.dtos.AuthResponse;
import com.uet.book_a_book.dtos.RegisterRequest;

public interface AuthService {
	AuthResponse signIn(AuthRequest request);
	void register(RegisterRequest request);
}
